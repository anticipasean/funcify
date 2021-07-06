package funcify.design.duet.conjunct;

import funcify.template.duet.conjunct.IterableConjunctDuetTemplate;
import funcify.tuple.Tuple2;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface IterableConjunctDuet<W, A, B> extends ConjunctDuet<W, A, B> {

    IterableConjunctDuetTemplate<W> factory();

    default Iterator<Tuple2<A, B>> iterator() {
        return factory().toIterator(this);
    }

    default Tuple2<A, B> fromIterables(final Iterable<? extends A> firstIterable,
                                       final Iterable<? extends B> secondIterable) {
        return factory().<Iterable<? extends A>, Iterable<? extends B>, A, B>fromIterables(firstIterable,
                                                                                           secondIterable).narrowT1();
    }

}
