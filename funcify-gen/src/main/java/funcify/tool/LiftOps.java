package funcify.tool;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-06-03
 */
public interface LiftOps {

    static <A> Optional<A> tryCatchLift(final Callable<A> callable) {
        try {
            return Optional.ofNullable(requireNonNull(callable,
                                                      () -> "callable").call());
        } catch (final Throwable t) {
            return Optional.empty();
        }
    }

    static <A> void tryCatchLift(final Runnable runnable) {
        try {
            requireNonNull(runnable,
                           () -> "runnable").run();
        } catch (final Throwable t) {
            //noop
        }
    }

    static <A, B> Function<A, Optional<B>> tryCatchLift(final Function<? super A, ? extends B> mapper) {
        return (A a) -> {
            try {
                return Optional.ofNullable(requireNonNull(mapper,
                                                          () -> "mapper").apply(a));
            } catch (final Throwable t) {
                return Optional.empty();
            }
        };
    }

    static <A, B, C> BiFunction<A, B, Optional<C>> tryCatchLift(final BiFunction<? super A, ? super B, ? extends C> mapper) {
        return (A a, B b) -> {
            try {
                return Optional.ofNullable(requireNonNull(mapper,
                                                          () -> "mapper").apply(a,
                                                                                b));
            } catch (final Throwable t) {
                return Optional.empty();
            }
        };
    }

    static <A> Predicate<A> tryCatchLift(final Predicate<? super A> condition) {
        return (A a) -> {
            try {
                return requireNonNull(condition,
                                      () -> "condition").test(a);
            } catch (final Throwable t) {
                return false;
            }
        };
    }
}
