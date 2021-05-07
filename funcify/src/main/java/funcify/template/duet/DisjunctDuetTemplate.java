package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface DisjunctDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    <A, B, C> C fold(Duet<W, A, B> container,
                     Function<? super A, ? extends C> ifFirstPresent,
                     Function<? super B, ? extends C> ifSecondPresent);

}
