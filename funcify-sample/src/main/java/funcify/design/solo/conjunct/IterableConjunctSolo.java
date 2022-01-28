package funcify.design.solo.conjunct;

import funcify.template.solo.conjunct.IterableConjunctSoloTemplate;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface IterableConjunctSolo<W, A> extends ConjunctSolo<W, A> {

    @Override
    IterableConjunctSoloTemplate<W> factory();

    default Iterator<A> iterator() {
        return factory().toIterator(this);
    }

    default <B> IterableConjunctSolo<W, B> fromIterable(Iterable<? extends B> iterable) {
        return factory().fromIterable(iterable)
                        .narrowT1();
    }
}
