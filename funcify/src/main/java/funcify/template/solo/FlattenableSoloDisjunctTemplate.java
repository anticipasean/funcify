package funcify.template.solo;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-05-01
 */
public interface FlattenableSoloDisjunctTemplate<W> extends FlattenableSoloTemplate<W> {

    <B> Solo<W, B> empty();

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> ifPresent,
                  Supplier<B> ifAbsent);

    @Override
    default <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                                      final Function<? super A, ? extends Solo<W, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"),
                    this::empty);
    }
}
