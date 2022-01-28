package funcify.design.duet.disjunct;

import funcify.template.duet.disjunct.IterableDisjunctDuetTemplate;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface IterableDisjunctDuet<W, A, B> extends DisjunctDuet<W, A, B> {

    IterableDisjunctDuetTemplate<W> factory();

    default <C, D> IterableDisjunctDuet<W, C, D> firstFromIterable(final Iterable<? extends C> iterable,
                                                                   final D defaultSecond) {
        return factory().<Iterable<? extends C>, C, D>firstFromIterable(iterable,
                                                                        defaultSecond).narrowT1();
    }

    default <C, D> IterableDisjunctDuet<W, C, D> secondFromIterable(final Iterable<? extends D> iterable,
                                                                    final C defaultFirst) {
        return factory().<Iterable<? extends D>, C, D>secondFromIterable(iterable,
                                                                         defaultFirst).narrowT1();
    }

    default Iterator<A> iteratorForFirst() {
        return factory().iteratorForFirst(this);
    }

    default Iterator<B> iteratorForSecond() {
        return factory().iteratorForSecond(this);
    }

}
