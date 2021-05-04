package funcify.template.duet;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableConjunctDuetTemplate<W> extends ZippableConjunctDuetTemplate<W>, FlattenableDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    <A, B> Duet<W, A, B> both(A value1,
                              B value2);

    <A, B, C, D> Duet<W, C, D> flatMap(final Duet<W, A, B> container,
                                       final BiFunction<? super A, ? super B, ? extends Duet<W, C, D>> flatMapper);

    default <A, B> Duet<W, A, B> flatten1(final Duet<W, Duet<W, A, B>, B> container) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (Duet<W, A, B> paramA, B paramB) -> {
                           return flatMap(paramA,
                                          (A paramA1, B paramB1) -> {
                                              return both(paramA1,
                                                          paramB);
                                          });
                       });
    }

    default <A, B> Duet<W, A, B> flatten2(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, Duet<W, A, B> paramB) -> {
                           return flatMap(paramB,
                                          (A paramA2, B paramB2) -> {
                                              return both(paramA,
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
                                                                 return both(paramA1,
                                                                             paramB2);
                                                             });
                                          });
                       });
    }

    @Override
    default <A, B, C> Duet<W, C, B> map1(Duet<W, A, B> container,
                                         Function<? super A, ? extends C> mapper) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> requireNonNull(mapper,
                                                              () -> "mapper").andThen(c -> this.<C, B>both(c,
                                                                                                           paramB))
                                                                             .apply(paramA));
    }

    @Override
    default <A, B, C> Duet<W, A, C> map2(Duet<W, A, B> container,
                                         Function<? super B, ? extends C> mapper) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> requireNonNull(mapper,
                                                              () -> "mapper").andThen(c -> this.<A, C>both(paramA,
                                                                                                           c))
                                                                             .apply(paramB));
    }

    @Override
    default <A, B, C, D> Duet<W, C, D> bimap(Duet<W, A, B> container,
                                             Function<? super A, ? extends C> mapper1,
                                             Function<? super B, ? extends D> mapper2) {
        return flatMap(requireNonNull(container,
                                      () -> "container"),
                       (A paramA, B paramB) -> both(requireNonNull(mapper1,
                                                                   () -> "mapper1").apply(paramA),
                                                    requireNonNull(mapper2,
                                                                   () -> "mapper2").apply(paramB)));
    }


    @Override
    default <A, B, C> Duet<W, C, B> zipFirst(Duet<W, A, B> container1,
                                             Duet<W, A, B> container2,
                                             BiFunction<? super A, ? super A, ? extends C> combiner) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A paramA1, B paramB1) -> {
                           return flatMap(requireNonNull(container2,
                                                         () -> "container2"),
                                          (A paramA2, B paramB2) -> {
                                              return both(requireNonNull(combiner,
                                                                         () -> "combiner").apply(paramA1,
                                                                                                 paramA2),
                                                          paramB1);
                                          });
                       });
    }

    @Override
    default <A, B, C> Duet<W, A, C> zipSecond(Duet<W, A, B> container1,
                                              Duet<W, A, B> container2,
                                              BiFunction<? super B, ? super B, ? extends C> combiner) {
        return flatMap(requireNonNull(container1,
                                      () -> "container1"),
                       (A paramA1, B paramB1) -> {
                           return flatMap(requireNonNull(container2,
                                                         () -> "container2"),
                                          (A paramA2, B paramB2) -> {
                                              return both(paramA1,
                                                          requireNonNull(combiner,
                                                                         () -> "combiner").apply(paramB1,
                                                                                                 paramB2));
                                          });
                       });
    }

}
