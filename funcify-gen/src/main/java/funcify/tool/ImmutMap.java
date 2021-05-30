package funcify.tool;

import static java.util.Objects.requireNonNull;

import funcify.tool.ImmutMap.Tuple2;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-05-28
 */
public interface ImmutMap<K, V> extends Iterable<Tuple2<K, V>> {

    default ImmutMapFactory factory() {
        return ImmutMapFactory.of();
    }

    default ImmutMap<K, V> put(K key,
                               V value) {
        return factory().put(this,
                             key,
                             value);
    }

    default ImmutMap<K, V> put(Tuple2<? extends K, ? extends V> tuple) {
        return put(tuple._1(),
                   tuple._2());
    }

    default ImmutMap<K, V> putAll(ImmutMap<? extends K, ? extends V> map) {
        return factory().putAll(this,
                                map);
    }

    default ImmutMap<K, V> remove(K key) {
        return factory().remove(this,
                                key);
    }

    default ImmutMap<K, V> removeAllKeys(Iterable<? extends K> keys) {
        return factory().removeAllKeys(this,
                                       keys);
    }


    default Optional<V> get(K key) {
        return factory().get(this,
                             key);
    }

    default V getOrElse(K key,
                        V alt) {
        return factory().get(this,
                             key)
                        .orElse(alt);
    }

    default V getOrElseGet(K key,
                           Supplier<? extends V> alt) {
        return factory().get(this,
                             key)
                        .orElseGet(alt);
    }

    default int size() {
        return factory().size(this);
    }

    default boolean containsKey(K key) {
        return factory().containsKey(this,
                                     key);
    }

    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default Iterator<Tuple2<K, V>> iterator() {
        return factory().iterator(this);
    }

    default Stream<Tuple2<K, V>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(),
                                                             size(),
                                                             Spliterator.CONCURRENT & Spliterator.IMMUTABLE),
                                    false);
    }

    default <T> T foldLeft(final T initialValue,
                           final BiFunction<T, ? super Tuple2<K, V>, T> foldFunction) {
        return stream().reduce(requireNonNull(initialValue,
                                              () -> "initialValue"),
                               foldFunction,
                               (t1, t2) -> t2);
    }

    Map<K, V> toMutableMap();

    @AllArgsConstructor(access = AccessLevel.PUBLIC, staticName = "of")
    @EqualsAndHashCode
    @ToString
    class Tuple2<A, B> {

        private final A _1;
        private final B _2;

        public static <K, V> Tuple2<K, V> fromJavaMapEntry(final Entry<K, V> entry) {
            return Tuple2.of(entry.getKey(),
                             entry.getValue());
        }

        public A _1() {
            return _1;
        }

        public B _2() {
            return _2;
        }

        public <C> C fold(final BiFunction<? super A, ? super B, ? extends C> mapper) {
            return requireNonNull(mapper,
                                  () -> "mapper").apply(_1,
                                                        _2);
        }

        public Entry<A, B> toJavaMapEntry() {
            return new SimpleImmutableEntry<>(_1,
                                              _2);
        }

    }

    @AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
    class ImmutMapFactory {

        public <K, V> ImmutMap<K, V> empty() {
            return DefaultImmutMap.of(new ConcurrentHashMap<>());
        }

        private static <K, V> DefaultImmutMap<K, V> narrow(final ImmutMap<K, V> immutMap) {
            return Optional.ofNullable(immutMap)
                           .flatMap(im -> {
                               if (im instanceof DefaultImmutMap) {
                                   return Optional.of((DefaultImmutMap<K, V>) im);
                               } else {
                                   return Optional.empty();
                               }
                           })
                           .orElseThrow(() -> new IllegalArgumentException("immutMap is not instance of DefaultImmutMap"));
        }

        public <K, V> ImmutMap<K, V> put(final ImmutMap<K, V> immutMap,
                                         final K key,
                                         final V value) {
            return narrow(immutMap).mapInternal(m -> {
                m.compute(key,
                          (k, v) -> {
                              if (value == null) {
                                  return v;
                              } else {
                                  return value;
                              }
                          });
                return m;
            });
        }


        public <K, V> ImmutMap<K, V> putAll(final ImmutMap<K, V> immutMap,
                                            final ImmutMap<? extends K, ? extends V> map) {
            return requireNonNull(map,
                                  () -> "map").foldLeft(immutMap,
                                                        ImmutMap::put);
        }


        public <K, V> ImmutMap<K, V> remove(final ImmutMap<K, V> immutMap,
                                            final K key) {
            return narrow(immutMap).mapInternal(m -> {
                m.remove(key);
                return m;
            });
        }


        public <K, V> ImmutMap<K, V> removeAllKeys(final ImmutMap<K, V> immutMap,
                                                   final Iterable<? extends K> keys) {
            return narrow(immutMap).mapInternal(m -> {
                for (K k : keys) {
                    m.remove(k);
                }
                return m;
            });
        }


        public <K, V> Optional<V> get(final ImmutMap<K, V> immutMap,
                                      final K key) {
            final AtomicReference<V> valueHolder = new AtomicReference<>();
            narrow(immutMap).mapInternal(m -> {
                valueHolder.set(m.getOrDefault(key,
                                               null));
                return m;
            });
            return Optional.ofNullable(valueHolder.get());
        }


        public <K, V> int size(final ImmutMap<K, V> immutMap) {
            final AtomicInteger sizeHolder = new AtomicInteger(0);
            narrow(immutMap).mapInternal(m -> {
                sizeHolder.set(m.size());
                return m;
            });
            return sizeHolder.get();
        }


        public <K, V> boolean containsKey(final ImmutMap<K, V> immutMap,
                                          final K key) {
            final AtomicBoolean result = new AtomicBoolean();
            narrow(immutMap).mapInternal(m -> {
                result.set(m.containsKey(key));
                return m;
            });
            return result.get();
        }


        public <K, V> Iterator<Tuple2<K, V>> iterator(final ImmutMap<K, V> immutMap) {
            final AtomicReference<Iterator<Tuple2<K, V>>> iteratorHolder = new AtomicReference<>();
            narrow(immutMap).mapInternal(m -> {
                iteratorHolder.set(m.entrySet()
                                    .stream()
                                    .map(Tuple2::fromJavaMapEntry)
                                    .iterator());
                return m;
            });
            return iteratorHolder.get() != null ? iteratorHolder.get() : Collections.emptyIterator();
        }
    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
    @EqualsAndHashCode
    @ToString
    class DefaultImmutMap<K, V> implements ImmutMap<K, V> {

        private final ConcurrentMap<K, V> baseMap;

        @Override
        public Map<K, V> toMutableMap() {
            return baseMap;
        }

        DefaultImmutMap<K, V> mapInternal(final Function<? super ConcurrentMap<K, V>, ? extends ConcurrentMap<K, V>> mapper) {
            return DefaultImmutMap.of(requireNonNull(mapper,
                                                     () -> "mapper").apply(baseMap));
        }
    }

}
