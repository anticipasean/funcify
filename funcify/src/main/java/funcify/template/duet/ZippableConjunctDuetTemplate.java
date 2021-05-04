package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface ZippableConjunctDuetTemplate<W> extends ZippableDuetTemplate<W>, MappableConjunctDuetTemplate<W> {

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
