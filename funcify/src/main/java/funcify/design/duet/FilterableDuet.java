package funcify.design.duet;

import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.option.Option;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableDuet<W, A, B> extends Duet<W, A, B> {

    Option<A> filterFirst(final Predicate<? super A> condition);

    FilterableDuet<W, A, B> filterFirst(final Predicate<? super A> condition,
                                        final Fn1<? super A, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition);

    Option<B> filterSecond(final Predicate<? super B> condition);

    FilterableDuet<W, A, B> filterSecond(final Predicate<? super B> condition,
                                         final Fn1<? super B, ? extends FilterableDuet<W, A, B>> ifNotFitsCondition);

}
