package funcify.template.duet;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.tuple.Tuple2;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableConjunctDuetTemplate<W> extends ConjunctDuetTemplate<W> {

    default <A, B> Iterator<Tuple2<A, B>> toIterator(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a, B b) -> Collections.singletonList(Tuple2.of(a,
                                                                      b))
                                             .iterator());
    }

    default <A, B> Iterable<Tuple2<A, B>> toIterable(Duet<W, A, B> container) {
        return () -> toIterator(container);
    }

}
