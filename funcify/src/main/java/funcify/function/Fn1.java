package funcify.function;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.flattenable.FlattenableDuet;
import funcify.function.Fn1.Fn1W;
import funcify.function.factory.Fn1Factory;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
@FunctionalInterface
public interface Fn1<A, B> extends Duet<Fn1W, A, B>, Function<A, B> {

    static enum Fn1W {

    }

    static <T, R> Fn1<T, R> narrowK(Solo<Solo<Fn1W, T>, R> soloInstance) {
        return (Fn1<T, R>) soloInstance;
    }

    static <T, R> Fn1<T, R> narrowK(Duet<Fn1W, T, R> duetInstance) {
        return (Fn1<T, R>) duetInstance;
    }

    static <A, B> Fn1<A, B> of(Function<A, B> function) {
        if (function instanceof Fn1) {
            return (Fn1<A, B>) function;
        }
        return Fn1Factory.getInstance()
                         .fromFunction(function)
                         .narrowT1();
    }

    default Fn1Factory factory() {
        return Fn1Factory.getInstance();
    }

    @Override
    B apply(A parameter);

    @Override
    default <Z> Fn1<Z, B> compose(final Function<? super Z, ? extends A> before) {
        return factory().fromFunction((Z param) -> apply(requireNonNull(before,
                                                                        () -> "before").apply(param)))
                        .narrowT1();
    }

    @Override
    default <V> Fn1<A, V> andThen(final Function<? super B, ? extends V> after) {
        return factory().fromFunction((A param) -> requireNonNull(after,
                                                                  () -> "after").apply(apply(param)))
                        .narrowT1();
    }

    default <C> Fn1<A, C> map(final Function<? super B, ? extends C> mapper) {
        return factory().map(this,
                             mapper)
                        .narrowT1();
    }

    default <C> Fn1<A, C> zip(final Fn1<A, B> container2,
                              final BiFunction<? super B, ? super B, ? extends C> combiner) {
        return factory().zip(this,
                             container2,
                             combiner)
                        .narrowT1();
    }

    default <C> Fn1<A, C> flatMap(final Function<? super B, ? extends Duet<Fn1W, A, C>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <Z, C> Fn1<Z, C> dimap(final Function<? super Z, ? extends A> mapper1,
                                   final Function<? super B, ? extends C> mapper2) {
        return factory().<Z, A, B, C>dimap(this,
                                           mapper1,
                                           mapper2).narrowT1();
    }

    @FunctionalInterface
    static interface ErrableFn1<A, B, T extends Throwable> {

        B apply(A parameter) throws T;

    }
}
