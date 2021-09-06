package funcify.tuple;

import funcify.ensemble.Quartet;
import funcify.ensemble.Solo;
import funcify.tuple.Tuple4.Tuple4W;
import funcify.tuple.factory.Tuple4Factory;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface Tuple4<A, B, C, D> extends Quartet<Tuple4W, A, B, C, D> {

    static enum Tuple4W {

    }

    static <A, B, C, D> Tuple4<A, B, C, D> narrowK(final Solo<Solo<Solo<Solo<Tuple4W, A>, B>, C>, D> soloInstance) {
        return (Tuple4<A, B, C, D>) soloInstance;
    }

    static <A, B, C, D> Tuple4<A, B, C, D> of(final A a,
                                              final B b,
                                              final C c,
                                              final D d) {
        return Tuple4Factory.getInstance()
                            .from(a,
                                  b,
                                  c,
                                  d)
                            .narrowT1();
    }

    A _1();

    B _2();

    C _3();

    D _4();

    default Tuple1<A> first() {
        return Tuple1.of(_1());
    }

    default Tuple1<B> second() {
        return Tuple1.of(_2());
    }

    default Tuple1<C> third() {
        return Tuple1.of(_3());
    }

    default Tuple1<D> fourth() {
        return Tuple1.of(_4());
    }

}
