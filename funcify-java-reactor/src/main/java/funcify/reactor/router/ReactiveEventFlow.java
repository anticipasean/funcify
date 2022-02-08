package funcify.reactor.router;

/**
 * @author smccarron
 * @created 2/6/22
 */
public interface ReactiveEventFlow<E> extends ReactiveTransformer<E, E> {


    public static class UnhandledEventConditionException extends IllegalStateException {

        public UnhandledEventConditionException(final String message) {
            super(message);
        }

        public UnhandledEventConditionException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public UnhandledEventConditionException(final Throwable cause) {
            super(cause);
        }
    }

}

