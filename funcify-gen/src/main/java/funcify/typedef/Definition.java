package funcify.typedef;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-26
 */
public interface Definition<D extends Definition<?>> {

    default D update(final Function<D, D> updater) {
        @SuppressWarnings("unchecked") final D definition = (D) this;
        return requireNonNull(updater,
                              () -> "updater").apply(definition);
    }

    default <T> D foldUpdate(final BiFunction<D, T, D> updaterFold,
                             final T input) {
        @SuppressWarnings("unchecked") final D definition = (D) this;
        return requireNonNull(updaterFold,
                              () -> "updater").apply(definition,
                                                     requireNonNull(input,
                                                                    () -> "input"));
    }

    default <R> R to(final Function<? super D, ? extends R> converter) {
        @SuppressWarnings("unchecked") final D definition = (D) this;
        return requireNonNull(converter,
                              () -> "converter").apply(definition);
    }

}
