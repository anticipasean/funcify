package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Simulated Higher Kinded Typing in Java
 * <br>
 * Trio => a Higher Kinded Type with the obligatory <b>W</b>itness type parameter and
 * <b>three</b> value type parameters
 *
 * @param <W> - Witness type parameter
 * @param <A> - First value type parameter
 * @param <B> - Second value type parameter
 * @param <C> - Third value type parameter
 * @author smccarron
 * @created 2021-04-28
 */
public interface Trio<W, A, B, C> extends Solo<Solo<Solo<W, A>, B>, C> {

    @Override
    default <R> R convert(Function<Solo<Solo<Solo<W, A>, B>, C>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

    @SuppressWarnings("unchecked")
    static <W, A extends E, B extends F, C extends G, E, F, G> Trio<W, E, F, G> widenP(Trio<W, A, B, C> trio) {
        return (Trio<W, E, F, G>) trio;
    }

    @SuppressWarnings("unchecked")
    static <W, E extends A, F extends B, G extends C, A, B, C, D> Trio<W, E, F, G> narrowP(Trio<W, A, B, C> trio) {
        return (Trio<W, E, F, G>) trio;
    }

    @SuppressWarnings("unchecked")
    default <T extends Trio<W, ? extends A, ? extends B, ? super C>> T narrowT3() {
        return (T) this;
    }
}
