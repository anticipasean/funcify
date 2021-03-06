package funcify.template.duet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Duet;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.template.duet.FlattenableDuetTemplate;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableConjunctDuetTemplate<W> extends FlattenableDuetTemplate<W>, IterableConjunctDuetTemplate<W>,
                                                            FilterableConjunctDuetTemplate<W>, PeekableConjunctDuetTemplate<W> {


    default <A, B, C, D> Duet<W, C, D> flatMap(final Duet<W, A, B> container,
                                               final BiFunction<? super A, ? super B, ? extends Duet<W, C, D>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A paramA, B paramB) -> {
                        return requireNonNull(flatMapper,
                                              () -> "flatMapper").apply(paramA,
                                                                        paramB)
                                                                 .narrowT1();
                    });
    }

    @Override
    default <A, B, C> Duet<W, C, B> flatMapFirst(final Duet<W, A, B> container,
                                                 final Function<? super A, ? extends Duet<W, C, B>> flatMapper) {
        return flatMap(container,
                       (A paramA, B paramB) -> requireNonNull(flatMapper,
                                                              () -> "flatMapper").apply(paramA));
    }

    @Override
    default <A, B, C> Duet<W, A, C> flatMapSecond(final Duet<W, A, B> container,
                                                  final Function<? super B, ? extends Duet<W, A, C>> flatMapper) {
        return flatMap(container,
                       (A paramA, B paramB) -> requireNonNull(flatMapper,
                                                              () -> "flatMapper").apply(paramB));
    }

    default <A, B> Duet<W, A, B> flattenFirst(final Duet<W, Duet<W, A, B>, B> container) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (Duet<W, A, B> paramA, B paramB) -> {
                           return flatMap(paramA,
                                          (A paramA1, B paramB1) -> {
                                              return from(paramA1,
                                                          paramB);
                                          });
                       });
    }

    default <A, B> Duet<W, A, B> flattenSecond(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, Duet<W, A, B> paramB) -> {
                           return flatMap(paramB,
                                          (A paramA2, B paramB2) -> {
                                              return from(paramA,
                                                          paramB2);
                                          });
                       });
    }

    default <A, B> Duet<W, A, B> flattenBoth(final Duet<W, Duet<W, A, B>, Duet<W, A, B>> container) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (Duet<W, A, B> paramA, Duet<W, A, B> paramB) -> {
                           return flatMap(paramA,
                                          (A paramA1, B paramB1) -> {
                                              return flatMap(paramB,
                                                             (A paramA2, B paramB2) -> {
                                                                 return from(paramA1,
                                                                             paramB2);
                                                             });
                                          });
                       });
    }


    @Override
    default <A, B, C> Duet<W, C, B> mapFirst(Duet<W, A, B> container,
                                             Function<? super A, ? extends C> mapper) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> requireNonNull(mapper,
                                                              () -> "mapper").andThen(c -> this.<C, B>from(c,
                                                                                                           paramB))
                                                                             .apply(paramA));
    }

    @Override
    default <A, B, C> Duet<W, A, C> mapSecond(Duet<W, A, B> container,
                                              Function<? super B, ? extends C> mapper) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> requireNonNull(mapper,
                                                              () -> "mapper").andThen(c -> this.<A, C>from(paramA,
                                                                                                           c))
                                                                             .apply(paramB));
    }

    @Override
    default <A, B, C, D> Duet<W, C, D> bimap(Duet<W, A, B> container,
                                             Function<? super A, ? extends C> mapper1,
                                             Function<? super B, ? extends D> mapper2) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> from(requireNonNull(mapper1,
                                                                   () -> "mapper1").apply(paramA),
                                                    requireNonNull(mapper2,
                                                                   () -> "mapper2").apply(paramB)));
    }


    @Override
    default <A, B, C, D> Duet<W, D, B> zipFirst(Duet<W, A, B> container1,
                                                Duet<W, C, B> container2,
                                                BiFunction<? super A, ? super C, ? extends D> combiner) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A paramA, B paramB1) -> {
                           return flatMap(requireNonNull(container2,
                                                         () -> "container2"),
                                          (C paramC, B paramB2) -> {
                                              return from(requireNonNull(combiner,
                                                                         () -> "combiner").apply(paramA,
                                                                                                 paramC),
                                                          paramB1);
                                          });
                       });
    }

    @Override
    default <A, B, C, D> Duet<W, A, D> zipSecond(Duet<W, A, B> container1,
                                                 Duet<W, A, C> container2,
                                                 BiFunction<? super B, ? super C, ? extends D> combiner) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A paramA, B paramB) -> {
                           return flatMap(requireNonNull(container2,
                                                         () -> "container2"),
                                          (A paramA2, C paramC) -> {
                                              return from(paramA,
                                                          requireNonNull(combiner,
                                                                         () -> "combiner").apply(paramB,
                                                                                                 paramC));
                                          });
                       });
    }

    default <A, B, C, D, E, F> Duet<W, E, F> zipBoth(Duet<W, A, B> container1,
                                                     Duet<W, C, D> container2,
                                                     BiFunction<? super A, ? super C, ? extends E> combiner1,
                                                     BiFunction<? super B, ? super D, ? extends F> combiner2) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A paramA, B paramB) -> {
                           return flatMap(requireNonNull(container2,
                                                         () -> "container2"),
                                          (C paramC, D paramD) -> {
                                              return from(requireNonNull(combiner1,
                                                                         () -> "combiner1").apply(paramA,
                                                                                                  paramC),
                                                          requireNonNull(combiner2,
                                                                         () -> "combiner2").apply(paramB,
                                                                                                  paramD));
                                          });
                       });
    }

    @Override
    default <A, B, WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filter(Duet<W, A, B> container,
                                                                              Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                              Fn0<? extends DS> ifNotFitsCondition,
                                                                              BiPredicate<? super A, ? super B> condition) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A paramA, B paramB) -> {
                        return requireNonNull(condition,
                                              () -> "condition").test(paramA,
                                                                      paramB) ? ifFitsCondition.apply(Tuple2.of(paramA,
                                                                                                                paramB))
                            : requireNonNull(ifNotFitsCondition,
                                             () -> "ifNotFitsCondition").get();
                    });
    }
}
