package funcify.reactor.iterable;

import funcify.reactor.adt.Try;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author smccarron
 * @created 2021-08-17
 */
public interface IterableOps {

    static <I extends Iterable<T>, T> Stream<T> fromIterable(final I iterable) {
        if (iterable == null) {
            return Stream.empty();
        } else {
            return StreamSupport.stream(iterable.spliterator(),
                                        false);
        }
    }

    static <I extends Iterable<T>, T> Function<I, Stream<T>> iterableToStream() {
        return IterableOps::fromIterable;
    }

    static <I extends Iterable<T>, T> Function<I, Stream<T>> iterableToStream(final Class<T> elementType) {
        return IterableOps::fromIterable;
    }

    static <I extends Iterator<T>, T> Stream<T> fromIterator(final I iterator) {
        if (iterator == null) {
            return Stream.empty();
        } else {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,
                                                                            0),
                                        false);
        }
    }

    static <I extends Iterator<T>, T> Function<I, Stream<T>> iteratorToStream() {
        return IterableOps::fromIterator;
    }

    static <I extends Iterator<T>, T> Function<I, Stream<T>> iteratorToStream(final Class<T> elementType) {
        return IterableOps::fromIterator;
    }

    static <I extends Iterable<T>, T, S extends T> Optional<S> extractFirstOfSubtype(final I iterable,
                                                                                     final Class<S> subtype) {
        return fromIterable(iterable).filter(subtype::isInstance)
                                     .flatMap(Try.lift(subtype::cast)
                                                 .andThen(Try::stream))
                                     .findFirst();
    }

    static <I extends Iterable<T>, T, S extends T> Function<I, Optional<S>> firstOfSubtypeExtractor(final Class<S> subType) {
        return iterable -> IterableOps.extractFirstOfSubtype(iterable,
                                                             subType);
    }

    static <I extends Iterable<T>, T, S extends T> Iterable<S> extractAllOfSubtype(final I iterable,
                                                                                   final Class<S> subtype) {
        return fromIterable(iterable).filter(subtype::isInstance)
                                     .flatMap(Try.lift(subtype::cast)
                                                 .andThen(Try::stream))
                                     .collect(Collectors.toList());
    }

    static <I extends Iterable<T>, T> List<T> toList(final I iterable) {
        if (iterable instanceof List<?>) {
            @SuppressWarnings("unchecked")
            final List<T> list = (List<T>) iterable;
            return list;
        }
        return fromIterable(iterable).collect(Collectors.toList());
    }

    static <I extends Iterable<T>, T> Set<T> toSet(final I iterable) {
        if (iterable instanceof Set<?>) {
            @SuppressWarnings("unchecked")
            final Set<T> set = (Set<T>) iterable;
            return set;
        }
        return fromIterable(iterable).collect(Collectors.toSet());
    }

}
