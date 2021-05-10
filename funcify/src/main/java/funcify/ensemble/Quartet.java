package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Simulated Higher Kinded Typing in Java
 * <br>
 * Quartet => a Higher Kinded Type with the obligatory <b>W</b>itness type parameter and
 * <b>four</b> value type parameters
 *
 * @param <W> - Witness type parameter
 * @param <A> - First value type parameter
 * @param <B> - Second value type parameter
 * @param <C> - Third value type parameter
 * @param <D> - Fourth value type parameter
 * @author smccarron
 * @created 2021-04-28
 */
public interface Quartet<W, A, B, C, D> extends Solo<Solo<Solo<Solo<W, A>, B>, C>, D> {

    @Override
    default <R> R convert(Function<Solo<Solo<Solo<Solo<W, A>, B>, C>, D>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

    @SuppressWarnings("unchecked")
    static <W, A extends E, B extends F, C extends G, D extends H, E, F, G, H> Quartet<W, E, F, G, H> widenP(Quartet<W, A, B, C, D> quartet) {
        return (Quartet<W, E, F, G, H>) quartet;
    }

    @SuppressWarnings("unchecked")
    static <W, E extends A, F extends B, G extends C, H extends D, A, B, C, D> Quartet<W, E, F, G, H> narrowP(Quartet<W, A, B, C, D> quartet) {
        return (Quartet<W, E, F, G, H>) quartet;
    }

    @SuppressWarnings("unchecked")
    default <Q extends Quartet<W, ? super A, ? super B, ? super C, ? super D>> Q narrowT4() {
        return (Q) this;
    }


}
