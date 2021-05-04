package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-03
 */
public interface MappableDisjunctDuetTemplate<W> extends MappableDuetTemplate<W> {

    @Override
    default <A, B, C> Duet<W, A, C> map(Duet<W, A, B> container,
                                        Function<? super B, ? extends C> mapper) {
        return map2(container,
                    mapper);
    }

    <A, B, C> Duet<W, C, B> map1(Duet<W, A, B> container,
                                 Function<? super A, ? extends C> mapper);

    <A, B, C> Duet<W, A, C> map2(Duet<W, A, B> container,
                                 Function<? super B, ? extends C> mapper);

    <A, B, C, D> Duet<W, C, D> bimap(Duet<W, A, B> container,
                                     Function<? super A, ? extends C> mapper1,
                                     Function<? super B, ? extends D> mapper2);

}
