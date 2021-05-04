package funcify.template.solo;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-01
 */
public interface FlattenableSoloConjunctTemplate<W> extends FlattenableSoloTemplate<W> {

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> mapper);

    @Override
    default <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                                      final Function<? super A, ? extends Solo<W, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"));
    }
}
