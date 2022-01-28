package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Simulated Higher Kinded Typing in Java
 * <br>
 * Duet => a Higher Kinded Type with the obligatory <b>W</b>itness type parameter and
 * <b>two</b> value type parameters
 *
 * @param <W> - Witness type parameter
 * @param <A> - First value type parameter
 * @param <B> - Second value type parameter
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
    static <W, C extends A, D extends B, A, B> Duet<W, C, D> narrowP(Duet<W, A, B> wideDuetInstance) {
        return (Duet<W, C, D>) wideDuetInstance;
    }

    @SuppressWarnings("unchecked")
    static <W, A extends C, B extends D, C, D> Duet<W, C, D> widenP(Duet<W, A, B> narrowDuetInstance) {
        return (Duet<W, C, D>) narrowDuetInstance;
    }

    @SuppressWarnings("unchecked")
    default <C extends Duet<W, ? super A, ? super B>> C narrowT2() {
        return (C) this;
    }


}
