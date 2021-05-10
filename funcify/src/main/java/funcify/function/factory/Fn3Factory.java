package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.ensemble.Quartet;
import funcify.function.Fn1;
import funcify.function.Fn1.Fn1W;
import funcify.function.Fn3;
import funcify.function.Fn3.Fn3W;
import funcify.tuple.Tuple3;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public class Fn3Factory {

    private static enum FactoryHolder {
        INSTANCE(new Fn3Factory());

        private final Fn3Factory fn3Factory;

        FactoryHolder(final Fn3Factory fn3Factory) {
            this.fn3Factory = fn3Factory;
        }

        public Fn3Factory getFn3Factory() {
            return fn3Factory;
        }
    }

    private Fn3Factory() {

    }

    public static Fn3Factory getInstance() {
        return FactoryHolder.INSTANCE.getFn3Factory();
    }

    public <A, B, C, D> Quartet<Fn3W, A, B, C, D> fromFunction(Fn3<A, B, C, D> function) {
        return requireNonNull(function,
                              () -> "function");
    }

    public <A, B, C, D, E> Quartet<Fn3W, A, B, C, E> flatMap(final Quartet<Fn3W, A, B, C, D> container,
                                                             final Function<? super D, ? extends Quartet<Fn3W, A, B, C, E>> flatMapper) {
        return fromFunction((A paramA, B paramB, C paramC) -> {
            return requireNonNull(flatMapper,
                                  () -> "flatMapper").apply(requireNonNull(container,
                                                                           () -> "container").convert(Fn3::narrowK)
                                                                                             .apply(paramA,
                                                                                                    paramB,
                                                                                                    paramC))
                                                     .convert(Fn3::narrowK)
                                                     .apply(paramA,
                                                            paramB,
                                                            paramC);
        });
    }

    public <A, B, C, D> Quartet<Fn3W, A, B, C, D> flatten(final Quartet<Fn3W, A, B, C, Quartet<Fn3W, A, B, C, D>> nestedFunction) {
        return flatMap(nestedFunction,
                       f -> f);
    }

    public <X, Y, Z, A, B, C, D> Quartet<Fn3W, X, Y, Z, D> compose(final Quartet<Fn3W, A, B, C, D> container,
                                                                   final Quartet<Fn3W, ? super X, ? super Y, ? super Z, ? extends Tuple3<A, B, C>> before) {
        return fromFunction((X paramX, Y paramY, Z paramZ) -> {
            return requireNonNull(before,
                                  () -> "before").convert(Fn3::narrowK)
                                                 .apply(paramX,
                                                        paramY,
                                                        paramZ)
                                                 .fold(requireNonNull(container,
                                                                      () -> "container").convert(Fn3::narrowK));
        });
    }

    public <X, Y, Z, A, B, C, D, E> Quartet<Fn3W, X, Y, Z, E> dimap(Quartet<Fn3W, A, B, C, D> container,
                                                                    Quartet<Fn3W, ? super X, ? super Y, ? super Z, ? extends Tuple3<A, B, C>> mapper1,
                                                                    Duet<Fn1W, ? super D, ? extends E> mapper2) {
        return fromFunction((X paramX, Y paramY, Z paramZ) -> {
            return requireNonNull(mapper1,
                                  () -> "mapper1").convert(Fn3::narrowK)
                                                  .apply(paramX,
                                                         paramY,
                                                         paramZ).<E>fold((A paramA, B paramB, C paramC) -> {
                    return requireNonNull(mapper2,
                                          () -> "mapper2").convert(Fn1::narrowK)
                                                          .apply(requireNonNull(container,
                                                                                () -> "container").convert(Fn3::narrowK)
                                                                                                  .apply(paramA,
                                                                                                         paramB,
                                                                                                         paramC));
                });
        });
    }


    @AllArgsConstructor
    private static class DefaultFn3<A, B, C, D> implements Fn3<A, B, C, D> {

        private final Fn3<A, B, C, D> capturedLambda;

        @Override
        public D apply(final A a,
                       final B b,
                       final C c) {
            return capturedLambda.apply(a,
                                        b,
                                        c);
        }
    }
}
