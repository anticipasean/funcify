package funcify.function;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.ensemble.Trio;
import funcify.function.Fn2.Fn2W;
import funcify.function.factory.Fn2Factory;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-06
 */
@FunctionalInterface
public interface Fn2<A, B, C> extends Trio<Fn2W, A, B, C>, BiFunction<A, B, C> {

    static enum Fn2W {

    }

    static <A, B, C> Fn2<A, B, C> narrowK(final Trio<Fn2W, A, B, C> trioInstance) {
        return (Fn2<A, B, C>) trioInstance;
    }

    static <A, B, C> Fn2<A, B, C> narrowK(final Solo<Solo<Solo<Fn2W, A>, B>, C> soloInstance) {
        return (Fn2<A, B, C>) soloInstance;
    }

    static <A, B, C> Fn2<A, B, C> of(final BiFunction<A, B, C> biFunction) {
        if (biFunction instanceof Fn2) {
            return (Fn2<A, B, C>) biFunction;
        }
        return Fn2Factory.getInstance()
                         .fromFunction(biFunction);
    }

    static <A, B, C> Fn2<A, B, C> uncurry(final Fn1<A, Fn1<B, C>> curriedFunction) {
        return (A a, B b) -> requireNonNull(curriedFunction,
                                            () -> "curriedFunction").apply(a)
                                                                    .apply(b);
    }

    /**
     * <pre>
     * Fn2&lt;A, B, Fn2&lt;A, B, C&gt;&gt; --> Fn2&lt;A, B, C&gt;
     * </pre>
     *
     * @return
     */
    static <A, B, C, F extends Fn2<A, B, Fn2<A, B, C>>> Fn2<A, B, C> flatten(final F nestedFunc) {
        return Fn2Factory.getInstance()
                         .flatten(Trio.widenP(nestedFunc))
                         .narrowT3();
    }

    default Fn2Factory factory() {
        return Fn2Factory.getInstance();
    }

    @Override
    C apply(A a,
            B b);

    default Fn1<B, C> applyFirst(final A a) {
        return Fn1.of((B b) -> this.apply(a,
                                               b));
    }

    default Fn1<A, C> applySecond(final B b) {
        return Fn1.of((A paramA) -> this.apply(paramA,
                                               b));
    }

    default Fn1<A, Fn1<B, C>> curry() {
        return Fn1.of((A a) -> (B b) -> apply(a,
                                              b));
    }

    default Fn2<B, A, C> swapFirstSecond(){
        return Fn2.of((B b, A a) -> apply(a, b));
    }

    default <D> Fn2<A, B, D> map(final Function<? super C, ? extends D> mapper) {
        return factory().map(this,
                             mapper)
                        .narrowT3();
    }

    default <D> Fn2<A, B, D> flatMap(final Function<? super C, ? extends Fn2<A, B, D>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT3();
    }

    @Override
    default <D> Fn2<A, B, D> andThen(final Function<? super C, ? extends D> after) {
        return map(after);
    }

    default <Z> Fn2<Z, B, C> composeFirst(final Function<? super Z, ? extends A> mapper) {
        return factory().contraMapFirst(this,
                                        mapper)
                        .narrowT3();
    }

    default <Z> Fn2<A, Z, C> composeSecond(final Function<? super Z, ? extends B> mapper) {
        return factory().contraMapSecond(this,
                                         mapper)
                        .narrowT3();
    }

    default <Y, Z> Fn2<Y, Z, C> compose(final BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> before) {
        return factory().contraMap(this,
                                   before)
                        .narrowT3();
    }

    default <Y, Z, D> Fn2<Y, Z, D> dimap(BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> mapper1,
                                         Function<? super C, ? extends D> mapper2) {
        return factory().<Y, Z, A, B, C, D>dimap(this,
                                                 mapper1,
                                                 mapper2).narrowT1();
    }

    default <D> Fn2<A, B, D> ap(final Fn2<? super A, ? super C, ? extends Fn2<? super A, ? super B, ? extends D>> applicative) {
        return factory().ap(this,
                            applicative)
                        .narrowT1();
    }


}
