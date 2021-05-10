package funcify.template.duet.conjunct;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Duet;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.option.Option;
import funcify.option.Option.OptionW;
import funcify.tuple.Tuple2;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-09
 */
public interface FilterableConjunctDuetTemplate<W> extends ConjunctDuetTemplate<W> {

    default <A, B, WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filterOnFirst(Duet<W, A, B> container,
                                                                                     Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                                     Fn0<? extends DS> ifNotFitsCondition,
                                                                                     Predicate<? super A> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      (A paramA, B paramB) -> {
                          return Objects.requireNonNull(condition,
                                                        () -> "condition")
                                        .test(paramA);
                      });
    }

    default <A, B> Option<Tuple2<A, B>> filterOnFirst(Duet<W, A, B> container,
                                                      Predicate<? super A> condition) {
        return this.<A, B, OptionW, Option<Tuple2<A, B>>>filterOnFirst(container,
                                                                       Option::of,
                                                                       Option::none,
                                                                       condition);
    }

    default <A, B, WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filterOnSecond(Duet<W, A, B> container,
                                                                                      Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                                      Fn0<? extends DS> ifNotFitsCondition,
                                                                                      Predicate<? super B> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      (A paramA, B paramB) -> {
                          return Objects.requireNonNull(condition,
                                                        () -> "condition")
                                        .test(paramB);
                      });
    }

    default <A, B> Option<Tuple2<A, B>> filterOnSecond(Duet<W, A, B> container,
                                                       Predicate<? super B> condition) {
        return this.<A, B, OptionW, Option<Tuple2<A, B>>>filterOnSecond(container,
                                                                        Option::of,
                                                                        Option::none,
                                                                        condition);
    }

    <A, B, WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filter(Duet<W, A, B> container,
                                                                      Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                      Fn0<? extends DS> ifNotFitsCondition,
                                                                      BiPredicate<? super A, ? super B> condition);

    default <A, B> Option<Tuple2<A, B>> filter(Duet<W, A, B> container,
                                               BiPredicate<? super A, ? super B> condition) {
        return this.<A, B, OptionW, Option<Tuple2<A, B>>>filter(container,
                                                                Option::of,
                                                                Option::none,
                                                                condition);
    }

}
