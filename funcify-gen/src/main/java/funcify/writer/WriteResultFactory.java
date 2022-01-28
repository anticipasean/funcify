package funcify.writer;

import java.util.Objects;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author smccarron
 * @created 2021-08-29
 */
class WriteResultFactory {

    @AllArgsConstructor(access = AccessLevel.PACKAGE,
                        staticName = "of")
    static class SuccessWriteResult<S> implements WriteResult<S> {

        @NonNull
        private final S successValue;

        @Override
        public <R> R unwrap(final Function<? super S, ? extends R> ifSuccess,
                            final Function<? super Throwable, ? extends R> ifFailure) {
            Objects.requireNonNull(ifFailure,
                                   () -> "ifFailure");
            return Objects.requireNonNull(ifSuccess,
                                          () -> "ifSuccess")
                          .apply(successValue);
        }
    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE,
                        staticName = "of")
    static class FailureWriteResult<S> implements WriteResult<S> {

        @NonNull
        private final Throwable failureValue;

        @Override
        public <R> R unwrap(final Function<? super S, ? extends R> ifSuccess,
                            final Function<? super Throwable, ? extends R> ifFailure) {
            Objects.requireNonNull(ifSuccess,
                                   () -> "ifSuccess");
            return Objects.requireNonNull(ifFailure,
                                          () -> "ifFailure")
                          .apply(failureValue);
        }

    }

}
