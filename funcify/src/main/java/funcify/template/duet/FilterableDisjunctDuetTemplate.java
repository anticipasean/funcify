package funcify.template.duet;

import funcify.ensemble.Duet;
import funcify.function.Fn1;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-09
 */
public interface FilterableDisjunctDuetTemplate<W> extends DisjunctDuetTemplate<W> {

    <A, B, WDD, DD extends Duet<WDD, A, B>> DD filterFirst(Duet<W, A, B> container,
                                                           Fn1<? super A, ? extends DD> ifNotFitsCondition,
                                                           Predicate<? super A> condition);

    <A, B, WDD, DD extends Duet<WDD, A, B>> DD filterSecond(Duet<W, A, B> container,
                                                            Fn1<? super B, ? extends DD> ifNotFitsCondition,
                                                            Predicate<? super B> condition);

}
