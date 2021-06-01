package funcify.tool;

import static java.util.Objects.requireNonNull;

import funcify.tool.SyncMap.Tuple2;
import java.util.AbstractMap.SimpleImmutableEntry;
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
public interface SyncMap<K, V> extends Iterable<Tuple2<K, V>> {

    static <K, V> SyncMap<K, V> of(final K k,
                                   final V v) {
        return SyncMapFactory.of().<K, V>empty().put(k,
                                                     v);
    }

    static <K, V> SyncMap<K, V> of(final Tuple2<? extends K, ? extends V> tuple) {
        return SyncMapFactory.of().<K, V>empty().put(tuple);
    }

    static <K, V> SyncMap<K, V> fromMap(final Map<? extends K, ? extends V> javaMap) {
        return requireNonNull(javaMap,
                              () -> "javaMap").entrySet()
                                              .stream()
                                              .map(Tuple2::fromJavaMapEntry)
                                              .reduce(SyncMapFactory.of()
                                                                    .empty(),
                                                      SyncMap::put,
                                                      (m1, m2) -> m2);
    }

    static <T, K> SyncMap<K, T> fromIterable(final Iterable<? extends T> values,
                                             final Function<? super T, ? extends K> keyExtractor) {
        return fromIterable(values,
                            keyExtractor,
                            v -> v);
    }

