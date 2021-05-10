package funcify.design.duet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.function.Fn1;
import funcify.template.duet.conjunct.ConjunctDuetTemplate;
import funcify.tuple.Tuple1;
import java.util.function.BiFunction;

/**
 * A Duet that can hold values for <b>both</b> type parameters, A and B, at the same time
 *
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctDuet<W, A, B> extends Duet<W, A, B> {

    ConjunctDuetTemplate<W> factory();

    <C> C fold(BiFunction<? super A, ? super B, ? extends C> mapper);

    default <WS> Solo<WS, A> first(final Fn1<? super A, ? extends Solo<WS, A>> mapper) {
        return fold(((a, b) -> requireNonNull(mapper,
                                              () -> "mapper").apply(a)));
    }

    default Tuple1<A> first() {
        return first(Tuple1::of).narrowT1();
    }

    default <WS> Solo<WS, B> second(final Fn1<? super B, ? extends Solo<WS, B>> mapper) {
        return fold(((a, b) -> requireNonNull(mapper,
                                              () -> "mapper").apply(b)));
    }

    default Tuple1<B> second() {
        return second(Tuple1::of).narrowT1();
    }
}
