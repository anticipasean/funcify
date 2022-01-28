package funcify.template.duet;

import funcify.ensemble.Duet;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface ZippableDuetTemplate<W> extends MappableDuetTemplate<W> {

    <A, B, C, D> Duet<W, D, B> zipFirst(Duet<W, A, B> container1,
                                        Duet<W, C, B> container2,
                                        BiFunction<? super A, ? super C, ? extends D> combiner);

    default <A, B, C> Duet<W, Tuple2<A, C>, B> zipFirst(Duet<W, A, B> container1,
                                                        Duet<W, C, B> container2) {
        return zipFirst(container1,
                        container2,
                        Tuple2::of);
    }

    <A, B, C, D> Duet<W, A, D> zipSecond(Duet<W, A, B> container1,
                                         Duet<W, A, C> container2,
                                         BiFunction<? super B, ? super C, ? extends D> combiner);

    default <A, B, C, D> Duet<W, A, Tuple2<B, C>> zipSecond(Duet<W, A, B> container1,
                                                            Duet<W, A, C> container2) {
        return zipSecond(container1,
                         container2,
                         Tuple2::of);
    }

}
