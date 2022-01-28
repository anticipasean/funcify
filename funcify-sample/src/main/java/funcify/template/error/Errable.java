package funcify.template.error;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface Errable {

    static RuntimeException throwUncheckedThrowable(final Throwable throwable) {
        throw Errable.<RuntimeException>uncheckThrowable(throwable);
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> T uncheckThrowable(final Throwable throwable) throws T {
        throw (T) throwable;
    }
}
