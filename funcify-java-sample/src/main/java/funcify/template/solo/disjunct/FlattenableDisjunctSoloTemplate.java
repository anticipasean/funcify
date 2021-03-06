package funcify.template.solo.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Solo;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.template.solo.FilterableSoloTemplate;
import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-01
 */
public interface FlattenableDisjunctSoloTemplate<W> extends FlattenableSoloTemplate<W>, IterableDisjunctSoloTemplate<W>,
                                                            FilterableSoloTemplate<W> {

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

    @Override
    default <A, WDS, DS extends DisjunctSolo<WDS, A>> DS filter(Solo<W, A> container,
                                                                Fn1<? super A, ? extends DS> ifFitsCondition,
                                                                Fn0<? extends DS> ifNotFitsCondition,
                                                                Predicate<? super A> condition) {
        requireNonNull(ifFitsCondition,
                       () -> "ifFitsCondition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        requireNonNull(condition,
                       () -> "condition");
        return fold(map(container,
                        (A paramA) -> {
                            return condition.test(paramA) ? ifFitsCondition.apply(paramA) : ifNotFitsCondition.apply();
                        }),
                    ds -> ds,
                    ifNotFitsCondition::apply);
    }

}
