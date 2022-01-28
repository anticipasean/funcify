package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface MappableDuetTemplate<W> {

    <A, B, C> Duet<W, C, B> mapFirst(Duet<W, A, B> container,
                                     Function<? super A, ? extends C> mapper);

    <A, B, C> Duet<W, A, C> mapSecond(Duet<W, A, B> container,
                                      Function<? super B, ? extends C> mapper);

    <A, B, C, D> Duet<W, C, D> bimap(Duet<W, A, B> container,
                                     Function<? super A, ? extends C> mapper1,
                                     Function<? super B, ? extends D> mapper2);
}
