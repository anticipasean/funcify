package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableFunctionalTypeDuetTemplate<W> extends FlattenableDuetTemplate<W> {

    <A, B> Duet<W, A, B> fromFunction(Function<A, B> function);

    <A, B, C> Duet<W, A, C> flatMap(final Duet<W, A, B> container,
                                    final Function<? super B, ? extends Duet<W, A, C>> flatMapper);

    default <A, B> Duet<W, A, B> flatten(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap(container,
                       b -> b);
    }

}
