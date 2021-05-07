package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.function.Fn1.Fn1W;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-29
 */
public class Fn1Factory {

    private static enum FactoryHolder {
        INSTANCE(new Fn1Factory());

        private final Fn1Factory fn1Factory;

        FactoryHolder(final Fn1Factory fn1Factory) {
            this.fn1Factory = fn1Factory;
        }

        public Fn1Factory getFn1Factory() {
            return fn1Factory;
        }
    }

    private Fn1Factory() {

    }

    public static Fn1Factory getInstance() {
        return FactoryHolder.INSTANCE.getFn1Factory();
    }

    public <A, B> Duet<Fn1W, A, B> fromFunction(final Function<A, B> function) {
        if (function instanceof Fn1) {
            return (Fn1<A, B>) function;
        }
        return new DefaultFn1<A, B>(requireNonNull(function,
                                                   () -> "function"));
    }

    public <A, B, C> Duet<Fn1W, A, C> flatMap(final Duet<Fn1W, A, B> container,
                                              final Function<? super B, ? extends Duet<Fn1W, A, C>> flatMapper) {
        return fromFunction((A paramA) -> requireNonNull(flatMapper,
                                                         () -> "flatMapper").apply(requireNonNull(container,
                                                                                                  () -> "container").convert(Fn1::narrowK)
                                                                                                                    .apply(paramA))
                                                                            .convert(Fn1::narrowK)
                                                                            .apply(paramA));
    }


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

    public <A, B, C> Duet<Fn1W, A, C> map(final Duet<Fn1W, A, B> container,
                                          final Function<? super B, ? extends C> mapper) {
        return fromFunction(requireNonNull(container,
                                           () -> "container").convert(Fn1::narrowK)
                                                             .andThen(requireNonNull(mapper,
                                                                                     () -> "mapper")));
    }

    public <Z, A, B, C> Duet<Fn1W, Z, C> dimap(final Duet<Fn1W, A, B> container,
                                               final Function<? super Z, ? extends A> mapper1,
                                               final Function<? super B, ? extends C> mapper2) {
        return fromFunction((Z paramZ) -> requireNonNull(container,
                                                         () -> "container").convert(Fn1::narrowK)
                                                                           .compose(requireNonNull(mapper1,
                                                                                                   () -> "mapper1"))
                                                                           .andThen(requireNonNull(mapper2,
                                                                                                   () -> "mapper2"))
                                                                           .apply(paramZ));
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
