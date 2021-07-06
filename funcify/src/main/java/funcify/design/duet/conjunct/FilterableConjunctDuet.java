package funcify.design.duet.conjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.duet.FilterableDuet;
import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.option.Option;
import funcify.template.duet.conjunct.FilterableConjunctDuetTemplate;
import funcify.tuple.Tuple2;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableConjunctDuet<W, A, B> extends ConjunctDuet<W, A, B>, FilterableDuet<W, A, B> {

    FilterableConjunctDuetTemplate<W> factory();


    default Option<Tuple2<A, B>> filter(final BiPredicate<? super A, ? super B> condition) {
        return factory().filter(this,
                                condition);
    }

    default Option<Tuple2<A, B>> filterNot(final BiPredicate<? super A, ? super B> condition) {
        return factory().filterNot(this,
                                   condition);
    }

    @Override
    default Option<A> filterFirst(final Predicate<? super A> condition) {
        return first().filter(condition);
    }

    @Override
    default FilterableDuet<W, A, B> filterFirst(final Predicate<? super A> condition,
                                                final Fn1<? super A, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition) {
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return fold((A paramA, B paramB) -> {
            return requireNonNull(condition,
                                  () -> "condition").test(paramA) ? this : ifNotFitsCondition.apply(paramA);
        });
    }

    @Override
    default Option<B> filterSecond(final Predicate<? super B> condition) {
        return second().filter(condition);
    }

    @Override
    default FilterableDuet<W, A, B> filterSecond(final Predicate<? super B> condition,
                                                 final Fn1<? super B, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition) {
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return fold((A paramA, B paramB) -> {
            return requireNonNull(condition,
                                  () -> "condition").test(paramB) ? this : ifNotFitsCondition.apply(paramB);
        });
    }

    default <WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filterOnFirst(Predicate<? super A> condition,
                                                                               Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                               Fn0<? extends DS> ifNotFitsCondition) {
        return factory().filterOnFirst(this,
                                       ifFitsCondition,
                                       ifNotFitsCondition,
                                       condition);
    }

    default Option<Tuple2<A, B>> filterOnFirst(Predicate<? super A> condition) {
        return factory().filterOnFirst(this,
                                       condition);
    }

    default <WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filterOnSecond(Predicate<? super B> condition,
                                                                                Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                                Fn0<? extends DS> ifNotFitsCondition) {
        return factory().filterOnSecond(this,
                                        ifFitsCondition,
                                        ifNotFitsCondition,
                                        condition);
    }

    default Option<Tuple2<A, B>> filterOnSecond(Predicate<? super B> condition) {
        return factory().filterOnSecond(this,
                                        condition);
    }

}
