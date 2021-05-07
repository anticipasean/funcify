package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface ConjunctDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    <A, B> Duet<W, A, B> both(A value1,
                              B value2);

    <A, B, C> C fold(Duet<W, A, B> container,
                     BiFunction<? super A, ? super B, ? extends C> mapper);

}
