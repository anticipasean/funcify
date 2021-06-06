package funcify.tool.container;

import static funcify.tool.LiftOps.tryCatchLift;
import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import funcify.tool.LiftOps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-05-31
 */
public interface SyncList<E> extends Iterable<E> {

    @JsonCreator
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
                              .concurrentFromIterable(iterable);
    }

    static <E> SyncList<E> nonConcurrentFromIterable(final Iterable<? extends E> iterable) {
        return SyncListFactory.of()
                              .nonConcurrentFromIterable(iterable);
    }

    static <E> SyncList<E> fromStream(final Stream<? extends E> stream) {
        return SyncListFactory.of()
                              .concurrentFromStream(stream);
    }

    static <E> SyncList<E> nonConcurrentFromStream(final Stream<? extends E> stream) {
        return SyncListFactory.of()
                              .nonConcurrentFromStream(stream);
    }

    static <E> SyncList<E> empty() {
        return SyncListFactory.of()
                              .emptyConcurrent();
    }

    static <E> SyncList<E> emptyNonConcurrent() {
        return SyncListFactory.of()
                              .emptyNonConcurrent();
    }

    default SyncListFactory factory() {
        return SyncListFactory.of();
    }

    default SyncList<E> prepend(E e) {
        return factory().prepend(this,
                                 e);
    }

    default SyncList<E> prependAll(final Iterable<? extends E> list) {
        return factory().prependAll(this,
                                    list);
    }

    default SyncList<E> append(final E e) {
        return factory().append(this,
                                e);
    }

    default SyncList<E> appendAll(final Iterable<? extends E> list) {
        return factory().appendAll(this,
                                   list);
    }

    default SyncList<E> updateAt(final int index,
                                 final E e) {
        return factory().updateAt(this,
                                  index,
                                  e);
    }

    default SyncList<E> insertAt(final int index,
                                 final E e) {
        return factory().insertAt(this,
                                  index,
                                  e);
    }

    default SyncList<E> insertAt(int index,
                                 final Iterable<? extends E> list) {
        return factory().insertAt(this,
                                  index,
                                  list);
    }

    default SyncList<E> removeValue(final E e) {
        return factory().removeValue(this,
                                     e);
    }

    default SyncList<E> removeAt(final int i) {
        return factory().removeAt(this,
                                  i);
    }

    default int size() {
        return factory().size(this);
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    default Optional<E> get(final int index) {
        return factory().get(this,
                             index);
    }

    default E getOrElse(final int index,
                        final E alt) {
        return get(index).orElse(alt);
    }

    default E getOrElseGet(final int index,
                           final Supplier<? extends E> alt) {
        return get(index).orElseGet(requireNonNull(alt,
                                                   () -> "alt"));
    }

    default <R> SyncList<R> map(final Function<? super E, ? extends R> mapper) {
        return factory().map(this,
                             mapper);
    }

    default <R> SyncList<R> flatMap(final Function<? super E, ? extends SyncList<R>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper);
    }

    default SyncList<E> filter(final Predicate<? super E> condition) {
        return factory().filter(this,
                                condition);
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
        return isEmpty() ? Optional.empty() : get(0);
    }

    default Optional<E> firstMatch(final Predicate<? super E> condition) {
        return stream().filter(requireNonNull(condition,
                                              () -> "condition"))
                       .findFirst();
    }

    default Optional<E> last() {
        return isEmpty() ? Optional.empty() : get(size() - 1);
    }

    default <T> T foldLeft(final T initialValue,
                           final BiFunction<T, ? super E, T> foldFunction) {
        return factory().foldLeft(this,
                                  initialValue,
                                  foldFunction);
    }

    default String join(final String delimiter) {
        return factory().join(this,
                              delimiter,
                              "",
                              "");
    }

    default String join(final String delimiter,
                        final String prefix,
                        final String suffix) {
        return factory().join(this,
                              delimiter,
                              prefix,
                              suffix);
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

        public <E> SyncList<E> concurrentFromIterable(final Iterable<? extends E> iterable) {
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

        public <E> SyncList<E> nonConcurrentFromIterable(final Iterable<? extends E> iterable) {
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

        public <E> SyncList<E> concurrentFromStream(final Stream<? extends E> stream) {
            @SuppressWarnings("unchecked") final E[] elements = (E[]) requireNonNull(stream,
                                                                                     () -> "stream").toArray();
            return ofConcurrent(elements);
        }

        public <E> SyncList<E> nonConcurrentFromStream(final Stream<? extends E> stream) {
            @SuppressWarnings("unchecked") final E[] elements = (E[]) requireNonNull(stream,
                                                                                     () -> "stream").toArray();
            return ofNonConcurrent(elements);
        }

        public <E> SyncList<E> emptyNonConcurrent() {
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
                            Optional.of(size(syncList))
                                    .filter(i -> i > 0)
                                    .orElse(0),
                            e);
        }

        public <E> SyncList<E> appendAll(final SyncList<E> syncList,
                                         final Iterable<? extends E> list) {
            return insertAt(syncList,
                            Optional.of(size(syncList))
                                    .filter(i -> i > 0)
                                    .orElse(0),
                            list);
        }

        public <E> SyncList<E> updateAt(final SyncList<E> syncList,
                                        final int i,
                                        final E e) {
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && i <= l.size()) {
                    final List<E> updatedList = IntStream.range(0,
                                                                l.size())
                                                         .mapToObj(idx -> {
                                                             if (i == idx) {
                                                                 return e;
                                                             } else {
                                                                 return tryCatchLift(() -> l.get(idx)).orElse(e);
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
                if (i >= 0 && i <= l.size()) {
                    tryCatchLift(() -> l.add(i,
                                             e));
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
                    tryCatchLift(() -> {
                        @SuppressWarnings("unchecked") final boolean result = l.addAll(i,
                                                                                       (Collection<? extends E>) iterable);
                        return result;
                    });
                    return l;
                } else if (i >= 0) {
                    tryCatchLift(() -> l.addAll(i,
                                                StreamSupport.stream(iterable.spliterator(),
                                                                     false)
                                                             .collect(Collectors.toList())));
                    return l;
                } else {
                    return l;
                }
            });
        }

        public <E> SyncList<E> removeValue(final SyncList<E> syncList,
                                           final E e) {
            return narrow(syncList).mapInternal(l -> {
                tryCatchLift(() -> l.remove(e));
                return l;
            });
        }

        public <E> SyncList<E> removeAt(final SyncList<E> syncList,
                                        final int i) {
            return narrow(syncList).mapInternal(l -> {
                if (i >= 0 && i <= l.size()) {
                    tryCatchLift(() -> l.remove(i));
                }
                return l;
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
                if (index >= 0 && index <= l.size()) {
                    tryCatchLift(() -> valueHolder.set(l.get(index)));
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
                                             .map(tryCatchLift(mapper))
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
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
                                          final Function<? super E, ? extends SyncList<R>> flatMapper) {
            requireNonNull(flatMapper,
                           () -> "flatMapper");
            return narrow(syncList).mapInternal(l -> {
                final List<R> updatedList = l.stream()
                                             .map(tryCatchLift(flatMapper))
                                             .flatMap(opt -> opt.map(SyncList::stream)
                                                                .orElseGet(Stream::empty))
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

        @SuppressWarnings("unchecked")
        private static <A extends B, B, O extends Optional<A>> Optional<B> narrowOptional(final O opt) {
            return (Optional<B>) opt;
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
                                           (t, e) -> tryCatchLift(foldFunction).apply(t,
                                                                                      e)
                                                                               .orElse(t),
                                           (t1, t2) -> t2);
        }

        public <E> SyncList<E> filter(final SyncList<E> syncList,
                                      final Predicate<? super E> condition) {
            return narrow(syncList).mapInternal(l -> {
                final List<E> updatedList = l.stream()
                                             .filter(tryCatchLift(condition))
                                             .collect(Collectors.toList());
                if (l instanceof CopyOnWriteArrayList) {
                    return new CopyOnWriteArrayList<>(updatedList);
                }
                return updatedList;
            });
        }

        public <E> String join(final SyncList<E> syncList,
                               final String delimiter,
                               final String prefix,
                               final String suffix) {
            return stream(syncList).map(LiftOps.<E, String>tryCatchLift(Objects::toString))
                                   .map(opt -> opt.orElse(""))
                                   .collect(Collectors.joining(requireNonNull(delimiter,
                                                                              () -> "delimiter"),
                                                               requireNonNull(prefix,
                                                                              () -> "prefix"),
                                                               requireNonNull(suffix,
                                                                              () -> "suffix")));
        }
    }


    @AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
    @EqualsAndHashCode
    @ToString
    static class DefaultSyncList<E> implements SyncList<E> {

        @JsonValue
        private final List<E> baseList;

        <R> DefaultSyncList<R> mapInternal(final Function<? super List<E>, ? extends List<R>> mapper) {
            return DefaultSyncList.of(requireNonNull(mapper,
                                                     () -> "mapper").apply(baseList));
        }

    }

}
