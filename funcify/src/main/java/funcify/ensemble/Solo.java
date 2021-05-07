package funcify.ensemble;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Simulated Higher Kinded Typing in Java
 * <br>
 * Solo => a Higher Kinded Type with the obligatory <b>W</b>itness type parameter and
 * <b>one</b> value type parameter
 *
 * @param <W> - Witness type parameter
 * @param <A> - Single value type parameter
 * @author smccarron
 * @created 2021-04-28
 */
public interface Solo<W, A> {

    default <R> R convert(Function<Solo<W, A>, R> converter) {
        return requireNonNull(converter,
                              () -> "converter").apply(this);
    }

    @SuppressWarnings("unchecked")
    static <W, B extends A, A> Solo<W, B> narrowP(Solo<W, A> wideSoloInstance) {
        return (Solo<W, B>) wideSoloInstance;
    }

    @SuppressWarnings("unchecked")
    static <W, A extends B, B> Solo<W, B> widenP(Solo<W, A> narrowSoloInstance) {
        return (Solo<W, B>) narrowSoloInstance;
    }

    @SuppressWarnings("unchecked")
    default <B extends Solo<W, ? super A>> B narrowT1() {
        return (B) this;
    }

}
