package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 *
 * @author smccarron
 * @created 2021-04-28
 */
public interface Duet<W, A, B> extends Solo<Solo<W, A>, B> {

    @Override
    default <R> R convert(Function<Solo<Solo<W, A>, B>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

    @SuppressWarnings("unchecked")
    static <W, C extends A, D extends B, A, B> Duet<W, C, D> narrow(Duet<W, A, B> wideDuetInstance) {
        return (Duet<W, C, D>) wideDuetInstance;
    }

    @SuppressWarnings("unchecked")
    static <W, A extends C, B extends D, C, D> Duet<W, C, D> widen(Duet<W, A, B> narrowDuetInstance) {
        return (Duet<W, C, D>) narrowDuetInstance;
    }

}
