package funcify.design.solo.disjunct;

import funcify.template.solo.disjunct.IterableDisjunctSoloTemplate;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface IterableDisjunctSolo<W, A> extends DisjunctSolo<W, A> {

    @Override
    IterableDisjunctSoloTemplate<W> factory();

    default Iterator<A> iterator() {
        return factory().toIterator(this);
    }

    default <B> IterableDisjunctSolo<W, B> fromIterable(Iterable<? extends B> iterable) {
        return factory().fromIterable(iterable)
                        .narrowT1();
    }
}
