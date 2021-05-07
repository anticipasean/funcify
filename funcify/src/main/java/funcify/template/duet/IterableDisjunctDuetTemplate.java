package funcify.template.duet;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableDisjunctDuetTemplate<W> extends DisjunctDuetTemplate<W> {

    default <A, B> Iterator<A> firstIterator(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a) -> Collections.singletonList(a)
                                        .iterator(),
                    (B b) -> Collections.emptyIterator());
    }

    default <A, B> Iterator<B> secondIterator(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a) -> Collections.emptyIterator(),
                    (B b) -> Collections.singletonList(b)
                                        .iterator());
    }

    default <A, B> Iterable<A> firstIterable(Duet<W, A, B> container) {
        return () -> firstIterator(container);
    }

    default <A, B> Iterable<B> secondIterable(Duet<W, A, B> container) {
        return () -> secondIterator(container);
    }

}
