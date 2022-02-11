package funcify.error;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public class FuncifyCodeGenException extends RuntimeException {

    public FuncifyCodeGenException() {
        super();
    }

    public FuncifyCodeGenException(final String message) {
        super(message);
    }

    public FuncifyCodeGenException(final String message,
                                   final Throwable cause) {
        super(message,
              cause);
    }

    public FuncifyCodeGenException(final Throwable cause) {
        super(cause);
    }

    protected FuncifyCodeGenException(final String message,
                                      final Throwable cause,
                                      final boolean enableSuppression,
                                      final boolean writableStackTrace) {
        super(message,
              cause,
              enableSuppression,
              writableStackTrace);
    }
}
