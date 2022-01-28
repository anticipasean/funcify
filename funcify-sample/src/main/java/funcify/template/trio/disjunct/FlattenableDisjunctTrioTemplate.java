package funcify.template.trio.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Trio;
import funcify.function.Fn1;
import funcify.template.trio.FlattenableTrioTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FlattenableDisjunctTrioTemplate<W> extends FlattenableTrioTemplate<W>, IterableDisjunctTrioTemplate<W>,
                                                            FilterableDisjunctTrioTemplate<W> {

    @Override
    default <A, B, C, D> Trio<W, D, B, C> flatMapFirst(final Trio<W, A, B, C> container,
                                                       final Fn1<? super A, ? extends Trio<W, D, B, C>> flatMapper) {
        return fold(container,
                    (A a) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(a),
                    (B b) -> this.<D, B, C>second(b),
                    (C c) -> this.<D, B, C>third(c));
    }

    @Override
    default <A, B, C, D> Trio<W, A, D, C> flatMapSecond(final Trio<W, A, B, C> container,
                                                        final Fn1<? super B, ? extends Trio<W, A, D, C>> flatMapper) {
        return fold(container,
                    (A a) -> this.<A, D, C>first(a),
                    (B b) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(b),
                    (C c) -> this.<A, D, C>third(c));
    }

    @Override
    default <A, B, C, D> Trio<W, A, B, D> flatMapThird(final Trio<W, A, B, C> container,
                                                       final Fn1<? super C, ? extends Trio<W, A, B, D>> flatMapper) {
        return fold(container,
                    (A a) -> this.<A, B, D>first(a),
                    (B b) -> this.<A, B, D>second(b),
                    (C c) -> requireNonNull(flatMapper,
                                            () -> "flatMapper").apply(c));
    }

    @Override
    default <A, B, C, D> Trio<W, D, B, C> mapFirst(final Trio<W, A, B, C> container,
                                                   final Fn1<? super A, ? extends D> mapper) {
        return flatMapFirst(container,
                            requireNonNull(mapper,
                                           () -> "mapper").andThen(this::<D, B, C>first));
    }

    @Override
    default <A, B, C, D> Trio<W, A, D, C> mapSecond(final Trio<W, A, B, C> container,
                                                    final Fn1<? super B, ? extends D> mapper) {
        return flatMapSecond(container,
                             requireNonNull(mapper,
                                            () -> "mapper").andThen(this::<A, D, C>second));
    }

    @Override
    default <A, B, C, D> Trio<W, A, B, D> mapThird(final Trio<W, A, B, C> container,
                                                   final Fn1<? super C, ? extends D> mapper) {
        return flatMapThird(container,
                            requireNonNull(mapper,
                                           () -> "mapper").andThen(this::<A, B, D>third));
    }

    @Override
    default <A, B, C> Trio<W, A, B, C> filterFirst(Trio<W, A, B, C> container,
                                                   Fn1<? super A, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                   Predicate<? super A> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapFirst(container,
                            (A a) -> {
                                return condition.test(a) ? this.<A, B, C>first(a) : ifNotFitsCondition.apply(a);
                            });
    }

    @Override
    default <A, B, C> Trio<W, A, B, C> filterSecond(Trio<W, A, B, C> container,
                                                    Fn1<? super B, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                    Predicate<? super B> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapSecond(container,
                             (B b) -> {
                                 return condition.test(b) ? this.<A, B, C>second(b) : ifNotFitsCondition.apply(b);
                             });
    }

    @Override
    default <A, B, C> Trio<W, A, B, C> filterThird(Trio<W, A, B, C> container,
                                                   Fn1<? super C, ? extends Trio<W, A, B, C>> ifNotFitsCondition,
                                                   Predicate<? super C> condition) {
        requireNonNull(condition,
                       () -> "condition");
        requireNonNull(ifNotFitsCondition,
                       () -> "ifNotFitsCondition");
        return flatMapThird(container,
                            (C c) -> {
                                return condition.test(c) ? this.<A, B, C>third(c) : ifNotFitsCondition.apply(c);
                            });
    }
}
