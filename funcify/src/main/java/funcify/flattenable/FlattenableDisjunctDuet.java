package funcify.flattenable;

import funcify.disjunct.DisjunctDuet;
import funcify.template.duet.FlattenableDisjunctDuetTemplate;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface FlattenableDisjunctDuet<W, A, B> extends DisjunctDuet<W, A, B>, FlattenableDuet<W, A, B> {

    @Override
    FlattenableDisjunctDuetTemplate<W> factory();

}
