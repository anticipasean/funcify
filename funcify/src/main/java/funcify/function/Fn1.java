package funcify.function;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.ensemble.FlattenableDuet;
import funcify.ensemble.FlattenableFunctionalDuet;
import funcify.ensemble.Solo;
import funcify.function.Fn1.Fn1W;
import funcify.function.factory.Fn1Factory;
import funcify.template.duet.FlattenableFunctionalTypeDuetTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Fn1<A, B> extends FlattenableFunctionalDuet<Fn1W, A, B>, Function<A, B> {

    static enum Fn1W {

    }

    static <T, R> Fn1<T, R> narrowK(Solo<Solo<Fn1W, T>, R> boxInstance) {
        return (Fn1<T, R>) boxInstance;
    }

    static <T, R> Fn1<T, R> narrowK(Duet<Fn1W, T, R> boxInstance) {
        return (Fn1<T, R>) boxInstance;
    }

    static <A, B> Fn1<A, B> of(Function<A, B> function) {
        return narrowK(new Fn1Factory().fromFunction(function));
    }

    @Override
    default FlattenableFunctionalTypeDuetTemplate<Fn1W> factory() {
        return new Fn1Factory();
    }

    @Override
    B apply(A parameter);

    @Override
    default <Z> Fn1<Z, B> compose(Function<? super Z, ? extends A> before) {
        return of((Z param) -> apply(requireNonNull(before,
                                                    () -> "before").apply(param)));
    }

    @Override
    default <V> Fn1<A, V> andThen(Function<? super B, ? extends V> after) {
        return of((A param) -> requireNonNull(after,
                                              () -> "after").apply(apply(param)));
    }

    @Override
    default <C> Fn1<A, C> map(final Function<? super B, ? extends C> mapper) {
        return narrowK(FlattenableFunctionalDuet.super.map(mapper));
    }

    @Override
    default <C> Fn1<A, C> zip(final FlattenableDuet<Fn1W, A, B> container2,
                              final BiFunction<? super B, ? super B, ? extends C> combiner) {
        return narrowK(FlattenableFunctionalDuet.super.zip(container2,
                                                           combiner));
    }

    @Override
    default <C> Fn1<A, C> flatMap(final Function<? super B, ? extends FlattenableFunctionalDuet<Fn1W, A, C>> flatMapper) {
        return narrowK(FlattenableFunctionalDuet.super.flatMap(flatMapper));
    }
}
