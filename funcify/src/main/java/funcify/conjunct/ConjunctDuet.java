package funcify.conjunct;

import java.util.function.BiFunction;

/**
 * A Duet that can hold values for <b>both</b> type parameters, A and B, at the same time
 *
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctDuet<W, A, B> {

    <C> C fold(BiFunction<? super A, ? super B, ? extends C> mapper);

}
