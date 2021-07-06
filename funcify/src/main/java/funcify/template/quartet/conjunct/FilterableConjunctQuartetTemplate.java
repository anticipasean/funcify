package funcify.template.quartet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Trio;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.function.Fn3;
import funcify.option.Option;
import funcify.option.Option.OptionW;
import funcify.tuple.Tuple3;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableConjunctQuartetTemplate<W> extends ConjunctQuartetTemplate<W> {

    default <A, B, C, WDS, DS extends DisjunctSolo<WDS, Tuple3<A, B, C>>> DS filterOnFirst(Trio<W, A, B, C> container,
                                                                                           Fn1<Tuple3<A, B, C>, ? extends DS> ifFitsCondition,
                                                                                           Fn0<? extends DS> ifNotFitsCondition,
                                                                                           Predicate<? super A> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      (A paramA, B paramB, C paramC) -> {
                          return requireNonNull(condition,
                                                () -> "condition").test(paramA);
                      });
    }

    default <A, B, C> Option<Tuple3<A, B, C>> filterOnFirst(Trio<W, A, B, C> container,
                                                            Predicate<? super A> condition) {
        return this.<A, B, C, OptionW, Option<Tuple3<A, B, C>>>filterOnFirst(container,
                                                                             Option::of,
                                                                             Option::none,
                                                                             condition);
    }

    default <A, B, C, WDS, DS extends DisjunctSolo<WDS, Tuple3<A, B, C>>> DS filterOnSecond(Trio<W, A, B, C> container,
                                                                                            Fn1<Tuple3<A, B, C>, ? extends DS> ifFitsCondition,
                                                                                            Fn0<? extends DS> ifNotFitsCondition,
                                                                                            Predicate<? super B> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      (A paramA, B paramB, C paramC) -> {
                          return requireNonNull(condition,
                                                () -> "condition").test(paramB);
                      });
    }

    default <A, B, C> Option<Tuple3<A, B, C>> filterOnSecond(Trio<W, A, B, C> container,
                                                             Predicate<? super B> condition) {
        return this.<A, B, C, OptionW, Option<Tuple3<A, B, C>>>filterOnSecond(container,
                                                                              Option::of,
                                                                              Option::none,
                                                                              condition);
    }

    <A, B, C, WDS, DS extends DisjunctSolo<WDS, Tuple3<A, B, C>>> DS filter(Trio<W, A, B, C> container,
                                                                            Fn1<Tuple3<A, B, C>, ? extends DS> ifFitsCondition,
                                                                            Fn0<? extends DS> ifNotFitsCondition,
                                                                            Fn3<? super A, ? super B, ? super C, Boolean> condition);

    default <A, B, C, WDS, DS extends DisjunctSolo<WDS, Tuple3<A, B, C>>> DS filterNot(Trio<W, A, B, C> container,
                                                                                       Fn1<Tuple3<A, B, C>, ? extends DS> ifFitsCondition,
                                                                                       Fn0<? extends DS> ifNotFitsCondition,
                                                                                       Fn3<? super A, ? super B, ? super C, Boolean> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      requireNonNull(condition,
                                     () -> "condition").andThen(b -> !b));
    }

    default <A, B, C> Option<Tuple3<A, B, C>> filter(Trio<W, A, B, C> container,
                                                     Fn3<? super A, ? super B, ? super C, Boolean> condition) {
        return this.<A, B, C, OptionW, Option<Tuple3<A, B, C>>>filter(container,
                                                                      Option::of,
                                                                      Option::none,
                                                                      condition);
    }

    default <A, B, C> Option<Tuple3<A, B, C>> filterNot(Trio<W, A, B, C> container,
                                                        Fn3<? super A, ? super B, ? super C, Boolean> condition) {
        return this.<A, B, C, OptionW, Option<Tuple3<A, B, C>>>filterNot(container,
                                                                         Option::of,
                                                                         Option::none,
                                                                         condition);
    }

}
