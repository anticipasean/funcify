package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface ZippableDuetTemplate<W> extends MappableDuetTemplate<W> {

    <A, B, C, D> Duet<W, D, B> zipFirst(Duet<W, A, B> container1,
                                        Duet<W, C, B> container2,
                                        BiFunction<? super A, ? super C, ? extends D> combiner);

    <A, B, C, D> Duet<W, A, D> zipSecond(Duet<W, A, B> container1,
                                         Duet<W, A, C> container2,
                                         BiFunction<? super B, ? super C, ? extends D> combiner);


}
