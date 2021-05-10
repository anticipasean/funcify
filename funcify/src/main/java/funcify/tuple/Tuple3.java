package funcify.tuple;

import funcify.flattenable.FlattenableConjunctTrio;
import funcify.tuple.Tuple3.Tuple3W;
import funcify.tuple.factory.Tuple3Factory;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface Tuple3<A, B, C> extends FlattenableConjunctTrio<Tuple3W, A, B, C> {

    static enum Tuple3W {

    }

    static <A, B, C> Tuple3<A, B, C> of(final A param1,
                                        final B param2,
                                        final C param3) {
        return Tuple3Factory.getInstance()
                            .from(param1,
                                  param2,
                                  param3)
                            .narrowT1();
    }

    @Override
    default Tuple3Factory factory() {
        return Tuple3Factory.getInstance();
    }

    A _1();

    B _2();

    C _3();

    default Tuple1<A> first() {
        return Tuple1.of(_1());
    }

    default Tuple1<B> second() {
        return Tuple1.of(_2());
    }

    default Tuple1<C> third() {
        return Tuple1.of(_3());
    }

    default Tuple3<C, B, A> swapFirstThird() {
        return factory().swapFirstThird(this)
                        .narrowT1();
    }

    default Tuple3<B, A, C> swapFirstSecond() {
        return factory().swapFirstSecond(this)
                        .narrowT1();
    }

    default Tuple3<A, C, B> swapSecondThird() {
        return factory().swapSecondThird(this)
                        .narrowT1();
    }
}
