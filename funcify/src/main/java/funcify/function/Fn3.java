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

    default Fn3Factory factory() {
        return Fn3Factory.getInstance();
    }

    D apply(A a,
            B b,
            C c);

    default <E> Fn3<A, B, C, E> flatMap(final Fn1<? super D, ? extends Fn3<A, B, C, E>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT4();
    }

    default Fn1<A, Fn1<B, Fn1<C, D>>> curry() {
        return Fn1.of((A a) -> (B b) -> (C c) -> apply(a,
                                                       b,
                                                       c));
    }

    @SuppressWarnings("unchecked")
    default <X, Y, Z, E> Fn3<X, Y, Z, E> dimap(final Fn3<? super X, ? super Y, ? super Z, ? extends Tuple3<A, B, C>> mapper1,
                                               final Fn1<? super D, ? extends E> mapper2) {
        return (Fn3<X, Y, Z, E>) factory().dimap(this,
                                                 mapper1,
                                                 mapper2);
    }

    default <E> Fn3<A, B, C, E> andThen(Fn1<? super D, ? extends E> after) {
        return (A a, B b, C c) -> requireNonNull(after,
                                                 () -> "after").apply(apply(a,
                                                                            b,
                                                                            c));
    }


}
