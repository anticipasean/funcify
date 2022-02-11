package funcify.writer;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-08-29
 */
public interface WriteResult<V> {

    static <V> WriteResult<V> success(final V success) {
        return WriteResultFactory.SuccessWriteResult.of(success);
    }

    static <V> WriteResult<V> failure(final Throwable failure) {
        return WriteResultFactory.FailureWriteResult.of(failure);
    }

    default Optional<V> getSuccessValue() {
        return unwrap(Optional::of,
                      t -> Optional.empty());
    }

    default Optional<Throwable> getFailureValue() {
        return unwrap(s -> Optional.empty(),
                      Optional::of);
    }

    default boolean isSuccess() {
        return unwrap(s -> true,
                      t -> false);
    }

    default boolean isFailure() {
        return unwrap(s -> false,
                      t -> true);
    }

    <R> R unwrap(final Function<? super V, ? extends R> ifSuccess,
                 final Function<? super Throwable, ? extends R> ifFailure);

}
