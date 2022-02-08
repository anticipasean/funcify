package funcify.reactor.error;

/**
 * @author smccarron
 * @created 2/6/22
 */
public class FuncifyReactorException extends RuntimeException {

    public FuncifyReactorException() {
    }

    public FuncifyReactorException(final String message) {
        super(message);
    }

    public FuncifyReactorException(final String message,
                                   final Throwable cause) {
        super(message, cause);
    }

    public FuncifyReactorException(final Throwable cause) {
        super(cause);
    }

    public FuncifyReactorException(final String message,
                                   final Throwable cause,
                                   final boolean enableSuppression,
                                   final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
