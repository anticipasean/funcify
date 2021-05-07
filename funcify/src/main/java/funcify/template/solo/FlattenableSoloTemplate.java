package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSoloTemplate<W> extends ZippableSoloTemplate<W> {

    <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                              final Function<? super A, ? extends Solo<W, B>> flatMapper);

    default <A> Solo<W, A> flatten(Solo<W, Solo<W, A>> container) {
        return flatMap(container,
                       b -> b);
    }
}
