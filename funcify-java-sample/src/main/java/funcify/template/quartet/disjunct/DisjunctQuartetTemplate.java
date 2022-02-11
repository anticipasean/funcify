package funcify.template.quartet.disjunct;

import funcify.ensemble.Quartet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-08
 */
public interface DisjunctQuartetTemplate<W> {

    <A, B, C, D> Quartet<W, A, B, C, D> first(A value1);

    <A, B, C, D> Quartet<W, A, B, C, D> second(B value2);

    <A, B, C, D> Quartet<W, A, B, C, D> third(C value3);

    <A, B, C, D> Quartet<W, A, B, C, D> fourth(D value4);

    <A, B, C, D, E> E fold(Quartet<W, A, B, C, D> container,
                           Function<? super A, ? extends E> ifFirst,
                           Function<? super B, ? extends E> ifSecond,
                           Function<? super C, ? extends E> ifThird,
                           Function<? super D, ? extends E> ifFourth);

}
