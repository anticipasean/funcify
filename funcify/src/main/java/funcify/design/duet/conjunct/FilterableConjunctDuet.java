package funcify.design.duet.conjunct;

import funcify.design.duet.FilterableDuet;
import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Duet;
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

    Option<FilterableConjunctDuet<W, A, B>> filter(final BiPredicate<? super A, ? super B> condition);

    default <WDS, DS extends DisjunctSolo<WDS, Tuple2<A, B>>> DS filterOnFirst(Predicate<? super A> condition,
                                                                               Fn1<Tuple2<A, B>, ? extends DS> ifFitsCondition,
                                                                               Fn0<? extends DS> ifNotFitsCondition) {
        return factory().filterOnFirst(this,
                                       ifFitsCondition,
                                       ifNotFitsCondition,
                                       condition);
    }

    default Option<Tuple2<A, B>> filterOnFirst(Duet<W, A, B> container,
                                               Predicate<? super A> condition) {
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
