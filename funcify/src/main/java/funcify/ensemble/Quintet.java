package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Simulated Higher Kinded Typing in Java
 * <br>
 * Quintet => a Higher Kinded Type with the obligatory <b>W</b>itness type parameter and
 * <b>four</b> value type parameters
 *
 * @param <W> - Witness type parameter
 * @param <A> - First value type parameter
 * @param <B> - Second value type parameter
 * @param <C> - Third value type parameter
 * @param <D> - Fourth value type parameter
 * @param <E> - Fourth value type parameter
 * @author smccarron
 * @created 2021-04-28
 */
public interface Quintet<W, A, B, C, D, E> extends Solo<Solo<Solo<Solo<Solo<W, A>, B>, C>, D>, E> {

    @Override
    default <R> R convert(Function<Solo<Solo<Solo<Solo<Solo<W, A>, B>, C>, D>, E>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

    @SuppressWarnings("unchecked")
    static <W, A extends E, B extends F, C extends G, D extends H, E extends I, F, G, H, I> Quintet<W, E, F, G, H, I> widenP(Quintet<W, A, B, C, D, E> quintet) {
        return (Quintet<W, E, F, G, H, I>) quintet;
    }

    @SuppressWarnings("unchecked")
    static <W, F extends A, G extends B, H extends C, I extends D, J extends E, A, B, C, D, E> Quintet<W, F, G, H, I, J> narrowP(Quintet<W, A, B, C, D, E> quintet) {
        return (Quintet<W, F, G, H, I, J>) quintet;
    }

    @SuppressWarnings("unchecked")
    default <Q extends Quintet<W, ? super A, ? super B, ? super C, ? super D, ? super E>> Q narrowT5() {
        return (Q) this;
    }


}
