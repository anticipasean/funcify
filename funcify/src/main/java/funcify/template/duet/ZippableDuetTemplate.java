package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface ZippableDuetTemplate<W> extends MappableDuetTemplate<W> {

    <A, B, C> Duet<W, A, C> zip(Duet<W, A, B> container1,
                                Duet<W, A, B> container2,
                                BiFunction<? super B, ? super B, ? extends C> zipper);


}
