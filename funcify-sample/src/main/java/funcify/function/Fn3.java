package funcify.function;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Quartet;
import funcify.ensemble.Solo;
import funcify.function.Fn3.Fn3W;
import funcify.function.factory.Fn3Factory;
import funcify.tuple.Tuple3;

/**
 * @author smccarron
 * @created 2021-05-07
 */
@FunctionalInterface
public interface Fn3<A, B, C, D> extends Quartet<Fn3W, A, B, C, D> {

    static enum Fn3W {

    }

    static <A, B, C, D> Fn3<A, B, C, D> narrowK(Quartet<Fn3W, A, B, C, D> quartetInstance) {
        return (Fn3<A, B, C, D>) quartetInstance;
    }

    static <A, B, C, D> Fn3<A, B, C, D> narrowK(Solo<Solo<Solo<Solo<Fn3W, A>, B>, C>, D> quartetInstance) {
        return (Fn3<A, B, C, D>) quartetInstance;
    }

    static <A, B, C, D> Fn3<A, B, C, D> uncurry(final Fn1<A, Fn1<B, Fn1<C, D>>> curriedFunction) {
        return (A a, B b, C c) -> requireNonNull(curriedFunction,
                                                 () -> "curriedFunction").apply(a)
                                                                         .apply(b)
                                                                         .apply(c);
    }

    static <A, B, C, D, F extends Fn3<A, B, C, Fn3<A, B, C, D>>> Fn3<A, B, C, D> flatten(final F nestedFunc) {
        return Fn3Factory.getInstance()
                         .flatten(Quartet.widenP(nestedFunc))
                         .narrowT4();
    }

    static <A, B, C, D> Fn3<A, B, C, D> of(final Fn3<A, B, C, D> function) {
        return Fn3Factory.getInstance()
                         .fromFunction(function)
                         .narrowT1();
    }

    default Fn3Factory factory() {
        return Fn3Factory.getInstance();
    }

    D apply(A a,
            B b,
            C c);

    default <E> Fn3<A, B, C, E> flatMap(final Fn1<? super D, ? extends Fn3<? super A, ? super B, ? super C, ? extends E>> flatMapper) {
        return factory().<A, B, C, D, E>flatMap(this,
                                                flatMapper).narrowT1();
    }

    default Fn1<A, Fn1<B, Fn1<C, D>>> curry() {
        return Fn1.of((A a) -> (B b) -> (C c) -> apply(a,
                                                       b,
                                                       c));
    }

    default Fn2<B, C, D> applyFirst(final A a) {
        return Fn2.of((B b, C c) -> apply(a,
                                          b,
                                          c));
    }

    default Fn2<A, C, D> applySecond(final B b) {
        return Fn2.of((A a, C c) -> apply(a,
                                          b,
                                          c));
    }

    default Fn2<A, B, D> applyThird(final C c) {
        return Fn2.of((A a, B b) -> apply(a,
                                          b,
                                          c));
    }

    default Fn3<B, A, C, D> swapFirstSecond() {
        return Fn3.of((B b, A a, C c) -> apply(a,
                                               b,
                                               c));
    }

    default Fn3<A, C, B, D> swapSecondThird() {
        return Fn3.of((A a, C c, B b) -> apply(a,
                                               b,
                                               c));
    }

    default Fn3<C, B, A, D> swapFirstThird() {
        return Fn3.of((C c, B b, A a) -> apply(a,
                                               b,
                                               c));
    }

    default <X, Y, Z, E> Fn3<X, Y, Z, E> dimap(final Fn3<? super X, ? super Y, ? super Z, ? extends Tuple3<? extends A, ? extends B, ? extends C>> mapper1,
                                               final Fn1<? super D, ? extends E> mapper2) {
        return factory().<X, Y, Z, A, B, C, D, E>dimap(this,
                                                       mapper1,
                                                       mapper2).narrowT1();
    }

    default <X, Y, Z> Fn3<X, Y, Z, D> compose(final Fn3<? super X, ? super Y, ? super Z, ? extends Tuple3<? extends A, ? extends B, ? extends C>> before) {
        return factory().<X, Y, Z, A, B, C, D>compose(this,
                                                      before).narrowT1();
    }

    default <E> Fn3<A, B, C, E> andThen(final Fn1<? super D, ? extends E> after) {
        return factory().<A, B, C, D, E>map(this,
                                            after).narrowT1();
    }


}
