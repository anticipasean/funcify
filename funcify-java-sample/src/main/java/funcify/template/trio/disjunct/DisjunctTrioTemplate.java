package funcify.template.trio.disjunct;

import funcify.ensemble.Trio;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-08
 */
public interface DisjunctTrioTemplate<W> {

    <A, B, C> Trio<W, A, B, C> first(A value1);

    <A, B, C> Trio<W, A, B, C> second(B value2);

    <A, B, C> Trio<W, A, B, C> third(C value3);

    <A, B, C, D> D fold(Trio<W, A, B, C> container,
                        Function<? super A, ? extends D> ifFirst,
                        Function<? super B, ? extends D> ifSecond,
                        Function<? super C, ? extends D> ifThird);

}
