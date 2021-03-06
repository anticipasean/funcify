package funcify.attempt;

import funcify.attempt.Try.TryW;
import funcify.attempt.factory.TryFactory;
import funcify.design.duet.FlattenableDuet;
import funcify.design.duet.disjunct.FlattenableDisjunctDuet;
import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.function.Fn0;
import funcify.function.Fn0.ErrableFn0;
import funcify.function.Fn1;
import funcify.function.Fn1.ErrableFn1;
import funcify.function.Fn2;
import java.util.Iterator;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface Try<S> extends FlattenableDisjunctDuet<TryW, S, Throwable>, Iterable<S> {

    static enum TryW {

    }

    static <S, F extends Throwable> Try<S> narrowK(final Solo<Solo<TryW, S>, F> soloInstance) {
        return (Try<S>) soloInstance;
    }

    static <S, F extends Throwable> Try<S> narrowK(final Duet<TryW, S, F> duetInstance) {
        return (Try<S>) duetInstance;
    }

    static <S> Try<S> of(final Fn0<? extends S> operation) {
        return TryFactory.getInstance()
                         .catching(operation)
                         .narrowT2();
    }

    static <S, T extends Throwable> Try<S> of(final ErrableFn0<? extends S, T> errableOperation) {
        return TryFactory.getInstance()
                         .catching(errableOperation)
                         .narrowT2();
    }

    @SafeVarargs
    static <S, T extends Throwable> Try<S> of(final ErrableFn0<S, T> errableOperation,
                                              final Class<? extends Throwable>... allowedErrorTypes) {
        return TryFactory.getInstance()
                         .catching(errableOperation,
                                   allowedErrorTypes)
                         .narrowT1();
    }

    @Override
    default TryFactory factory() {
        return TryFactory.getInstance();
    }

    static <S> Try<S> success(final S value) {
        return TryFactory.getInstance().<S, Throwable>first(value).narrowT1();
    }

    static <S> Try<S> failure(final Throwable throwable) {
        return TryFactory.getInstance().<S, Throwable>second(throwable).narrowT1();
    }

    default <B> Try<B> mapCatchingOnly(final Fn1<? super S, ? extends B> mapper,
                                       final Class<? extends Throwable> allowedErrorType) {
        return factory().mapCatchingOnly(this,
                                         mapper,
                                         allowedErrorType)
                        .narrowT2();
    }

    default <B> Try<B> flatMapCatchingOnly(final Fn1<? super S, ? extends Try<? extends B>> flatMapper,
                                           final Class<? extends Throwable> allowedErrorType) {
        return factory().flatMapCatchingOnly(this,
                                             flatMapper,
                                             allowedErrorType)
                        .narrowT1();
    }

    default <B, T extends Throwable> Try<B> checkedMap(final ErrableFn1<? super S, ? extends B, T> mapper) {
        return factory().checkedMap(this,
                                    mapper)
                        .narrowT2();
    }

    default <B, T extends Throwable> Try<B> checkedFlatMap(final ErrableFn1<? super S, ? extends Try<? extends B>, T> flatMapper) {
        return factory().checkedFlatMap(this,
                                        flatMapper)
                        .narrowT1();
    }

    default <B> Try<B> flatMapSuccess(final Fn1<? super S, ? extends FlattenableDuet<TryW, B, Throwable>> flatMapper) {
        return flatMapFirst(flatMapper).narrowT1();
    }

    static <S> Try<S> flatten(Try<Try<S>> attempt) {
        // Widen attempt to its parent Duet type, flatten, and then narrow type to Try again
        return TryFactory.getInstance()
                         .flattenFirst(Duet.widenP(attempt))
                         .narrowT1();
    }

    default <F extends Throwable> Try<S> flatMapFailure(final Fn1<? super Throwable, ? extends FlattenableDuet<TryW, S, F>> flatMapper) {
        return flatMapSecond(flatMapper).narrowT1();
    }

    default <B> Try<B> mapSuccess(final Fn1<? super S, ? extends B> mapper) {
        return mapFirst(mapper).narrowT2();
    }


    default <F extends Throwable> Try<S> mapFailure(final Fn1<? super Throwable, ? extends F> mapper) {
        return mapSecond(mapper).narrowT1();
    }

    default <C, F extends Throwable> Try<C> zipSuccess(final Try<S> container,
                                                       final Fn2<? super S, ? super S, ? extends C> combiner) {
        return zipFirst(container,
                        combiner).narrowT2();
    }

    default <F extends Throwable> Try<S> zipFailure(final Try<S> container,
                                                    final Fn2<? super Throwable, ? super Throwable, ? extends F> combiner) {
        return zipSecond(container,
                         combiner).narrowT1();
    }


    default <B, F extends Throwable> Try<B> bimap(final Fn1<? super S, ? extends B> mapper1,
                                                  final Fn1<? super Throwable, ? extends F> mapper2) {
        return factory().<S, Throwable, B, Throwable>bimap(this,
                                                           mapper1,
                                                           mapper2).narrowT2();
    }

    @Override
    default Iterator<S> iterator() {
        return factory().iteratorForFirst(this);
    }
}
