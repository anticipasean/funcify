package funcify.design.trio.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.ensemble.Trio;
import funcify.function.Fn1;
import funcify.function.Fn3;
import funcify.tuple.Tuple1;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface ConjunctTrio<W, A, B, C> extends Trio<W, A, B, C> {

    <D> D fold(Fn3<? super A, ? super B, ? super C, ? extends D> mapper);

    default <WS> Solo<WS, A> first(final Fn1<? super A, ? extends Solo<WS, A>> mapper) {
        return fold(((a, b, c) -> requireNonNull(mapper,
                                                 () -> "mapper").apply(a)));
    }

    default Tuple1<A> first() {
        return first(Tuple1::of).narrowT1();
    }

    default <WS> Solo<WS, B> second(final Fn1<? super B, ? extends Solo<WS, B>> mapper) {
        return fold(((a, b, c) -> requireNonNull(mapper,
                                                 () -> "mapper").apply(b)));
    }

    default Tuple1<B> second() {
        return second(Tuple1::of).narrowT1();
    }

    default <WS> Solo<WS, C> third(final Fn1<? super C, ? extends Solo<WS, C>> mapper) {
        return fold(((a, b, c) -> requireNonNull(mapper,
                                                 () -> "mapper").apply(c)));
    }

    default Tuple1<C> third() {
        return third(Tuple1::of).narrowT1();
    }

}
