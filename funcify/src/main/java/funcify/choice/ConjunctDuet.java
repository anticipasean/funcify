package funcify.choice;

import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctDuet<W, A, B> {

    <C> C fold(BiFunction<? super A, ? super B, ? extends C> mapper);

}
