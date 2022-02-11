package funcify.template.duet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.solo.conjunct.ConjunctSolo;
import funcify.design.trio.conjunct.ConjunctTrio;
import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.function.Fn3;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface ConjunctDuetTemplate<W> {

    <A, B> Duet<W, A, B> from(A value1,
                              B value2);

    <A, B, C> C fold(Duet<W, A, B> container,
                     BiFunction<? super A, ? super B, ? extends C> mapper);

    default <W3, A, B, C, T3 extends ConjunctTrio<W3, A, B, C>> T3 append(Duet<W, A, B> container,
                                                                          C value,
                                                                          Fn3<? super A, ? super B, ? super C, ? extends T3> mapper) {
        return fold(container,
                    (A a, B b) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(a,
                                                                    b,
                                                                    value)
                                                             .narrowT1();
                    });
    }

    default <W3, A, B, C, T3 extends ConjunctTrio<W3, C, A, B>> T3 prepend(Duet<W, A, B> container,
                                                                           C value,
                                                                           Fn3<? super C, ? super A, ? super B, ? extends T3> mapper) {
        return fold(container,
                    (A a, B b) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(value,
                                                                    a,
                                                                    b)
                                                             .narrowT1();
                    });
    }

    default <W1, A, B, T1 extends ConjunctSolo<W1, A>> T1 dropRight(Duet<W, A, B> container,
                                                                    Fn1<? super A, ? extends T1> mapper) {
        return fold(container,
                    (A a, B b) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(a)
                                                             .narrowT1();
                    });
    }

    default <W1, A, B, T1 extends ConjunctSolo<W1, B>> T1 dropLeft(Duet<W, A, B> container,
                                                                   Fn1<? super B, ? extends T1> mapper) {
        return fold(container,
                    (A a, B b) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(b)
                                                             .narrowT1();
                    });
    }

    default <A, B> Duet<W, B, A> swap(Duet<W, A, B> container) {
        return fold(container,
                    (A a, B b) -> {
                        return from(b,
                                    a);
                    });
    }

}
