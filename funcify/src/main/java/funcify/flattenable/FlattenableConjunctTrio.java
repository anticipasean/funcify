package funcify.flattenable;

import funcify.conjunct.ConjunctTrio;
import funcify.template.trio.FlattenableConjunctTrioTemplate;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface FlattenableConjunctTrio<W, A, B, C> extends ConjunctTrio<W, A, B, C> {

    FlattenableConjunctTrioTemplate<W> factory();



}
