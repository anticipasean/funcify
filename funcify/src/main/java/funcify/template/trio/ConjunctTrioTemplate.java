package funcify.template.trio;


import static java.util.Objects.requireNonNull;

import funcify.conjunct.ConjunctDuet;
import funcify.ensemble.Trio;
import funcify.function.Fn2;
import funcify.function.Fn3;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface ConjunctTrioTemplate<W> {

    <A, B, C> Trio<W, A, B, C> from(A value1,
                                    B value2,
                                    C value3);

    <A, B, C, D> D fold(Trio<W, A, B, C> container,
                        Fn3<? super A, ? super B, ? super C, ? extends D> mapper);

    default <W2, A, B, C, T2 extends ConjunctDuet<W2, A, B>> T2 dropRight(Trio<W, A, B, C> container,
                                                                          Fn2<? super A, ? super B, ? extends T2> mapper) {
        return fold(container,
                    (a, b, c) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(a,
                                                                    b);
                    });
    }

    default <W2, A, B, C, T2 extends ConjunctDuet<W2, B, C>> T2 dropLeft(Trio<W, A, B, C> container,
                                                                         Fn2<? super B, ? super C, ? extends T2> mapper) {
        return fold(container,
                    (a, b, c) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(b,
                                                                    c);
                    });
    }

    //TODO: prepend and append when tuple4 ready

    default <A, B, C> Trio<W, C, B, A> swapFirstThird(Trio<W, A, B, C> container) {
        return fold(container,
                    (a, b, c) -> from(c,
                                      b,
                                      a));
    }

    default <A, B, C> Trio<W, B, A, C> swapFirstSecond(Trio<W, A, B, C> container) {
        return fold(container,
                    (a, b, c) -> from(b,
                                      a,
                                      c));
    }

    default <A, B, C> Trio<W, A, C, B> swapSecondThird(Trio<W, A, B, C> container) {
        return fold(container,
                    (a, b, c) -> from(a,
                                      c,
                                      b));
    }

}
