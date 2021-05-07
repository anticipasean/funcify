package funcify.function;

import funcify.ensemble.Solo;
import funcify.function.Fn0.Fn0W;
import funcify.function.factory.Fn0Factory;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface Fn0<A> extends Solo<Fn0W, A>, Supplier<A> {

    static enum Fn0W {

    }

    static <A> Fn0<A> narrowK(final Solo<Fn0W, A> solo) {
        return (Fn0<A>) solo;
    }

    static <A> Fn0<A> constant(final A value) {
        return Fn0Factory.getInstance()
                         .from(value)
                         .narrowT1();
    }

    static <A> Fn0<A> of(final Supplier<A> function) {
        return Fn0Factory.getInstance()
                         .fromFunction(function);
    }

    default Fn0Factory factory() {
        return Fn0Factory.getInstance();
    }

    default <B> Fn0<B> flatMap(final Function<? super A, ? extends Solo<Fn0W, B>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <B> Fn0<B> map(final Function<? super A, ? extends B> mapper) {
        return factory().map(this,
                             mapper)
                        .narrowT1();
    }

    default <B, C> Fn0<C> zip(final Solo<Fn0W, B> container,
                              final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return factory().zip(this,
                             container,
                             combiner)
                        .narrowT1();
    }

    @FunctionalInterface
    static interface ErrableFn0<A, T extends Throwable> {

        A get() throws T;

    }

}
