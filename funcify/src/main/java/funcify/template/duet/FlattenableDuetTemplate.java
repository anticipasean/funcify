package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableDuetTemplate<W> extends ZippableDuetTemplate<W> {

    <A, B, C> Duet<W, C, B> flatMapFirst(final Duet<W, A, B> container,
                                         final Function<? super A, ? extends Duet<W, C, B>> flatMapper);

    <A, B, C> Duet<W, A, C> flatMapSecond(final Duet<W, A, B> container,
                                          final Function<? super B, ? extends Duet<W, A, C>> flatMapper);

}
