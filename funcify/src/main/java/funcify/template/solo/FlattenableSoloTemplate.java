package funcify.template.solo;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * ¬
 *
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSoloTemplate<W> extends ZippableSoloTemplate<W> {

    default <A> Solo<W, A> widenK(final Solo<W, ? extends A> narrowInstance) {
        return map(narrowInstance,
                   a -> a);
    }

    @SuppressWarnings("unchecked")
    default <A> Solo<W, A> narrowK(final Solo<W, ? super A> wideInstance) {
        return map(wideInstance,
                   a -> (A) a);
    }

    <A, B> Solo<W, B> flatMap(final Solo<W, A> container,
                              final Function<? super A, ? extends Solo<W, B>> flatMapper);

    default <A> Solo<W, A> flatten(Solo<W, Solo<W, A>> container) {
        return flatMap(container,
                       b -> b);
    }


    @Override
    default <A, B, F extends Function<A, B>> Solo<W, B> ap(final Solo<W, A> container,
                                                           final Solo<W, F> containerWithFunction) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA) -> {
                           return flatMap(requireNonNull(containerWithFunction,
                                                         () -> "containerWithFunction"),
                                          (Function<A, B> func) -> {
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

                                          }).convert(this::widenK);
                       });
    }

}
