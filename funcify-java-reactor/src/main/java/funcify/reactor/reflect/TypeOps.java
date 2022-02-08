package funcify.reactor.reflect;

import funcify.reactor.adt.Try;
import funcify.reactor.predicate.PredicateOps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author smccarron
 * @created 2021-08-05
 */
public interface TypeOps {

    static <T> Predicate<Object> instanceOfTypeCondition(final Class<T> type) {
        return o -> isInstanceOfType(o, type);
    }

    static <T> boolean isInstanceOfType(Object input,
                                        Class<T> type) {
        return input != null &&
               ((!type.isPrimitive() && type.isAssignableFrom(input.getClass())) || isInstanceOfPrimitiveType(input, type));
    }

    //    static <T> boolean isInstanceOfType(Object input,
    //                                        TypeRef<T> typeRef) {
    //        return input != null && Try.attemptNullable(typeRef::getType)
    //                                   .flatMap(Try::fromOptional)
    //                                   .attemptMap(ResolvableType::forType)
    //                                   .zip(Try.success(input),
    //                                        ResolvableType::isInstance)
    //                                   .orElse(Boolean.FALSE);
    //    }

    static <T> boolean isInstanceOfPrimitiveType(Object input,
                                                 Class<T> primitiveType) {
        return input != null && primitiveType != null && primitiveType.isPrimitive() &&
               boxedTypeForPrimitiveType(primitiveType).isInstance(input);
    }

    static Class<?> boxedTypeForPrimitiveType(Class<?> primitiveType) {
        if (primitiveType == null) {
            throw new IllegalArgumentException("primitive_type value is null");
        }
        if (!primitiveType.isPrimitive()) {
            throw new IllegalArgumentException("input type for primitive_type parameter is not primitive");
        }
        if (primitiveType == Integer.TYPE) {
            return Integer.class;
        } else if (primitiveType == Long.TYPE) {
            return Long.class;
        } else if (primitiveType == Boolean.TYPE) {
            return Boolean.class;
        } else if (primitiveType == Double.TYPE) {
            return Double.class;
        } else if (primitiveType == Float.TYPE) {
            return Float.class;
        } else if (primitiveType == Byte.TYPE) {
            return Byte.class;
        } else if (primitiveType == Short.TYPE) {
            return Short.class;
        } else if (primitiveType == Character.TYPE) {
            return Character.class;
        } else {
            throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
        }
    }

    static <T> Optional<T> toType(final Object input,
                                  final Class<T> type) {
        Objects.requireNonNull(type, () -> "type");
        return Optional.ofNullable(input)
                       .filter(instanceOfTypeCondition(type))
                       .flatMap(o -> Try.attempt(() -> type.cast(o)).getSuccess());
    }

    static <T> Function<Object, Optional<T>> toTypeFunction(final Class<T> type) {
        return input -> toType(input, type);
    }

    static <T> Optional<Iterable<T>> toIterableOfType(final Object input,
                                                      final Class<T> type) {
        Objects.requireNonNull(type, () -> "type");
        final Optional<Iterable<?>> rawIterableOpt = toType(input, Iterable.class).map(i -> (Iterable<?>) i);
        /*
         * if cannot be cast to iterable, then return empty optional
         */
        if (!rawIterableOpt.isPresent()) {
            return Optional.empty();
        }
        final Predicate<Object> typeTestCondition = instanceOfTypeCondition(type);
        final Function<Object, T> castFunction = type::cast;
        final Boolean hasAtLeastOneElement = rawIterableOpt.map(Iterable::spliterator)
                                                           .map(Spliterator::getExactSizeIfKnown)
                                                           .map(l -> l >= 1)
                                                           .orElseGet(() -> rawIterableOpt.map(Iterable::iterator)
                                                                                          .map(Iterator::hasNext)
                                                                                          .orElse(Boolean.FALSE));
        /*
         * Doesn't even have one element, sized or unsized, so return empty list
         */
        if (!hasAtLeastOneElement) {
            return Optional.of(Collections.emptyList());
            /*
             * Has an exact size so make sure converted iterable size matches original's size
             */
        } else if (toType(input, Iterable.class).map(i -> ((Iterable<?>) i))
                                                .map(Iterable::spliterator)
                                                .map(Spliterator::getExactSizeIfKnown)
                                                .orElse(-1L) > 1) {
            final Long exactSize = toType(input, Iterable.class).map(i -> ((Iterable<?>) i))
                                                                .map(Iterable::spliterator)
                                                                .map(Spliterator::getExactSizeIfKnown)
                                                                .get();
            final List<T> iterableAsList = toType(input,
                                                  Iterable.class).map(i -> StreamSupport.stream(((Iterable<?>) i).spliterator(),
                                                                                                false))
                                                                 .orElseGet(Stream::empty)
                                                                 .filter(typeTestCondition)
                                                                 .map(castFunction)
                                                                 .collect(Collectors.toList());
            final Function<List<T>, Iterable<T>> listToIterable = l -> l;
            return Optional.of(iterableAsList).filter(l -> l.size() == exactSize).map(listToIterable);
            /*
             * Doesn't have an exact size so type test and cast as the caller iterates through, some elements might be filtered out
             * if the element type specified is actually a subtype of the original element type before type erasure.
             *
             */
        } else {
            final Iterable<T> iterable = () -> {
                return toType(input, Iterable.class).map(i -> StreamSupport.stream(((Iterable<?>) i).spliterator(), false))
                                                    .orElseGet(Stream::empty)
                                                    .filter(typeTestCondition)
                                                    .map(castFunction)
                                                    .iterator();
            };
            return Optional.of(iterable);
        }
    }

    static <K, V> Optional<Map<K, V>> toMapOfType(final Object input,
                                                  final Class<K> keyType,
                                                  final Class<V> valueType) {
        if (!isInstanceOfType(input, Map.class)) {
            return Optional.empty();
        }
        if (Optional.of(input).map(Object::getClass).map(Class::getTypeParameters).map(tv -> tv.length).orElse(0) != 2) {
            return Optional.empty();
        }

        final Function<Object, Map<?, ?>> castFunction = Map.class::cast;
        final Function<Object, K> keyCast = keyType::cast;
        final Function<Object, V> valueCast = valueType::cast;
        final Optional<Map<?, ?>> rawMapOpt = toType(input, Map.class).map(castFunction);
        final int mapSize = rawMapOpt.map(Stream::of).orElseGet(Stream::empty).mapToInt(Map::size).sum();
        if (mapSize == 0) {
            return Optional.of(new HashMap<>());
        }
        return Optional.of(rawMapOpt.map(Map::entrySet)
                                    .map(Set::stream)
                                    .orElseGet(Stream::empty)
                                    .filter(e -> PredicateOps.bothOfType(keyType, valueType).test(e.getKey(), e.getValue()))
                                    .collect(Collectors.toMap(entry -> keyCast.apply(entry.getKey()),
                                                              entry -> valueCast.apply(entry.getValue()))))
                       .filter(m -> m.size() == mapSize);
    }

}
