package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-05-03
 */
public interface ZippableDisjunctDuetTemplate<W> extends ZippableDuetTemplate<W>, MappableDisjunctDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    @Override
    default <A, B, C> Duet<W, A, C> zip(Duet<W, A, B> container1,
                                        Duet<W, A, B> container2,
                                        BiFunction<? super B, ? super B, ? extends C> zipper) {
        return zipSecond(container1,
                         container2,
                         zipper);
    }

    <A, B, C> Duet<W, C, B> zipFirst(Duet<W, A, B> container1,
                                     Duet<W, A, B> container2,
                                     BiFunction<? super A, ? super A, ? extends C> combiner);

    <A, B, C> Duet<W, A, C> zipSecond(Duet<W, A, B> container1,
                                      Duet<W, A, B> container2,
                                      BiFunction<? super B, ? super B, ? extends C> combiner);
}
