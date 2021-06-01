package funcify.tool;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-31
 */
public interface SyncList<E> extends Iterable<E> {

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <E> SyncList<E> of(final E... e) {
        return SyncListFactory.of()
                              .ofConcurrent(e);
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    static <E> SyncList<E> ofNonConcurrent(final E... e) {
        return SyncListFactory.of()
                              .ofNonConcurrent(e);
    }

    static <E> SyncList<E> fromIterable(final Iterable<? extends E> iterable) {
        return SyncListFactory.of()
                              .fromIterableConcurrent(iterable);
    }

    static <E> SyncList<E> fromIterableNonConcurrent(final Iterable<? extends E> iterable) {
        return SyncListFactory.of()
                              .fromIterableNonConcurrent(iterable);
    }

    static <E> SyncList<E> fromStream(final Stream<? extends E> stream) {
        return SyncListFactory.of()
                              .fromStreamConcurrent(stream);
    }

    static <E> SyncList<E> fromStreamNonConcurrent(final Stream<? extends E> stream) {
        return SyncListFactory.of()
                              .fromStreamNonConcurrent(stream);
    }

    static <E> SyncList<E> empty() {
        return SyncListFactory.of()
                              .empty();
    }

    static <E> SyncList<E> emptyNonConcurrent() {
        return SyncListFactory.of()
                              .emptyConcurrent();
    }

    default SyncListFactory factory() {
        return SyncListFactory.of();
    }

    default SyncList<E> prepend(E e) {
        return factory().prepend(this,
                                 e);
    }

    default SyncList<E> prependAll(Iterable<? extends E> list) {
        return factory().prependAll(this,
                                    list);
    }

    default SyncList<E> append(E e) {
        return factory().append(this,
                                e);
    }

    default SyncList<E> appendAll(Iterable<? extends E> list) {
        return factory().appendAll(this,
                                   list);
    }

    default SyncList<E> updateAt(int i,
                                 E e) {
        return factory().updateAt(this,
                                  i,
                                  e);
    }

    default SyncList<E> insertAt(int i,
                                 E e) {
        return factory().insertAt(this,
                                  i,
                                  e);
    }

    default SyncList<E> insertAt(int i,
                                 Iterable<? extends E> list) {
        return factory().insertAt(this,
                                  i,
                                  list);
    }

    default SyncList<E> removeValue(E e) {
        return factory().removeValue(this,
                                     e);
    }

    default SyncList<E> removeAt(int i) {
        return factory().removeAt(this,
                                  i);
    }

    default int size() {
        return factory().size(this);
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default Optional<E> get(int index) {
        return factory().get(this,
                             index);
    }

    default E getOrElse(int index,
                        E alt) {
        return get(index).orElse(alt);
    }

    default E getOrElseGet(int index,
                           Supplier<? extends E> alt) {
        return get(index).orElseGet(requireNonNull(alt,
                                                   () -> "alt"));
    }

    default <R> SyncList<R> map(final Function<? super E, ? extends R> mapper) {
        return factory().map(this,
                             mapper);
    }

    default <R> SyncList<R> flatMap(final Function<? super E, ? extends SyncList<? extends R>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper);
    }

    @Override
    default Iterator<E> iterator() {
        return factory().iterator(this);
    }

    @Override
    default Spliterator<E> spliterator() {
        return factory().spliterator(this);
    }

    default Stream<E> stream() {
        return factory().stream(this);
    }

    default Optional<E> first() {
        return get(0);
    }

    default Optional<E> last() {
        return get(size() - 1);
    }

    default <T> T foldLeft(final T initialValue,
                           final BiFunction<T, ? super E, T> foldFunction) {
        return factory().foldLeft(this,
                                  initialValue,
                                  foldFunction);
    }

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
    static class SyncListFactory {

        @SafeVarargs
        @SuppressWarnings("varargs")
        public final <E> SyncList<E> ofConcurrent(final E... element) {
            return DefaultSyncList.of(new CopyOnWriteArrayList<>(element));
        }

        @SafeVarargs
        @SuppressWarnings("varargs")
        public final <E> SyncList<E> ofNonConcurrent(final E... element) {
            return DefaultSyncList.of(Arrays.asList(element));
        }

        public <E> SyncList<E> fromIterableConcurrent(final Iterable<? extends E> iterable) {
            if (iterable instanceof DefaultSyncList) {
                @SuppressWarnings("unchecked")
                final List<? extends E> baseList = ((DefaultSyncList<? extends E>) iterable).baseList;
                if (baseList instanceof CopyOnWriteArrayList) {
                    @SuppressWarnings("unchecked") final SyncList<E> syncList = (SyncList<E>) iterable;
                    return syncList;
                } else {
                    return DefaultSyncList.of(new CopyOnWriteArrayList<>(baseList));
                }
            } else if (iterable instanceof CopyOnWriteArrayList) {
                @SuppressWarnings("unchecked") final List<E> baseList = (List<E>) iterable;
                return DefaultSyncList.of(baseList);
            } else if (iterable instanceof Collection) {
                @SuppressWarnings("unchecked")
                final List<E> baseList = (List<E>) new CopyOnWriteArrayList<>(((Collection<? extends E>) iterable));
                return DefaultSyncList.<E>of(baseList);
            } else {
                @SuppressWarnings("unchecked")
                final List<E> baseList = (List<E>) new CopyOnWriteArrayList<>(StreamSupport.stream(requireNonNull(iterable,
                                                                                                                  () -> "iterable").spliterator(),
                                                                                                   false)
                                                                                           .toArray());
                return DefaultSyncList.<E>of(baseList);
            }
        }

        public <E> SyncList<E> fromIterableNonConcurrent(final Iterable<? extends E> iterable) {
            requireNonNull(iterable,
                           () -> "iterable");
            if (iterable instanceof DefaultSyncList) {
                @SuppressWarnings("unchecked")
                final List<? extends E> baseList = ((DefaultSyncList<? extends E>) iterable).baseList;
                if (baseList instanceof CopyOnWriteArrayList) {
                    return DefaultSyncList.of(new ArrayList<>(baseList));
                } else {
                    @SuppressWarnings("unchecked") final SyncList<E> syncList = (SyncList<E>) iterable;
                    return syncList;
                }
            } else if (iterable instanceof List && !(iterable instanceof CopyOnWriteArrayList)) {
                @SuppressWarnings("unchecked") final List<E> baseList = (List<E>) iterable;
                return DefaultSyncList.of(baseList);
            } else if (iterable instanceof Collection && !(iterable instanceof CopyOnWriteArrayList)) {
                @SuppressWarnings("unchecked") final List<E> baseList = new ArrayList<>((Collection<? extends E>) iterable);
                return DefaultSyncList.<E>of(baseList);
            } else if (iterable instanceof CopyOnWriteArrayList) {
                @SuppressWarnings("unchecked")
                final List<E> baseList = new ArrayList<>(((CopyOnWriteArrayList<? extends E>) iterable));
                return DefaultSyncList.<E>of(baseList);
            } else {
                final List<E> baseList = StreamSupport.stream(iterable.spliterator(),
                                                              false)
                                                      .collect(Collectors.toList());
                return DefaultSyncList.<E>of(baseList);
            }
        }

        public <E> SyncList<E> fromStreamConcurrent(final Stream<? extends E> stream) {
            @SuppressWarnings("unchecked") final E[] elements = (E[]) requireNonNull(stream,
                                                                                     () -> "stream").toArray();
            return ofConcurrent(elements);
        }

        public <E> SyncList<E> fromStreamNonConcurrent(final Stream<? extends E> stream) {
            @SuppressWarnings("unchecked") final E[] elements = (E[]) requireNonNull(stream,
                                                                                     () -> "stream").toArray();
            return ofNonConcurrent(elements);
        }

        public <E> SyncList<E> empty() {
            return DefaultSyncList.of(new ArrayList<>());
        }

        public <E> SyncList<E> emptyConcurrent() {
            return DefaultSyncList.of(new CopyOnWriteArrayList<>());
        }

        private static <E> DefaultSyncList<E> narrow(final SyncList<E> syncList) {
            return Optional.ofNullable(syncList)
                           .flatMap(sl -> {
                               if (sl instanceof DefaultSyncList) {
                                   return Optional.of((DefaultSyncList<E>) sl);
                               } else {
                                   return Optional.empty();
                               }
                           })
                           .orElseThrow(() -> new IllegalArgumentException(
                               SyncList.class.getSimpleName() + " is not an instance of "
                                   + DefaultSyncList.class.getSimpleName()));
        }

        public <E> SyncList<E> prepend(final SyncList<E> syncList,
                                       final E e) {
            return insertAt(syncList,
                            0,
                            e);
        }

        public <E> SyncList<E> prependAll(final SyncList<E> syncList,
                                          final Iterable<? extends E> list) {
            return insertAt(syncList,
                            0,
                            list);
        }

        public <E> SyncList<E> append(final SyncList<E> syncList,
                                      final E e) {
            return insertAt(syncList,
                            syncList.size() - 1,
                            e);
        }

        public <E> SyncList<E> appendAll(final SyncList<E> syncList,
                                         final Iterable<? extends E> list) {
            return insertAt(syncList,
                            syncList.size() - 1,
                            list);
        }

        public <E> SyncList<E> updateAt(final SyncList<E> syncList,
                                        final int i,
                                        final E e) {
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && i <= l.size() - 1) {
                    final List<E> updatedList = IntStream.range(0,
                                                                l.size())
                                                         .mapToObj(idx -> {
                                                             if (i == idx) {
                                                                 return e;
                                                             } else {
                                                                 return l.get(idx);
                                                             }
                                                         })
                                                         .collect(Collectors.toList());
                    if (l instanceof CopyOnWriteArrayList) {
                        return new CopyOnWriteArrayList<>(updatedList);
                    } else {
                        return updatedList;
                    }
                } else {
                    return l;
                }
            });
        }

        public <E> SyncList<E> insertAt(final SyncList<E> syncList,
                                        final int i,
                                        final E e) {
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && i <= l.size() - 1) {
                    l.add(i,
                          e);
                }
                return l;
            });
        }

        public <E> SyncList<E> insertAt(final SyncList<E> syncList,
                                        final int i,
                                        final Iterable<? extends E> iterable) {
            requireNonNull(iterable,
                           () -> "list");
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && iterable instanceof Collection<?>) {
                    @SuppressWarnings("unchecked") final boolean result = l.addAll(i,
                                                                                   (Collection<? extends E>) iterable);
                    return l;
                } else if (i >= 0) {
                    l.addAll(i,
                             StreamSupport.stream(iterable.spliterator(),
                                                  false)
                                          .collect(Collectors.toList()));
                    return l;
                } else {
                    return l;
                }
            });
        }

        public <E> SyncList<E> removeValue(final SyncList<E> syncList,
                                           final E e) {
            return narrow(syncList).mapInternal(l -> {
                l.remove(e);
                return l;
            });
        }

        public <E> SyncList<E> removeAt(final SyncList<E> syncList,
                                        final int i) {
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && i <= l.size() - 1) {
                    l.remove(i);
                    return l;
                } else {
                    return l;
                }
            });
        }

        public <E> int size(final SyncList<E> syncList) {
            final AtomicInteger sizeHolder = new AtomicInteger(0);
            narrow(syncList).mapInternal(l -> {
                sizeHolder.set(l.size());
                return l;
            });
            return sizeHolder.get();
        }

        public <E> Optional<E> get(final SyncList<E> syncList,
                                   final int index) {
            final AtomicReference<E> valueHolder = new AtomicReference<>(null);
            narrow(syncList).mapInternal(l -> {
                if (index >= 0 && index <= l.size() - 1) {
                    valueHolder.set(l.get(index));
                }
                return l;
            });
            return Optional.ofNullable(valueHolder.get());
        }

        public <E, R> SyncList<R> map(final SyncList<E> syncList,
                                      final Function<? super E, ? extends R> mapper) {
            requireNonNull(mapper,
                           () -> "mapper");
            return narrow(syncList).mapInternal(l -> {
                final List<R> updatedList = l.stream()
                                             .map(mapper)
                                             .collect(Collectors.toList());
                if (l instanceof CopyOnWriteArrayList) {
                    // Create standard arraylist first and create copy-on-write-array list in one call to its constructor rather than
                    // through multiple (hidden) calls to List.add within Stream.collect(Collectors.toCollection(CopyOnWriteArrayList
                    return new CopyOnWriteArrayList<>(updatedList);
                } else {
                    return updatedList;
                }
            });
        }

        public <E, R> SyncList<R> flatMap(final SyncList<E> syncList,
                                          final Function<? super E, ? extends SyncList<? extends R>> flatMapper) {
            requireNonNull(flatMapper,
                           () -> "flatMapper");
            return narrow(syncList).mapInternal(l -> {
                final List<R> updatedList = l.stream()
                                             .map(flatMapper)
                                             .flatMap(SyncList::stream)
                                             .collect(Collectors.toList());
                if (l instanceof CopyOnWriteArrayList) {
                    // Create standard arraylist first and create copy-on-write-array list in one call to its constructor rather than
                    // through multiple (hidden) calls to List.add within Stream.collect(Collectors.toCollection(CopyOnWriteArrayList::new)
                    return new CopyOnWriteArrayList<>(updatedList);
                } else {
                    return updatedList;
                }
            });
        }

        public <E> Iterator<E> iterator(final SyncList<E> syncList) {
            return Spliterators.iterator(spliterator(syncList));
        }

        public <E> Spliterator<E> spliterator(final SyncList<E> syncList) {
            final AtomicReference<Spliterator<E>> spliteratorHolder = new AtomicReference<>(Spliterators.emptySpliterator());
            narrow(syncList).mapInternal(l -> {
                spliteratorHolder.set(l.spliterator());
                return l;
            });
            return spliteratorHolder.get();
        }

        public <E> Stream<E> stream(final SyncList<E> syncList) {
            return StreamSupport.stream(spliterator(syncList),
                                        false);
        }

        public <E, T> T foldLeft(final SyncList<E> syncList,
                                 final T initialValue,
                                 final BiFunction<T, ? super E, T> foldFunction) {
            return stream(syncList).reduce(initialValue,
                                           requireNonNull(foldFunction,
                                                          () -> "foldFunction"),
                                           (t1, t2) -> t2);
        }
    }

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
    static class DefaultSyncList<E> implements SyncList<E> {

        private final List<E> baseList;

        <R> DefaultSyncList<R> mapInternal(final Function<? super List<E>, ? extends List<R>> mapper) {
            return DefaultSyncList.of(requireNonNull(mapper,
                                                     () -> "mapper").apply(baseList));
        }

    }

}
