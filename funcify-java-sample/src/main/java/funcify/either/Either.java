package funcify.either;

import funcify.design.duet.FilterableDuet;
import funcify.design.duet.disjunct.FlattenableDisjunctDuet;
import funcify.either.Either.EitherW;
import funcify.either.factory.EitherFactory;
import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.function.Fn1;
import funcify.option.Option;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface Either<L, R> extends FlattenableDisjunctDuet<EitherW, L, R>, Iterable<R> {

    static enum EitherW {

    }

    static <L, R> Either<L, R> narrowK(Duet<EitherW, L, R> duetInstance) {
        return (Either<L, R>) duetInstance;
    }

    static <L, R> Either<L, R> narrowK(Solo<Solo<EitherW, L>, R> duetInstance) {
        return (Either<L, R>) duetInstance;
    }

    static <L, R> Either<L, R> left(final L leftValue) {
        return EitherFactory.getInstance().<L, R>first(leftValue).narrowT1();
    }

    static <L, R> Either<L, R> right(final R rightValue) {
        return EitherFactory.getInstance().<L, R>second(rightValue).narrowT1();
    }

    static <L, R> Either<L, R> left(final L leftValue,
                                    final Class<R> rightType) {
        return EitherFactory.getInstance().<L, R>first(leftValue).narrowT1();
    }

    static <L, R> Either<L, R> right(final Class<L> leftType,
                                     final R rightValue) {
        return EitherFactory.getInstance().<L, R>second(rightValue).narrowT1();
    }

    static <I extends Iterable<? extends L>, L, R> Either<L, R> ofLeftIterable(final I iterable,
                                                                               final R defaultRight) {
        return EitherFactory.getInstance().<I, L, R>firstFromIterable(iterable,
                                                                      defaultRight).narrowT1();
    }

    static <I extends Iterable<? extends R>, L, R> Either<L, R> ofRightIterable(final I iterable,
                                                                                final L defaultLeft) {
        return EitherFactory.getInstance().<I, L, R>secondFromIterable(iterable,
                                                                       defaultLeft).narrowT1();
    }


    @Override
    default EitherFactory factory() {
        return EitherFactory.getInstance();
    }


    default <C, D> Either<C, D> leftFromIterable(final Iterable<? extends C> iterable,
                                                 final D defaultRight) {
        return FlattenableDisjunctDuet.super.<C, D>firstFromIterable(iterable,
                                                                     defaultRight).narrowT1();
    }

    default <C, D> Either<C, D> rightFromIterable(final Iterable<? extends D> iterable,
                                                  final C defaultLeft) {
        return FlattenableDisjunctDuet.super.<C, D>secondFromIterable(iterable,
                                                                      defaultLeft).narrowT1();
    }

    default <C> Either<C, R> mapLeft(final Function<? super L, ? extends C> mapper) {
        return factory().mapFirst(this,
                                  mapper)
                        .narrowT2();
    }


    default <C> Either<L, C> mapRight(final Function<? super R, ? extends C> mapper) {
        return factory().mapSecond(this,
                                   mapper)
                        .narrowT2();
    }


    default <A, C> Either<C, R> zipLeft(final Either<A, R> container,
                                        final BiFunction<? super L, ? super A, ? extends C> combiner) {
        return factory().zipFirst(this,
                                  container,
                                  combiner)
                        .narrowT2();
    }


    default <A, C> Either<L, C> zipRight(final Either<L, A> container,
                                         final BiFunction<? super R, ? super A, ? extends C> combiner) {
        return factory().zipSecond(this,
                                   container,
                                   combiner)
                        .narrowT2();
    }


    default <C> Either<C, R> flatMapLeft(final Function<? super L, ? extends Either<C, R>> flatMapper) {
        return factory().flatMapFirst(this,
                                      flatMapper)
                        .narrowT2();
    }


    default <C> Either<L, C> flatMapRight(final Function<? super R, ? extends Either<L, C>> flatMapper) {
        return factory().flatMapSecond(this,
                                       flatMapper)
                        .narrowT2();
    }

    default Either<L, R> flatMapLeftToRight(final Function<? super L, ? extends Either<L, R>> flatMapper) {
        return factory().flatMapFirstToSecond(this,
                                              flatMapper)
                        .narrowT2();
    }

    default Either<L, R> flatMapRightToLeft(final Function<? super R, ? extends Either<L, R>> flatMapper) {
        return factory().flatMapSecondToFirst(this,
                                              flatMapper)
                        .narrowT2();
    }


    @Override
    default Iterator<R> iterator() {
        return factory().iteratorForSecond(this);
    }

    default L orElseLeft(final L alternative) {
        return orElseFirst(alternative);
    }

    default R orElseRight(final R alternative) {
        return orElseSecond(alternative);
    }

    default L orElseGetLeft(final Supplier<L> alternativeSupplier) {
        return orElseGetFirst(alternativeSupplier);
    }

    default R orElseGetRight(final Supplier<R> alternativeSupplier) {
        return orElseGetSecond(alternativeSupplier);
    }

    default Option<L> getLeft() {
        return getFirst();
    }

    default Option<R> getRight() {
        return getSecond();
    }


    default Option<L> filterLeft(final Predicate<? super L> condition) {
        return getLeft().filter(condition);
    }


    default Either<L, R> filterLeft(final Predicate<? super L> condition,
                                    final Fn1<? super L, ? extends FilterableDuet<EitherW, L, R>> ifNotFitsCondition) {
        return factory().filterFirst(this,
                                     ifNotFitsCondition,
                                     condition)
                        .narrowT1();
    }


    default Option<R> filterRight(final Predicate<? super R> condition) {
        return getRight().filter(condition);
    }


    default Either<L, R> filterRight(final Predicate<? super R> condition,
                                     final Fn1<? super R, ? extends FilterableDuet<EitherW, L, R>> ifNotFitsCondition) {
        return factory().filterSecond(this,
                                      ifNotFitsCondition,
                                      condition)
                        .narrowT1();
    }

    @Override
    default Either<L, R> bipeek(final Consumer<? super L> consumer1,
                                final Consumer<? super R> consumer2) {
        return FlattenableDisjunctDuet.super.bipeek(consumer1,
                                                    consumer2)
                                            .narrowT1();
    }

    default Either<L, R> peekLeft(final Consumer<? super L> consumer) {
        return FlattenableDisjunctDuet.super.peekFirst(consumer)
                                            .narrowT1();
    }

    default Either<L, R> peekRight(final Consumer<? super R> consumer) {
        return FlattenableDisjunctDuet.super.peekSecond(consumer)
                                            .narrowT1();
    }
}
