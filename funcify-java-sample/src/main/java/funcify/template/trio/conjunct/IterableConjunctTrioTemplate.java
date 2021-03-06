package funcify.template.trio.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Trio;
import funcify.tuple.Tuple3;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface IterableConjunctTrioTemplate<W> extends ConjunctTrioTemplate<W> {

    default <A, B, C> Iterator<Tuple3<A, B, C>> toIterator(Trio<W, A, B, C> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a, B b, C c) -> Stream.of(Tuple3.of(a,
                                                           b,
                                                           c))
                                             .iterator());
    }

    default <A, B, C> Iterable<Tuple3<A, B, C>> toIterable(Trio<W, A, B, C> container) {
        return () -> toIterator(container);
    }

}
