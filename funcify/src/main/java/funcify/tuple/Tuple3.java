package funcify.tuple;

import funcify.design.trio.FlattenableTrio;
import funcify.design.trio.conjunct.FlattenableConjunctTrio;
import funcify.function.Fn1;
import funcify.function.Fn3;
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

    @Override
    default <D, E, F> Tuple3<D, E, F> flatMap(final Fn3<? super A, ? super B, ? super C, ? extends FlattenableTrio<Tuple3W, D, E, F>> flatMapper) {
        return FlattenableConjunctTrio.super.flatMap(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <D> Tuple3<D, B, C> flatMapFirst(final Fn1<? super A, ? extends FlattenableTrio<Tuple3W, D, B, C>> flatMapper) {
        return FlattenableConjunctTrio.super.flatMapFirst(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <D> Tuple3<A, D, C> flatMapSecond(final Fn1<? super B, ? extends FlattenableTrio<Tuple3W, A, D, C>> flatMapper) {
        return FlattenableConjunctTrio.super.flatMapSecond(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <D> Tuple3<A, B, D> flatMapThird(final Fn1<? super C, ? extends FlattenableTrio<Tuple3W, A, B, D>> flatMapper) {
        return FlattenableConjunctTrio.super.flatMapThird(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <D> Tuple3<D, B, C> mapFirst(final Fn1<? super A, ? extends D> mapper) {
        return FlattenableConjunctTrio.super.<D>mapFirst(mapper).narrowT1();
    }

    @Override
    default <D> Tuple3<A, D, C> mapSecond(final Fn1<? super B, ? extends D> mapper) {
        return FlattenableConjunctTrio.super.<D>mapSecond(mapper).narrowT1();
    }

    @Override
    default <D> Tuple3<A, B, D> mapThird(final Fn1<? super C, ? extends D> mapper) {
        return FlattenableConjunctTrio.super.<D>mapThird(mapper).narrowT1();
    }
}
