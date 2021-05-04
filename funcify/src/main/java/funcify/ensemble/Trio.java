package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Trio<W, A, B, C> extends Duet<Solo<W, A>, B, C> {

    @Override
    default <R> R convert(Function<Solo<Solo<Solo<W, A>, B>, C>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }
}
