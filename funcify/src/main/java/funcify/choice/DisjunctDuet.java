package funcify.choice;

import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface DisjunctDuet<W, A, B> {

    <C> C fold(Function<? super A, ? extends C> ifFirstPresent,
               Function<? super B, ? extends C> ifSecondPresent);

}
