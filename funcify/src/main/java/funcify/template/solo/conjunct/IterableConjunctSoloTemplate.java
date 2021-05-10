package funcify.template.solo.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableConjunctSoloTemplate<W> extends ConjunctSoloTemplate<W> {

    default <A> Iterator<A> toIterator(Solo<W, A> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A paramA) -> Collections.singletonList(paramA)
                                             .iterator());
    }

    default <A> Iterable<A> toIterable(Solo<W, A> container) {
        return () -> toIterator(container);
    }

}
