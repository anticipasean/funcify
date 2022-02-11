package funcify.design.trio.disjunct;

import funcify.design.trio.FlattenableTrio;
import funcify.template.trio.disjunct.FlattenableDisjunctTrioTemplate;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface FlattenableDisjunctTrio<W, A, B, C> extends DisjunctTrio<W, A, B, C>, FilterableDisjunctTrio<W, A, B, C>,
                                                             FlattenableTrio<W, A, B, C> {

    @Override
    FlattenableDisjunctTrioTemplate<W> factory();



}
