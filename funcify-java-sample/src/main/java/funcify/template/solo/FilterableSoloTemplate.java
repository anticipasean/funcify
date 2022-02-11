package funcify.template.solo;

import static java.util.Objects.requireNonNull;

import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.ensemble.Solo;
import funcify.function.Fn0;
import funcify.function.Fn1;
import funcify.option.Option;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-08
 */
public interface FilterableSoloTemplate<W> {

    <A, WDS, DS extends DisjunctSolo<WDS, A>> DS filter(Solo<W, A> container,
                                                        Fn1<? super A, ? extends DS> ifFitsCondition,
                                                        Fn0<? extends DS> ifNotFitsCondition,
                                                        Predicate<? super A> condition);

    default <A, WDS, DS extends DisjunctSolo<WDS, A>> DS filterNot(Solo<W, A> container,
                                                                   Fn1<? super A, ? extends DS> ifFitsCondition,
                                                                   Fn0<? extends DS> ifNotFitsCondition,
                                                                   Predicate<? super A> condition) {
        return filter(container,
                      ifFitsCondition,
                      ifNotFitsCondition,
                      requireNonNull(condition,
                                     () -> "condition").negate());
    }

    default <A> Option<A> filter(Solo<W, A> container,
                                 Predicate<? super A> condition) {
        return filter(container,
                      Option::of,
                      Option::none,
                      condition);
    }

    default <A> Option<A> filterNot(Solo<W, A> container,
                                    Predicate<? super A> condition) {
        return filter(container,
                      requireNonNull(condition,
                                     () -> "condition").negate());
    }

}
