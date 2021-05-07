package funcify.template.solo;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-01
 */
public interface FlattenableDisjunctSoloTemplate<W> extends FlattenableSoloTemplate<W>, IterableDisjunctSoloTemplate<W> {

    @Override
    default <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                                      final Function<? super A, ? extends Solo<W, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"),
                    this::<B>empty);
    }

    @Override
    default <A, B, F extends Function<? super A, ? extends B>> Solo<W, B> ap(final Solo<W, A> container,
                                                                             final Solo<W, F> containerWithFunction) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA) -> {
                           return flatMap(requireNonNull(containerWithFunction,
                                                         () -> "containerWithFunction"),
                                          (Function<? super A, ? extends B> func) -> {
                                              return from(func.apply(paramA));
                                          });
                       });
    }

    @Override
    default <A, B> Solo<W, B> map(final Solo<W, A> container,
                                  final Function<? super A, ? extends B> mapper) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       requireNonNull(mapper,
                                      () -> "mapper").andThen(b -> from(b)));
    }

    @Override
    default <A, B, C> Solo<W, C> zip(final Solo<W, A> container1,
                                     final Solo<W, B> container2,
                                     final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A a) -> {
                           return flatMap(container2,
                                          (B b) -> {
                                              return requireNonNull(combiner,
                                                                    () -> "combiner").andThen(value -> from(value))
                                                                                     .apply(a,
                                                                                            b);

                                          }).narrowT1();
                       });
    }


}
