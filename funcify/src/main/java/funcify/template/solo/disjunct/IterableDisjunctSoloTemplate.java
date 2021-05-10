package funcify.template.solo.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface IterableDisjunctSoloTemplate<W> extends DisjunctSoloTemplate<W> {

    default <A> Iterator<A> toIterator(Solo<W, A> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    a -> Collections.singletonList(a)
                                    .iterator(),
                    Collections::emptyIterator);
    }

    default <A> Iterable<A> toIterable(Solo<W, A> container) {
        return () -> toIterator(container);
    }

}
