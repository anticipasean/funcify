package funcify.template.quartet.conjunct;


import static java.util.Objects.requireNonNull;

import funcify.design.trio.conjunct.ConjunctTrio;
import funcify.ensemble.Quartet;
import funcify.function.Fn3;
import funcify.function.Fn4;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface ConjunctQuartetTemplate<W> {

    <A, B, C, D> Quartet<W, A, B, C, D> from(A value1,
                                             B value2,
                                             C value3,
                                             D value4);

    <A, B, C, D, E> E fold(Quartet<W, A, B, C, D> container,
                           Fn4<? super A, ? super B, ? super C, ? super D, ? extends E> mapper);

    default <W2, A, B, C, D, T3 extends ConjunctTrio<W2, A, B, C>> T3 dropRight(Quartet<W, A, B, C, D> container,
                                                                                Fn3<? super A, ? super B, ? super C, ? extends T3> mapper) {
        return fold(container,
                    (a, b, c, d) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(a,
                                                                    b,
                                                                    c);
                    });
    }

    default <W2, A, B, C, D, T3 extends ConjunctTrio<W2, B, C, D>> T3 dropLeft(Quartet<W, A, B, C, D> container,
                                                                               Fn3<? super B, ? super C, ? super D, ? extends T3> mapper) {
        return fold(container,
                    (a, b, c, d) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(b,
                                                                    c,
                                                                    d);
                    });
    }

    //TODO: prepend and append when tuple4 ready

    default <A, B, C, D> Quartet<W, C, B, A, D> swapFirstThird(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(c,
                                         b,
                                         a,
                                         d));
    }

    default <A, B, C, D> Quartet<W, B, A, C, D> swapFirstSecond(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(b,
                                         a,
                                         c,
                                         d));
    }

    default <A, B, C, D> Quartet<W, A, C, B, D> swapSecondThird(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(a,
                                         c,
                                         b,
                                         d));
    }


    default <A, B, C, D> Quartet<W, D, B, C, A> swapFirstFourth(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(d,
                                         b,
                                         c,
                                         a));

    }

    default <A, B, C, D> Quartet<W, A, D, C, B> swapSecondFourth(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(a,
                                         d,
                                         c,
                                         b));

    }

    default <A, B, C, D> Quartet<W, A, B, D, C> swapThirdFourth(Quartet<W, A, B, C, D> container) {
        return fold(container,
                    (a, b, c, d) -> from(a,
                                         b,
                                         d,
                                         c));

    }



}
