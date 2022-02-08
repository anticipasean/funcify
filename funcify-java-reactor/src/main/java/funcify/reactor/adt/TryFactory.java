package funcify.reactor.adt;

import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.function.Function;

/**
 * Builds the components needed for the Try interface but should not be used separately
 *
 * @author smccarron
 * @created 2021-08-05
 */
class TryFactory {

    @AllArgsConstructor(staticName = "of")
    static class Success<S> implements Try<S> {

        private final S successObject;

        @Override
        public <R> R fold(final Function<? super S, ? extends R> successHandler,
                          final Function<Throwable, ? extends R> failureHandler) {
            Objects.requireNonNull(failureHandler,
                                   () -> "failureHandler");
            return Objects.requireNonNull(successHandler,
                                          () -> "successHandler")
                          .apply(successObject);
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class Failure<S> implements Try<S> {

        private final Throwable throwable;


        @Override
        public <R> R fold(final Function<? super S, ? extends R> successHandler,
                          final Function<Throwable, ? extends R> failureHandler) {
            Objects.requireNonNull(successHandler,
                                   () -> "successHandler");
            return Objects.requireNonNull(failureHandler,
                                          () -> "failureHandler")
                          .apply(throwable);
        }
    }

}
