package funcify.template.quartet.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Quartet;
import funcify.function.Fn1;
import funcify.template.quartet.FlattenableQuartetTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FlattenableDisjunctQuartetTemplate<W> extends FlattenableQuartetTemplate<W>, IterableDisjunctQuartetTemplate<W>,
                                                               FilterableDisjunctQuartetTemplate<W> {


    @Override
    default <A, B, C, D, E> Quartet<W, E, B, C, D> flatMapFirst(final Quartet<W, A, B, C, D> container,
                                                                final Fn1<? super A, ? extends Quartet<W, E, B, C, D>> flatMapper) {
        return fold(container,
                    (A a) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(a),
                    (B b) -> this.<E, B, C, D>second(b),
                    (C c) -> this.<E, B, C, D>third(c),
                    (D d) -> this.<E, B, C, D>fourth(d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, E, C, D> flatMapSecond(final Quartet<W, A, B, C, D> container,
                                                                 final Fn1<? super B, ? extends Quartet<W, A, E, C, D>> flatMapper) {
        return fold(container,
                    (A a) -> this.<A, E, C, D>first(a),
                    (B b) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(b),
                    (C c) -> this.<A, E, C, D>third(c),
                    (D d) -> this.<A, E, C, D>fourth(d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, E, D> flatMapThird(final Quartet<W, A, B, C, D> container,
                                                                final Fn1<? super C, ? extends Quartet<W, A, B, E, D>> flatMapper) {
        return fold(container,
                    (A a) -> this.<A, B, E, D>first(a),
                    (B b) -> this.<A, B, E, D>second(b),
                    (C c) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(c),
                    (D d) -> this.<A, B, E, D>fourth(d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, C, E> flatMapFourth(final Quartet<W, A, B, C, D> container,
                                                                 final Fn1<? super D, ? extends Quartet<W, A, B, C, E>> flatMapper) {
        return fold(container,
                    (A a) -> this.<A, B, C, E>first(a),
                    (B b) -> this.<A, B, C, E>second(b),
                    (C c) -> this.<A, B, C, E>third(c),
                    (D d) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, E, B, C, D> mapFirst(final Quartet<W, A, B, C, D> container,
                                                            final Fn1<? super A, ? extends E> mapper) {
        return flatMapFirst(container,
                            requireNonNull(mapper,
                                           () -> "mapper").andThen(this::<E, B, C, D>first));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, E, C, D> mapSecond(final Quartet<W, A, B, C, D> container,
                                                             final Fn1<? super B, ? extends E> mapper) {
        return flatMapSecond(container,
                             requireNonNull(mapper,
                                            () -> "mapper").andThen(this::<A, E, C, D>second));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, E, D> mapThird(final Quartet<W, A, B, C, D> container,
                                                            final Fn1<? super C, ? extends E> mapper) {
        return flatMapThird(container,
                            requireNonNull(mapper,
                                           () -> "mapper").andThen(this::<A, B, E, D>third));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, C, E> mapFourth(final Quartet<W, A, B, C, D> container,
                                                             final Fn1<? super D, ? extends E> mapper) {
        return flatMapFourth(container,
                             requireNonNull(mapper,
                                            () -> "mapper").andThen(this::<A, B, C, E>fourth));
    }


    @Override
    default <A, B, C, D> Quartet<W, A, B, C, D> filterFirst(Quartet<W, A, B, C, D> container,
                                                            Fn1<? super A, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                            Predicate<? super A> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapFirst(container,
                            (A a) -> {
                                return condition.test(a) ? this.<A, B, C, D>first(a) : ifNotFitsCondition.apply(a);
                            });
    }

    @Override
    default <A, B, C, D> Quartet<W, A, B, C, D> filterSecond(Quartet<W, A, B, C, D> container,
                                                             Fn1<? super B, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                             Predicate<? super B> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapSecond(container,
                             (B b) -> {
                                 return condition.test(b) ? this.<A, B, C, D>second(b) : ifNotFitsCondition.apply(b);
                             });
    }

    @Override
    default <A, B, C, D> Quartet<W, A, B, C, D> filterThird(Quartet<W, A, B, C, D> container,
                                                            Fn1<? super C, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                            Predicate<? super C> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapThird(container,
                            (C c) -> {
                                return condition.test(c) ? this.<A, B, C, D>third(c) : ifNotFitsCondition.apply(c);
                            });
    }

    @Override
    default <A, B, C, D> Quartet<W, A, B, C, D> filterFourth(Quartet<W, A, B, C, D> container,
                                                             Fn1<? super D, ? extends Quartet<W, A, B, C, D>> ifNotFitsCondition,
                                                             Predicate<? super D> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapFourth(container,
                             (D d) -> {
                                 return condition.test(d) ? this.<A, B, C, D>fourth(d) : ifNotFitsCondition.apply(d);
                             });
    }
}
