package funcify.attempt;

import funcify.attempt.Try.TryW;
import funcify.attempt.factory.TryFactory;
import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.flattenable.FlattenableDisjunctDuet;
import funcify.flattenable.FlattenableDuet;
import funcify.function.Fn0.ErrableFn0;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface Try<A, T extends Throwable> extends FlattenableDisjunctDuet<TryW, A, T>, Iterable<A> {

    static enum TryW {

    }

    static <A, T extends Throwable> Try<A, T> narrowK(Solo<Solo<TryW, A>, T> soloInstance) {
        return (Try<A, T>) soloInstance;
    }

    static <A, T extends Throwable> Try<A, T> narrowK(Duet<TryW, A, T> duetInstance) {
        return (Try<A, T>) duetInstance;
    }

    static <A> Try<A, Throwable> of(final ErrableFn0<A, Throwable> errableOperation) {
        return TryFactory.getInstance()
                         .catching(errableOperation)
                         .narrowT1();
    }

    @SafeVarargs
    static <A, T extends Throwable> Try<A, T> of(final ErrableFn0<A, T> errableOperation,
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

    default <C> Try<C, T> flatMap(final Function<? super A, ? extends FlattenableDisjunctDuet<TryW, C, T>> flatMapper) {
        return flatMapSuccess(flatMapper);
    }

    default <C> Try<C, T> flatMapSuccess(final Function<? super A, ? extends FlattenableDuet<TryW, C, T>> flatMapper) {
        return flatMapFirst(flatMapper).narrowT1();
    }

    static <A, E extends Throwable> Try<A, E> flatten(Try<Try<A, E>, E> attempt) {
        // Widen attempt to its parent Duet type, flatten, and then narrow type to Try again
        return TryFactory.getInstance()
                         .flattenFirst(Duet.widenP(attempt))
                         .narrowT1();
    }

    default <C extends Throwable> Try<A, C> flatMapFailure(final Function<? super T, ? extends FlattenableDuet<TryW, A, C>> flatMapper) {
        return flatMapSecond(flatMapper).narrowT1();
    }

    default <C> Try<C, T> mapSuccess(final Function<? super A, ? extends C> mapper) {
        return mapFirst(mapper).narrowT2();
    }


    default <C extends Throwable> Try<A, C> mapFailure(final Function<? super T, ? extends C> mapper) {
        return mapSecond(mapper).narrowT1();
    }

    default <C> Try<C, T> zipSuccess(final FlattenableDuet<TryW, A, T> container,
                                     final BiFunction<? super A, ? super A, ? extends C> combiner) {
        return zipFirst(container,
                        combiner).narrowT2();
    }

    default <C extends Throwable> Try<A, C> zipFailure(final FlattenableDuet<TryW, A, T> container,
                                                       final BiFunction<? super T, ? super T, ? extends C> combiner) {
        return zipSecond(container,
                         combiner).narrowT1();
    }

    default <B, C extends Throwable> Try<B, C> bimap(final Function<? super A, ? extends B> mapper1,
                                                     final Function<? super T, ? extends C> mapper2) {
        return factory().bimap(this,
                               mapper1,
                               mapper2)
                        .narrowT2();
    }

    @Override
    default Iterator<A> iterator() {
        return factory().firstIterator(this);
    }
}
