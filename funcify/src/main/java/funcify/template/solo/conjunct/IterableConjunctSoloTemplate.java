package funcify.template.solo.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableConjunctSoloTemplate<W> extends ConjunctSoloTemplate<W> {

    default <A> Iterator<A> toIterator(Solo<W, A> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A paramA) -> Stream.of(paramA)
                                        .iterator());
    }

    default <A> Iterable<A> toIterable(Solo<W, A> container) {
        return () -> toIterator(container);
    }

    default <I extends Iterable<? extends A>, A> Solo<W, A> fromIterable(I iterable) {
        if (iterable != null) {
            for (A current : iterable) {
                return from(current);
            }
        }
        throw new NoSuchElementException("iterable was empty; no conjunct solo could be created");
    }

}
