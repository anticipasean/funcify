package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableDisjunctDuetTemplate<W> extends FlattenableDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    <A, B, C> Duet<W, C, B> flatMap1(final Duet<W, A, B> container,
                                     final Function<? super A, ? extends Duet<W, C, B>> flatMapper);

    <A, B, C> Duet<W, A, C> flatMap2(final Duet<W, A, B> container,
                                     final Function<? super B, ? extends Duet<W, A, C>> flatMapper);

    default <A, B> Duet<W, A, B> flatten1(final Duet<W, Duet<W, A, B>, B> container) {
        return flatMap1(container,
                        b -> b);
    }

    default <A, B> Duet<W, A, B> flatten2(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap2(container,
                        b -> b);
    }

}
