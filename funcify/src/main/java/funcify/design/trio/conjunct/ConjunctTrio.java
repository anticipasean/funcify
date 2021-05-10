package funcify.design.trio.conjunct;

import funcify.ensemble.Trio;
import funcify.function.Fn3;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface ConjunctTrio<W, A, B, C> extends Trio<W, A, B, C> {

    <D> D fold(Fn3<? super A, ? super B, ? super C, ? extends D> mapper);

}
