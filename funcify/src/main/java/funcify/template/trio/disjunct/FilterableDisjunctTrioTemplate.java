package funcify.template.trio.disjunct;

import funcify.ensemble.Trio;
import funcify.function.Fn1;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableDisjunctTrioTemplate<W> extends DisjunctTrioTemplate<W> {

    <A, B, C> Trio<W, A, B, C> filterFirst(Trio<W, A, B, C> container,
                                           Fn1<? super A, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                           Predicate<? super A> condition);

    default <A, B, C> Trio<W, A, B, C> filterNotFirst(Trio<W, A, B, C> container,
                                                      Fn1<? super A, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                      Predicate<? super A> condition) {
        return filterFirst(container,
                           ifNotFitsCondition,
                           Objects.requireNonNull(condition,
                                                  () -> "condition")
                                  .negate());
    }

    <A, B, C> Trio<W, A, B, C> filterSecond(Trio<W, A, B, C> container,
                                            Fn1<? super B, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                            Predicate<? super B> condition);

    default <A, B, C> Trio<W, A, B, C> filterNotSecond(Trio<W, A, B, C> container,
                                                       Fn1<? super B, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                       Predicate<? super B> condition) {
        return filterSecond(container,
                            ifNotFitsCondition,
                            Objects.requireNonNull(condition,
                                                   () -> "condition")
                                   .negate());
    }

    <A, B, C> Trio<W, A, B, C> filterThird(Trio<W, A, B, C> container,
                                           Fn1<? super C, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                           Predicate<? super C> condition);

    default <A, B, C> Trio<W, A, B, C> filterNotThird(Trio<W, A, B, C> container,
                                                      Fn1<? super C, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                      Predicate<? super C> condition) {
        return filterThird(container,
                           ifNotFitsCondition,
                           Objects.requireNonNull(condition,
                                                  () -> "condition")
                                  .negate());
    }

}
