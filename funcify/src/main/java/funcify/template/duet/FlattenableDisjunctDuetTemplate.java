package funcify.template.duet;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableDisjunctDuetTemplate<W> extends FlattenableDuetTemplate<W>, IterableDisjunctDuetTemplate<W> {

    @Override
    default <A, B, C> Duet<W, C, B> flatMapFirst(final Duet<W, A, B> container,
                                                 final Function<? super A, ? extends Duet<W, C, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"),
                    b -> second(b));
    }

    @Override
    default <A, B, C> Duet<W, A, C> flatMapSecond(final Duet<W, A, B> container,
                                                  final Function<? super B, ? extends Duet<W, A, C>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    a -> first(a),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"));
    }

    default <A, B, C> Duet<W, A, B> flatMapFirstToSecond(final Duet<W, A, B> container,
                                                         final Function<? super A, ? extends Duet<W, A, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"),
                    b -> second(b));
    }

    default <A, B, C> Duet<W, A, B> flatMapSecondToFirst(final Duet<W, A, B> container,
                                                         final Function<? super B, ? extends Duet<W, A, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    a -> first(a),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"));
    }

    //    @Override
    //    default <A, B, C, D> Duet<W, C, D> flatMap(final Duet<W, A, B> container,
    //                                               final BiFunction<? super A, ? super B, ? extends Duet<W, C, D>> flatMapper) {
    //        return fold(requireNonNull(container,
    //                                   () -> "container"),
    //                    (A paramA) -> {
    //                        return requireNonNull(flatMapper,
    //                                              () -> "flatMapper").apply(paramA,
    //                                                                        null);
    //                    },
    //                    (B paramB) -> {
    //                        return requireNonNull(flatMapper,
    //                                              () -> "flatMapper").apply(null,
    //                                                                        paramB);
    //                    });
    //    }

    default <A, B> Duet<W, A, B> flattenFirst(final Duet<W, Duet<W, A, B>, B> container) {
        return flatMapFirst(requireNonNull(container,
                                           () -> "container"),
                            (Duet<W, A, B> paramA) -> {
                                return flatMapFirst(paramA,
                                                    (A paramA1) -> {
                                                        return first(paramA1);
                                                    });
                            });
    }

    default <A, B> Duet<W, A, B> flattenSecond(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMapSecond(requireNonNull(container,
                                            () -> "container"),
                             (Duet<W, A, B> paramB) -> {
                                 return flatMapSecond(paramB,
                                                      (B paramB2) -> {
                                                          return second(paramB2);
                                                      });
                             });
    }


    @Override
    default <A, B, C, D> Duet<W, D, B> zipFirst(Duet<W, A, B> container1,
                                                Duet<W, C, B> container2,
                                                BiFunction<? super A, ? super C, ? extends D> combiner) {
        return flatMapFirst(container1,
                            (A paramA) -> {
                                return flatMapFirst(container2,
                                                    (C paramC) -> {
                                                        return first(requireNonNull(combiner,
                                                                                    () -> "combiner").apply(paramA,
                                                                                                            paramC));

                                                    });
                            });
    }

    @Override
    default <A, B, C, D> Duet<W, A, D> zipSecond(Duet<W, A, B> container1,
                                                 Duet<W, A, C> container2,
                                                 BiFunction<? super B, ? super C, ? extends D> combiner) {
        return flatMapSecond(container1,
                             (B paramB) -> {
                                 return flatMapSecond(container2,
                                                      (C paramC) -> {
                                                          return second(requireNonNull(combiner,
                                                                                       () -> "combiner").apply(paramB,
                                                                                                               paramC));
                                                      });
                             });
    }
    //
    //    @Override
    //    default <A, B, C, D, E, F> Duet<W, E, F> zipBoth(Duet<W, A, B> container1,
    //                                                     Duet<W, C, D> container2,
    //                                                     BiFunction<? super A, ? super C, ? extends E> combiner1,
    //                                                     BiFunction<? super B, ? super D, ? extends F> combiner2) {
    //        return flatMap(container1,
    //                       (A paramA, B paramB) -> {
    //                           return flatMap(container2,
    //                                          (C paramC, D paramD) -> {
    //                                              return both(requireNonNull(combiner1,
    //                                                                         () -> "combiner1").apply(paramA,
    //                                                                                                  paramC),
    //                                                          requireNonNull(combiner2,
    //                                                                         () -> "combiner2").apply(paramB,
    //                                                                                                  paramD));
    //                                          });
    //                       });
    //    }

    @Override
    default <A, B, C> Duet<W, C, B> mapFirst(Duet<W, A, B> container,
                                             Function<? super A, ? extends C> mapper) {
        return fold(container,
                    (A paramA) -> first(requireNonNull(mapper,
                                                       () -> "mapper").apply(paramA)),
                    (B paramB) -> second(paramB));
    }

    @Override
    default <A, B, C> Duet<W, A, C> mapSecond(Duet<W, A, B> container,
                                              Function<? super B, ? extends C> mapper) {
        return fold(container,
                    (A paramA) -> first(paramA),
                    (B paramB) -> second(requireNonNull(mapper,
                                                        () -> "mapper").apply(paramB)));
    }

    @Override
    default <A, B, C, D> Duet<W, C, D> bimap(Duet<W, A, B> container,
                                             Function<? super A, ? extends C> mapper1,
                                             Function<? super B, ? extends D> mapper2) {
        return fold(container,
                    (A paramA) -> first(requireNonNull(mapper1,
                                                       () -> "mapper1").apply(paramA)),
                    (B paramB) -> second(requireNonNull(mapper2,
                                                        () -> "mapper2").apply(paramB)));
    }
}
