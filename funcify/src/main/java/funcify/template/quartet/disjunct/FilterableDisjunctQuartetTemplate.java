package funcify.template.quartet.disjunct;

import funcify.ensemble.Quartet;
import funcify.function.Fn1;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FilterableDisjunctQuartetTemplate<W> extends DisjunctQuartetTemplate<W> {

    <A, B, C, D> Quartet<W, A, B, C, D> filterFirst(Quartet<W, A, B, C, D> container,
                                                    Fn1<? super A, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                    Predicate<? super A> condition);

    default <A, B, C, D> Quartet<W, A, B, C, D> filterNotFirst(Quartet<W, A, B, C, D> container,
                                                               Fn1<? super A, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                               Predicate<? super A> condition) {
        return filterFirst(container,
                           ifNotFitsCondition,
                           Objects.requireNonNull(condition,
                                                  () -> "condition")
                                  .negate());
    }

    <A, B, C, D> Quartet<W, A, B, C, D> filterSecond(Quartet<W, A, B, C, D> container,
                                                     Fn1<? super B, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                     Predicate<? super B> condition);

    default <A, B, C, D> Quartet<W, A, B, C, D> filterNotSecond(Quartet<W, A, B, C, D> container,
                                                                Fn1<? super B, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                                Predicate<? super B> condition) {
        return filterSecond(container,
                            ifNotFitsCondition,
                            Objects.requireNonNull(condition,
                                                   () -> "condition")
                                   .negate());
    }

    <A, B, C, D> Quartet<W, A, B, C, D> filterThird(Quartet<W, A, B, C, D> container,
                                                    Fn1<? super C, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                    Predicate<? super C> condition);

    default <A, B, C, D> Quartet<W, A, B, C, D> filterNotThird(Quartet<W, A, B, C, D> container,
                                                               Fn1<? super C, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                               Predicate<? super C> condition) {
        return filterThird(container,
                           ifNotFitsCondition,
                           Objects.requireNonNull(condition,
                                                  () -> "condition")
                                  .negate());
    }

    <A, B, C, D> Quartet<W, A, B, C, D> filterFourth(Quartet<W, A, B, C, D> container,
                                                     Fn1<? super D, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                     Predicate<? super D> condition);

    default <A, B, C, D> Quartet<W, A, B, C, D> filterNotFourth(Quartet<W, A, B, C, D> container,
                                                                Fn1<? super D, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                                Predicate<? super D> condition) {
        return filterFourth(container,
                            ifNotFitsCondition,
                            Objects.requireNonNull(condition,
                                                   () -> "condition")
                                   .negate());
    }

}
