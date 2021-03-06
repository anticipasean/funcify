package funcify.template.solo.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface IterableDisjunctSoloTemplate<W> extends DisjunctSoloTemplate<W> {

    default <I extends Iterable<? extends A>, A> Solo<W, A> fromIterable(final I iterable) {
        if (iterable != null) {
            for (final A current : iterable) {
                return from(current);
            }
        }
        return empty();
    }

    default <A> Iterator<A> toIterator(Solo<W, A> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    a -> Stream.of(a)
                               .iterator(),
                    Stream.<A>empty()::iterator);
    }

    default <A> Iterable<A> toIterable(Solo<W, A> container) {
        return () -> toIterator(container);
    }

}
