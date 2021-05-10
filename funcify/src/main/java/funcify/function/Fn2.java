package funcify.function;

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

    default Fn2Factory factory() {
        return Fn2Factory.getInstance();
    }

    @Override
    C apply(A a,
            B b);

    default Fn1<B, C> applyFirst(final A firstParameter) {
        return Fn1.of((B paramB) -> this.apply(firstParameter,
                                               paramB));
    }

    default Fn1<A, C> applySecond(final B secondParameter) {
        return Fn1.of((A paramA) -> this.apply(paramA,
                                               secondParameter));
    }

    default <D> Fn2<A, B, D> map(final Function<? super C, ? extends D> mapper) {
        return factory().map(this,
                             mapper);
    }

    default <D> Fn2<A, B, D> flatMap(final Function<? super C, ? extends Fn2<A, B, D>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper);
    }

    /**
     * <pre>
     * Fn2&lt;A, B, Fn2&lt;A, B, C&gt;&gt; --> Fn2&lt;A, B, C&gt;
     * </pre>
     *
     * @return
     */
    static <A, B, C, F1 extends Trio<Fn2W, A, B, C>, F2 extends Trio<Fn2W, A, B, F1>> Fn2<A, B, C> flatten(final F2 nestedFunc) {
        return Fn2Factory.getInstance()
                         .flatten(Fn2Factory.getInstance()
                                            .fromFunction((A paramA, B paramB) -> ((Fn2<A, B, F1>) Fn2.narrowK(nestedFunc)).apply(paramA,
                                                                                                                                  paramB)));
    }

    @Override
    default <D> Fn2<A, B, D> andThen(final Function<? super C, ? extends D> after) {
        return map(after);
    }

    default <Z> Fn2<Z, B, C> composeFirst(final Function<? super Z, ? extends A> mapper) {
        return factory().contraMapFirst(this,
                                        mapper);
    }

    default <Z> Fn2<A, Z, C> composeSecond(final Function<? super Z, ? extends B> mapper) {
        return factory().contraMapSecond(this,
                                         mapper);
    }

    default <Y, Z> Fn2<Y, Z, C> compose(final BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> before) {
        return factory().contraMap(this,
                                   before);
    }

    default <Y, Z, D> Fn2<Y, Z, D> dimap(BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> mapper1,
                                         Function<? super C, ? extends D> mapper2) {
        return factory().dimap(this,
                               mapper1,
                               mapper2);
    }


}
