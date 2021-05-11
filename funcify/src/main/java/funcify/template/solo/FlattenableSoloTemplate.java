package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSoloTemplate<W> extends ZippableSoloTemplate<W>, PeekableSoloTemplate<W> {

    <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                              final Function<? super A, ? extends Solo<W, B>> flatMapper);

    default <A> Solo<W, A> flatten(Solo<W, Solo<W, A>> container) {
        return flatMap(container,
                       b -> b);
    }

    @Override
    default <A> Solo<W, A> peek(final Solo<W, A> container,
                                final Consumer<? super A> consumer) {
        return flatMap(container,
                       (A a) -> {
                           Objects.requireNonNull(consumer,
                                                  () -> "consumer")
                                  .accept(a);
                           return container;
                       });
    }
}
