package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.function.Fn1.Fn1W;
import funcify.template.duet.FlattenableFunctionalTypeDuetTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-29
 */
public class Fn1Factory implements FlattenableFunctionalTypeDuetTemplate<Fn1W> {


    @Override
    public <A, B> Duet<Fn1W, A, B> fromFunction(final Function<A, B> function) {
        return new DefaultFn1<>(requireNonNull(function,
                                               () -> "function"));
    }

    @Override
    public <A, B, C> Duet<Fn1W, A, C> flatMap(final Duet<Fn1W, A, B> container,
                                              final Function<? super B, ? extends Duet<Fn1W, A, C>> flatMapper) {
        return fromFunction((A paramA) -> requireNonNull(flatMapper,
                                                         () -> "flatMapper").apply(requireNonNull(container,
                                                                                                  () -> "container").convert(Fn1::narrowK)
                                                                                                                    .apply(paramA))
                                                                            .convert(Fn1::narrowK)
                                                                            .apply(paramA));
    }

    @Override
    public <A, B, C> Duet<Fn1W, A, C> map(final Duet<Fn1W, A, B> container,
                                          final Function<? super B, ? extends C> mapper) {
        return fromFunction(requireNonNull(container,
                                           () -> "container").convert(Fn1::narrowK)
                                                             .andThen(requireNonNull(mapper,
                                                                                     () -> "mapper")));
    }

    @Override
    public <A, B, C> Duet<Fn1W, A, C> zip(final Duet<Fn1W, A, B> container1,
                                          final Duet<Fn1W, A, B> container2,
                                          final BiFunction<? super B, ? super B, ? extends C> zipper) {
        return fromFunction((A paramA) -> {
            return zipper.apply(requireNonNull(container1,
                                               () -> "container1").convert(Fn1::narrowK)
                                                                  .apply(paramA),
                                requireNonNull(container2,
                                               () -> "container2").convert(Fn1::narrowK)
                                                                  .apply(paramA));
        });
    }

    private static class DefaultFn1<T, R> implements Fn1<T, R> {

        private final Function<T, R> function;

        private DefaultFn1(final Function<T, R> function) {
            this.function = function;
        }

        @Override
        public R apply(final T parameter) {
            return function.apply(parameter);
        }
    }
}
