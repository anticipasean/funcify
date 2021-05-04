package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Solo<W, A> {

    default <R> R convert(Function<Solo<W, A>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

}
