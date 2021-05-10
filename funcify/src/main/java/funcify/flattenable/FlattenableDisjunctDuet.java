package funcify.flattenable;

import funcify.disjunct.DisjunctDuet;
import funcify.ensemble.Duet;
import funcify.template.duet.FlattenableDisjunctDuetTemplate;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface FlattenableDisjunctDuet<W, A, B> extends DisjunctDuet<W, A, B>, FlattenableDuet<W, A, B> {

    @Override
    FlattenableDisjunctDuetTemplate<W> factory();

    @Override
    default Duet<W, A, B> first(final A value) {
        return factory().first(value);
    }

    @Override
    default Duet<W, A, B> second(final B value) {
        return factory().second(value);
    }

//    default
}
