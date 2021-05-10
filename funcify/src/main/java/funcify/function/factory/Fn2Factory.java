package funcify.function.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Trio;
import funcify.function.Fn2;
import funcify.function.Fn2.Fn2W;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public class Fn2Factory {

    private static enum FactoryHolder {

        INSTANCE(new Fn2Factory());

        private final Fn2Factory fn2Factory;

        FactoryHolder(final Fn2Factory fn2Factory) {
            this.fn2Factory = fn2Factory;
        }

        public Fn2Factory getFn2Factory() {
            return fn2Factory;
        }
    }

    private Fn2Factory() {

    }

    public static Fn2Factory getInstance() {
        return FactoryHolder.INSTANCE.getFn2Factory();
    }

    public <A, B, C> Fn2<A, B, C> fromFunction(BiFunction<A, B, C> biFunction) {
        if (biFunction instanceof Fn2) {
            return (Fn2<A, B, C>) biFunction;
        }
        return new DefaultFn2<A, B, C>(requireNonNull(biFunction,
                                                      () -> "biFunction"));
    }

    public <A, B, C, D> Fn2<A, B, D> map(Trio<Fn2W, A, B, C> container,
                                         Function<? super C, ? extends D> mapper) {
        return fromFunction((A paramA, B paramB) -> requireNonNull(mapper,
                                                                   () -> "mapper").apply(requireNonNull(container,
                                                                                                        () -> "container").convert(Fn2::narrowK)
                                                                                                                          .apply(paramA,
                                                                                                                                 paramB)));
    }

    public <A, B, C, D> Fn2<A, B, D> flatMap(Trio<Fn2W, A, B, C> container,
                                             Function<? super C, ? extends Trio<Fn2W, A, B, D>> flatMapper) {
        return fromFunction((A paramA, B paramB) -> {
            return requireNonNull(flatMapper,
                                  () -> "flatMapper").apply(requireNonNull(container,
                                                                           () -> "container").convert(Fn2::narrowK)
                                                                                             .apply(paramA,
                                                                                                    paramB))
                                                     .convert(Fn2::narrowK)
                                                     .apply(paramA,
                                                            paramB);
        });
    }

    public <A, B, C> Fn2<A, B, C> flatten(Trio<Fn2W, A, B, Trio<Fn2W, A, B, C>> container) {
        return flatMap(container,
                       f -> f);
    }

    public <Y, Z, A, B, C, D> Fn2<Y, Z, D> dimap(Trio<Fn2W, A, B, C> container,
                                                 BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> mapper1,
                                                 Function<? super C, ? extends D> mapper2) {
        return fromFunction((Y paramY, Z paramZ) -> {
            return requireNonNull(mapper1,
                                  () -> "mapper1").apply(paramY,
                                                         paramZ)
                                                  .fold((A paramA, B paramB) -> {
                                                      return requireNonNull(container,
                                                                            () -> "container").convert(Fn2::narrowK)
                                                                                              .apply(paramA,
                                                                                                     paramB);
                                                  });
        }).map(requireNonNull(mapper2,
                              () -> "mapper2"));
    }

    public <Z, A, B, C> Fn2<Z, B, C> contraMapFirst(Trio<Fn2W, A, B, C> container,
                                                    Function<? super Z, ? extends A> mapper) {
        return dimap(container,
                     (Z paramZ, B paramB) -> {
                         return Tuple2.of(requireNonNull(mapper,
                                                         () -> "mapper").apply(paramZ),
                                          paramB);
                     },
                     c -> c);
    }

    public <Z, A, B, C> Fn2<A, Z, C> contraMapSecond(Trio<Fn2W, A, B, C> container,
                                                     Function<? super Z, ? extends B> mapper) {
        return dimap(container,
                     (A paramA, Z paramZ) -> {
                         return Tuple2.of(paramA,
                                          requireNonNull(mapper,
                                                         () -> "mapper").apply(paramZ));
                     },
                     c -> c);
    }

    public <Y, Z, A, B, C> Fn2<Y, Z, C> contraMap(Trio<Fn2W, A, B, C> container,
                                                  BiFunction<? super Y, ? super Z, ? extends Tuple2<A, B>> mapper) {
        return dimap(container,
                     mapper,
                     c -> c);
    }

    @AllArgsConstructor
    private static class DefaultFn2<A, B, C> implements Fn2<A, B, C> {

        private final BiFunction<A, B, C> biFunction;

        @Override
        public C apply(final A a,
                       final B b) {
            return biFunction.apply(a,
                                    b);
        }
    }
}
