package funcify.design.trio.disjunct;

import funcify.design.trio.FilterableTrio;
import funcify.function.Fn1;
import funcify.option.Option;
import funcify.template.trio.disjunct.FilterableDisjunctTrioTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface FilterableDisjunctTrio<W, A, B, C> extends DisjunctTrio<W, A, B, C>, FilterableTrio<W, A, B, C> {

    FilterableDisjunctTrioTemplate<W> factory();


    @Override
    default FilterableDisjunctTrio<W, A, B, C> filterFirst(final Predicate<? super A> condition,
                                                           final Fn1<? super A, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterFirst(this,
                                              ifNotFitsCondition,
                                              condition).narrowT1();
    }

    @Override
    default FilterableDisjunctTrio<W, A, B, C> filterSecond(final Predicate<? super B> condition,
                                                            final Fn1<? super B, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterSecond(this,
                                               ifNotFitsCondition,
                                               condition).narrowT1();
    }


    @Override
    default FilterableDisjunctTrio<W, A, B, C> filterThird(final Predicate<? super C> condition,
                                                           final Fn1<? super C, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterThird(this,
                                              ifNotFitsCondition,
                                              condition).narrowT1();
    }

    @Override
    default FilterableTrio<W, A, B, C> filterNotFirst(final Predicate<? super A> condition,
                                                      final Fn1<? super A, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterNotFirst(this,
                                                 ifNotFitsCondition,
                                                 condition).narrowT1();
    }

    @Override
    default FilterableTrio<W, A, B, C> filterNotSecond(final Predicate<? super B> condition,
                                                       final Fn1<? super B, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterNotSecond(this,
                                                  ifNotFitsCondition,
                                                  condition).narrowT1();
    }

    @Override
    default FilterableTrio<W, A, B, C> filterNotThird(final Predicate<? super C> condition,
                                                      final Fn1<? super C, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition) {
        return factory().<A, B, C>filterNotThird(this,
                                                 ifNotFitsCondition,
                                                 condition).narrowT1();
    }

    @Override
    default Option<A> filterFirst(final Predicate<? super A> condition) {
        return getFirst().filter(condition);
    }

    @Override
    default Option<B> filterSecond(final Predicate<? super B> condition) {
        return getSecond().filter(condition);
    }

    @Override
    default Option<C> filterThird(final Predicate<? super C> condition) {
        return getThird().filter(condition);
    }
}