    static <T, K, V> SyncMap<K, V> fromIterable(final Iterable<? extends T> values,
                                                final Function<? super T, ? extends K> keyExtractor,
                                                final Function<? super T, ? extends V> valueExtractor) {
        requireNonNull(keyExtractor,
                       () -> "keyExtractor");
        requireNonNull(valueExtractor,
                       () -> "valueExtractor");
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(requireNonNull(values,
                                                                                       () -> "values").iterator(),
                                                                        0),
                                    false)
                            .map(t -> Tuple2.of(keyExtractor.apply(t),
                                                valueExtractor.apply(t)))
                            .reduce(SyncMapFactory.of()
                                                  .empty(),
                                    SyncMap::put,
                                    (m1, m2) -> m2);
    }

    default SyncMapFactory factory() {
        return SyncMapFactory.of();
    }

    default SyncMap<K, V> put(K key,
                              V value) {
        return factory().put(this,
                             key,
                             value);
    }

    default SyncMap<K, V> put(Tuple2<? extends K, ? extends V> tuple) {
        return put(tuple._1(),
                   tuple._2());
    }

    default SyncMap<K, V> putAll(SyncMap<? extends K, ? extends V> map) {
        return factory().putAll(this,
                                map);
    }

    default SyncMap<K, V> remove(K key) {
        return factory().remove(this,
                                key);
    }

    default SyncMap<K, V> removeAllKeys(Iterable<? extends K> keys) {
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
        return factory().stream(this);
    }

    default Stream<K> keys() {
        return stream().map(Tuple2::_1);
    }

    default Stream<V> values() {
        return stream().map(Tuple2::_2);
    }

    default <T> T foldLeft(final T initialValue,
                           final BiFunction<T, ? super Tuple2<K, V>, T> foldFunction) {
        return stream().reduce(requireNonNull(initialValue,
                                              () -> "initialValue"),
                               foldFunction,
                               (t1, t2) -> t2);
    }

    default <R> SyncMap<K, R> map(final Function<? super V, ? extends R> mapper) {
        return factory().map(this,
                             mapper);
    }

    default <R> SyncMap<K, R> flatMap(final BiFunction<? super K, ? super V, ? extends SyncMap<? extends K, ? extends R>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper);
    }

    Map<K, V> asJavaMap();

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

        public <C> Tuple2<C, B> map1(final Function<? super A, ? extends C> mapper) {
            return Tuple2.of(requireNonNull(mapper,
                                            () -> "mapper").apply(_1()),
                             _2());
        }

        public <C> Tuple2<A, C> map2(final Function<? super B, ? extends C> mapper) {
            return Tuple2.of(_1(),
                             requireNonNull(mapper,
                                            () -> "mapper").apply(_2()));
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
    class SyncMapFactory {

        public <K, V> SyncMap<K, V> empty() {
            return DefaultSyncMap.of(new ConcurrentHashMap<>());
        }

        private static <K, V> DefaultSyncMap<K, V> narrow(final SyncMap<K, V> syncMap) {
            return Optional.ofNullable(syncMap)
                           .flatMap(im -> {
                               if (im instanceof SyncMap.DefaultSyncMap) {
                                   return Optional.of((DefaultSyncMap<K, V>) im);
                               } else {
                                   return Optional.empty();
                               }
                           })
                           .orElseThrow(() -> new IllegalArgumentException(
                               SyncMap.class.getSimpleName() + " is not instance of DefaultImmutMap"));
        }

        public <K, V> SyncMap<K, V> put(final SyncMap<K, V> syncMap,
                                        final K key,
                                        final V value) {
            return narrow(syncMap).mapInternal(m -> {
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


        public <K, V> SyncMap<K, V> putAll(final SyncMap<K, V> syncMap,
                                           final SyncMap<? extends K, ? extends V> map) {
            return requireNonNull(map,
                                  () -> "map").foldLeft(syncMap,
                                                        SyncMap::put);
        }


        public <K, V> SyncMap<K, V> remove(final SyncMap<K, V> syncMap,
                                           final K key) {
            return narrow(syncMap).mapInternal(m -> {
                m.remove(key);
                return m;
            });
        }


        public <K, V> SyncMap<K, V> removeAllKeys(final SyncMap<K, V> syncMap,
                                                  final Iterable<? extends K> keys) {
            return narrow(syncMap).mapInternal(m -> {
                for (K k : keys) {
                    m.remove(k);
                }
                return m;
            });
        }


        public <K, V> Optional<V> get(final SyncMap<K, V> syncMap,
                                      final K key) {
            final AtomicReference<V> valueHolder = new AtomicReference<>();
            narrow(syncMap).mapInternal(m -> {
                valueHolder.set(m.getOrDefault(key,
                                               null));
                return m;
            });
            return Optional.ofNullable(valueHolder.get());
        }


        public <K, V> int size(final SyncMap<K, V> syncMap) {
            final AtomicInteger sizeHolder = new AtomicInteger(0);
            narrow(syncMap).mapInternal(m -> {
                sizeHolder.set(m.size());
                return m;
            });
            return sizeHolder.get();
        }


        public <K, V> boolean containsKey(final SyncMap<K, V> syncMap,
                                          final K key) {
            final AtomicBoolean result = new AtomicBoolean();
            narrow(syncMap).mapInternal(m -> {
                result.set(m.containsKey(key));
                return m;
            });
            return result.get();
        }

        public <K, V> Spliterator<Tuple2<K, V>> spliterator(final SyncMap<K, V> syncMap) {
            final AtomicReference<Spliterator<Tuple2<K, V>>> spliteratorHolder = new AtomicReference<>();
            narrow(syncMap).mapInternal(m -> {
                spliteratorHolder.set(Spliterators.spliterator(m.entrySet()
                                                                .stream()
                                                                .map(Tuple2::fromJavaMapEntry)
                                                                .iterator(),
                                                               m.size(),
                                                               Spliterator.CONCURRENT & Spliterator.SIZED));
                return m;
            });
            return spliteratorHolder.get() != null ? spliteratorHolder.get() : Spliterators.emptySpliterator();
        }

        public <K, V> Stream<Tuple2<K, V>> stream(final SyncMap<K, V> syncMap) {
            return StreamSupport.stream(() -> spliterator(syncMap),
                                        Spliterator.CONCURRENT,
                                        false);
        }

        public <K, V> Iterator<Tuple2<K, V>> iterator(final SyncMap<K, V> syncMap) {
            return Spliterators.iterator(spliterator(syncMap));
        }

        public <K1, V1, K2, V2> SyncMap<K2, V2> flatMap(final SyncMap<K1, V1> syncMap,
                                                        final BiFunction<? super K1, ? super V1, ? extends SyncMap<? extends K2, ? extends V2>> flatMapper) {

            return stream(syncMap).map(t -> t.fold(requireNonNull(flatMapper,
                                                                  () -> "flatMapper")))
                                  .flatMap(SyncMap::stream)
                                  .reduce(empty(),
                                          SyncMap::put,
                                          (m1, m2) -> m2);

        }

        public <K, V, R> SyncMap<K, R> map(final SyncMap<K, V> syncMap,
                                           final Function<? super V, ? extends R> mapper) {

            return stream(syncMap).map(t -> t.map2(requireNonNull(mapper,
                                                                  () -> "mapper")))
                                  .reduce(empty(),
                                          SyncMap::put,
                                          (m1, m2) -> m2);

        }
    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
    @EqualsAndHashCode
    @ToString
    class DefaultSyncMap<K, V> implements SyncMap<K, V> {

        private final ConcurrentMap<K, V> baseMap;

        @Override
        public Map<K, V> asJavaMap() {
            return new ConcurrentHashMap<>(baseMap);
        }

        DefaultSyncMap<K, V> mapInternal(final Function<? super ConcurrentMap<K, V>, ? extends ConcurrentMap<K, V>> mapper) {
            return DefaultSyncMap.of(requireNonNull(mapper,
                                                    () -> "mapper").apply(baseMap));
        }
    }

}
