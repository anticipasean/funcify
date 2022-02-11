package funcify.template.solo.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.duet.conjunct.ConjunctDuet;
import funcify.ensemble.Solo;
import funcify.function.Fn2;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface ConjunctSoloTemplate<W> {

    <A> Solo<W, A> from(A value);

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> mapper);

    default <W2, A, B, T2 extends ConjunctDuet<W2, A, B>> T2 append(Solo<W, A> container,
                                                                    B value,
                                                                    Fn2<? super A, ? super B, ? extends T2> mapper) {
        return fold(container,
                    (A a) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(a,
                                                                    value)
                                                             .narrowT1();
                    });
    }

    default <W2, A, B, T2 extends ConjunctDuet<W2, B, A>> T2 prepend(Solo<W, A> container,
                                                                     B value,
                                                                     Fn2<? super B, ? super A, ? extends T2> mapper) {
        return fold(container,
                    (A a) -> {
                        return requireNonNull(mapper,
                                              () -> "mapper").apply(value,
                                                                    a)
                                                             .narrowT1();
                    });
    }


}
