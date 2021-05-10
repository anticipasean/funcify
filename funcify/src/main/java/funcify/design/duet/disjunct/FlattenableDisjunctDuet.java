package funcify.design.duet.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.design.duet.FilterableDuet;
import funcify.design.duet.FlattenableDuet;
import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.option.Option;
import funcify.template.duet.disjunct.FlattenableDisjunctDuetTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface FlattenableDisjunctDuet<W, A, B> extends DisjunctDuet<W, A, B>, FlattenableDuet<W, A, B>,
                                                          FilterableDuet<W, A, B> {

    @Override
    FlattenableDisjunctDuetTemplate<W> factory();

    @Override
    default Duet<W, A, B> first(final A value) {
        return factory().first(value);
    }

    @Override
    default Duet<W, A, B> second(final B value) {
        return factory().second(value);
    }


    @Override
    default Option<A> filterFirst(final Predicate<? super A> condition) {
        requireNonNull(condition,
                       () -> "condition");
        return fold(a -> condition.test(a) ? Option.of(a) : Option.none(),
                    b -> Option.none());
    }

    @Override
    default FlattenableDisjunctDuet<W, A, B> filterFirst(final Predicate<? super A> condition,
                                                         final Fn1<? super A, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return fold(a -> condition.test(a) ? this : ifNotFitsCondition.apply(a),
                    b -> this).narrowT1();
    }

    @Override
    default Option<B> filterSecond(final Predicate<? super B> condition) {
        requireNonNull(condition,
                       () -> "condition");
        return fold(a -> Option.none(),
                    b -> condition.test(b) ? Option.of(b) : Option.none());
    }

    @Override
    default FlattenableDisjunctDuet<W, A, B> filterSecond(final Predicate<? super B> condition,
                                                          final Fn1<? super B, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return fold(a -> this,
                    b -> condition.test(b) ? this : ifNotFitsCondition.apply(b)).narrowT1();
    }
}
