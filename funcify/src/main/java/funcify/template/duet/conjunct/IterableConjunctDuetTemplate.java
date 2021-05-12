package funcify.template.duet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.tuple.Tuple2;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableConjunctDuetTemplate<W> extends ConjunctDuetTemplate<W> {

    default <A, B> Iterator<Tuple2<A, B>> toIterator(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a, B b) -> Stream.of(Tuple2.of(a,
                                                      b))
                                        .iterator());
    }

    default <A, B> Iterable<Tuple2<A, B>> toIterable(Duet<W, A, B> container) {
        return () -> toIterator(container);
    }

    default <IA extends Iterable<? extends A>, IB extends Iterable<? extends B>, A, B> Tuple2<A, B> fromIterables(final IA firstIterable,
                                                                                                                  final IB secondIterable) {
        if (firstIterable != null && secondIterable != null) {
            for (A currentA : firstIterable) {
                for (B currentB : secondIterable) {
                    return Tuple2.of(currentA,
                                     currentB);
                }
            }
        }
        throw new NoSuchElementException("unable to extract a first and/or second parameter value for creating a tuple from first and second iterables");
    }

}
