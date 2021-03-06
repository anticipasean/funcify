package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Quintet;
import funcify.function.Fn1;
import funcify.function.Fn4;
import funcify.function.Fn4.Fn4W;
import funcify.tuple.Tuple4;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public class Fn4Factory {

    private static enum FactoryHolder {
        INSTANCE(new Fn4Factory());

        private final Fn4Factory fn4Factory;

        FactoryHolder(final Fn4Factory fn4Factory) {
            this.fn4Factory = fn4Factory;
        }

        public Fn4Factory getFn4Factory() {
            return fn4Factory;
        }
    }

    public Fn4Factory() {

    }

    public static Fn4Factory getInstance() {
        return FactoryHolder.INSTANCE.getFn4Factory();
    }

    public <A, B, C, D, E> Quintet<Fn4W, A, B, C, D, E> fromFunction(Fn4<A, B, C, D, E> function) {
        return requireNonNull(function,
                              () -> "function");
    }

    public <A, B, C, D, E, F> Quintet<Fn4W, A, B, C, D, F> flatMap(final Quintet<Fn4W, A, B, C, D, E> container,
                                                                   final Fn1<? super E, ? extends Quintet<Fn4W, ? super A, ? super B, ? super C, ? super D, ? extends F>> flatMapper) {
        return fromFunction((A paramA, B paramB, C paramC, D paramD) -> {
            return requireNonNull(flatMapper,
                                  () -> "flatMapper").convert(Fn1::narrowK)
                                                     .apply(requireNonNull(container,
                                                                           () -> "container").convert(Fn4::narrowK)
                                                                                             .apply(paramA,
                                                                                                    paramB,
                                                                                                    paramC,
                                                                                                    paramD))
                                                     .convert(Fn4::narrowK)
                                                     .apply(paramA,
                                                            paramB,
                                                            paramC,
                                                            paramD);
        });
    }

    public <A, B, C, D, E> Quintet<Fn4W, A, B, C, D, E> flatten(final Quintet<Fn4W, A, B, C, D, Quintet<Fn4W, A, B, C, D, E>> nestedFunction) {
        return flatMap(nestedFunction,
                       f -> f);
    }

    public <W1, X, Y, Z, A, B, C, D, E> Quintet<Fn4W, W1, X, Y, Z, E> compose(final Quintet<Fn4W, A, B, C, D, E> container,
                                                                              final Fn4<? super W1, ? super X, ? super Y, ? super Z, ? extends Tuple4<? extends A, ? extends B, ? extends C, ? extends D>> before) {
        return fromFunction((W1 paramW, X paramX, Y paramY, Z paramZ) -> {
            return requireNonNull(before,
                                  () -> "before").apply(paramW,
                                                        paramX,
                                                        paramY,
                                                        paramZ).<W1, X, Y, Z>fold(requireNonNull(container,
                                                                                                  () -> "container").convert(Fn4::narrowK));
        });
    }

    public <W1, X, Y, Z, A, B, C, D, E, F> Quintet<Fn4W, W1, X, Y, Z, F> dimap(final Quintet<Fn4W, A, B, C, D, E> container,
                                                                               final Fn4<? super W1, ? super X, ? super Y, ? super Z, ? extends Tuple4<? extends A, ? extends B, ? extends C, ? extends D>> mapper1,
                                                                               final Fn1<? super E, ? extends F> mapper2) {
        return fromFunction((W1 paramW, X paramX, Y paramY, Z paramZ) -> {
            return requireNonNull(mapper1,
                                  () -> "mapper1").apply(paramW,
                                                         paramX,
                                                         paramY,
                                                         paramZ).<F>fold((A paramA, B paramB, C paramC, D paramD) -> {
                return requireNonNull(mapper2,
                                      () -> "mapper2").apply(requireNonNull(container,
                                                                            () -> "container").convert(Fn4::narrowK)
                                                                                              .apply(paramA,
                                                                                                     paramB,
                                                                                                     paramC,
                                                                                                     paramD));
            });
        });
    }


    @AllArgsConstructor
    private static class DefaultFn4<A, B, C, D, E> implements Fn4<A, B, C, D, E> {

        private final Fn4<A, B, C, D, E> capturedLambda;

        @Override
        public E apply(final A a,
                       final B b,
                       final C c,
                       final D d) {
            return capturedLambda.apply(a,
                                        b,
                                        c,
                                        d);
        }
    }

}
