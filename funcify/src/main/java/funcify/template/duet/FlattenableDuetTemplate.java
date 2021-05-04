package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableDuetTemplate<W> extends ZippableDuetTemplate<W> {

    default <A, B> Duet<W, A, B> widenK(final Duet<W, ? extends A, ? extends B> container) {
        return Duet.widen(container);
    }

    default <A, B> Duet<W, A, B> narrowK(final Duet<W, ? super A, ? super B> container) {
        return Duet.narrow(container);
    }

    <A, B, C> Duet<W, A, C> flatMap(final Duet<W, A, B> container,
                                    final Function<? super B, ? extends Duet<W, A, C>> flatMapper);

    default <A, B> Duet<W, A, B> flatten(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap(container,
                       b -> b);
    }

}
