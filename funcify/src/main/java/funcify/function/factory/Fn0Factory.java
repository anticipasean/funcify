package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.function.Fn0;
import funcify.function.Fn0.Fn0W;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public class Fn0Factory {

    private static enum FactoryHolder {
        INSTANCE(new Fn0Factory());

        private final Fn0Factory fn0Factory;

        FactoryHolder(final Fn0Factory fn0Factory) {
            this.fn0Factory = fn0Factory;
        }

        public Fn0Factory getFn0Factory() {
            return fn0Factory;
        }
    }

    private Fn0Factory() {

    }

    public static Fn0Factory getInstance() {
        return FactoryHolder.INSTANCE.getFn0Factory();
    }

    public <B> Solo<Fn0W, B> from(final B value) {
        return fromFunction(() -> value);
    }

    public <A> Fn0<A> fromFunction(Supplier<A> function) {
        if (function instanceof Fn0) {
            return (Fn0<A>) function;
        }
        return new DefaultFn0<>(requireNonNull(function,
                                               () -> "function"));
    }


    public <A, B> Solo<Fn0W, B> flatMap(Solo<Fn0W, A> container,
                                        Function<? super A, ? extends Solo<Fn0W, B>> flatMapper) {
        return fromFunction(() -> requireNonNull(flatMapper,
                                                 () -> "flatMapper").apply(requireNonNull(container,
                                                                                          () -> "container").convert(Fn0::narrowK)
                                                                                                            .get())
                                                                    .convert(Fn0::narrowK)
                                                                    .get());
    }


    public <A, B> Solo<Fn0W, B> map(Solo<Fn0W, A> container,
                                    Function<? super A, ? extends B> mapper) {
        return fromFunction(() -> requireNonNull(mapper,
                                                 () -> "mapper").apply(requireNonNull(container,
                                                                                      () -> "container").convert(Fn0::narrowK)
                                                                                                        .get()));
    }


    public <A, B, F extends Function<? super A, ? extends B>> Solo<Fn0W, B> ap(final Solo<Fn0W, A> container,
                                                                               final Solo<Fn0W, F> containerWithFunction) {
        return fromFunction(() -> requireNonNull(containerWithFunction,
                                                 () -> "containerWithFunction").convert(Fn0::narrowK)
                                                                               .get()
                                                                               .apply(requireNonNull(container,
                                                                                                     () -> "container").convert(Fn0::narrowK)
                                                                                                                       .get()));
    }


    public <A, B, C> Solo<Fn0W, C> zip(Solo<Fn0W, A> container1,
                                       Solo<Fn0W, B> container2,
                                       BiFunction<? super A, ? super B, ? extends C> mapper) {
        return flatMap(container1,
                       (A paramA) -> {
                           return flatMap(container2,
                                          (B paramB) -> {
                                              return fromFunction(() -> requireNonNull(mapper,
                                                                                       () -> "mapper").apply(paramA,
                                                                                                             paramB));
                                          });
                       });
    }


    @AllArgsConstructor
    private static class DefaultFn0<A> implements Fn0<A> {

        private final Supplier<A> function;

        @Override
        public A get() {
            return function.get();
        }
    }

}
