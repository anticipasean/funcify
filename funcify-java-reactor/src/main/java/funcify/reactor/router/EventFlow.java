package funcify.reactor.router;

import funcify.reactor.adt.Try;

import java.util.function.Function;

/**
 * Function built through a factory, {@link EventFlowFactory}, that routes a received event to one or more subscribers and/or
 * consumers depending on the conditions entered
 *
 * @param <E> - Event Type
 * @author smccarron
 * @created 2021-08-18
 */
public interface EventFlow<E> extends Function<E, Try<EventFlow.SubmissionStatus>> {

    static enum SubmissionStatus {
        /**
         * Indicates the input event was able to be "published" or "accepted" by subscriber(s) and/or consumer(s) to which it was
         * routed
         */
        SUBMITTED,
        /**
         * Indicates an error occurred when attempting to publish or submit the input event to one or more of the publishers
         * <p></p>
         * When such an error occurs, an attempt is made to publish the error to subscriber(s) and/or error consumer(s) to which
         * the event was to be routed
         */
        ERROR_DURING_SUBMISSION,
        /**
         * Indicates the input event has yet to meet one of the event and/or contextual conditions required for routing to a given
         * set of subscribers and/or consumers
         */
        NOT_YET_SUBMITTED
    }

    /**
     * @param event - input event with parent type {@code <PE>}
     * @return {@link Try#success} of the {@link SubmissionStatus} if {@link SubmissionStatus#SUBMITTED} or {@link
     * SubmissionStatus#ERROR_DURING_SUBMISSION}, or else, {@link Try#failure} of an error that should ( or could ) not be passed
     * to the subscribers and/or error consumers e.g. {@link SubmissionStatus#NOT_YET_SUBMITTED} -> {@link
     * UnhandledEventConditionException}
     */
    @Override
    Try<SubmissionStatus> apply(E event);

    public static class UnhandledEventConditionException extends IllegalStateException {

        public UnhandledEventConditionException(final String message) {
            super(message);
        }

        public UnhandledEventConditionException(final String message,
                                                final Throwable cause) {
            super(message, cause);
        }

        public UnhandledEventConditionException(final Throwable cause) {
            super(cause);
        }
    }
}
