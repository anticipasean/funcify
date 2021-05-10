package funcify.template.duet.disjunct;

import funcify.ensemble.Duet;
import funcify.function.Fn1;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-09
 */
public interface FilterableDisjunctDuetTemplate<W> extends DisjunctDuetTemplate<W> {

    <A, B> Duet<W, A, B> filterFirst(Duet<W, A, B> container,
                                     Fn1<? super A, ? extends Duet<W, A, B>> ifNotFitsCondition,
                                     Predicate<? super A> condition);

    <A, B> Duet<W, A, B> filterSecond(Duet<W, A, B> container,
                                      Fn1<? super B, ? extends Duet<W, A, B>> ifNotFitsCondition,
                                      Predicate<? super B> condition);

}
