package funcify.design.trio;


import funcify.ensemble.Trio;
import funcify.function.Fn1;
import funcify.option.Option;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface FilterableTrio<W, A, B, C> extends Trio<W, A, B, C> {

    Option<A> filterFirst(final Predicate<? super A> condition);

    Option<A> filterNotFirst(final Predicate<? super A> condition);

    FilterableTrio<W, A, B, C> filterFirst(final Predicate<? super A> condition,
                                           final Fn1<? super A, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);

    FilterableTrio<W, A, B, C> filterNotFirst(final Predicate<? super A> condition,
                                              final Fn1<? super A, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);

    Option<B> filterSecond(final Predicate<? super B> condition);

    Option<B> filterNotSecond(final Predicate<? super B> condition);

    FilterableTrio<W, A, B, C> filterSecond(final Predicate<? super B> condition,
                                            final Fn1<? super B, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);

    FilterableTrio<W, A, B, C> filterNotSecond(final Predicate<? super B> condition,
                                               final Fn1<? super B, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);

    Option<C> filterThird(final Predicate<? super C> condition);

    Option<C> filterNotThird(final Predicate<? super C> condition);

    FilterableTrio<W, A, B, C> filterThird(final Predicate<? super C> condition,
                                           final Fn1<? super C, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);

    FilterableTrio<W, A, B, C> filterNotThird(final Predicate<? super C> condition,
                                              final Fn1<? super C, ? extends FilterableTrio<W, A, B, C>> ifNotFitsCondition);


}
