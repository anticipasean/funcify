package funcify.testing;

import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-09-06
 */
public class NonZeroExitStatusCheckingTransaction implements AutoCloseable {

    private final SecurityManager currentSecurityManager;

    private NonZeroExitStatusCheckingTransaction() {
        this.currentSecurityManager = System.getSecurityManager();
        System.setSecurityManager(NonZeroExitStatusValidator.of(Optional.ofNullable(this.currentSecurityManager)));
    }

    public static NonZeroExitStatusCheckingTransaction newInstance() {
        return new NonZeroExitStatusCheckingTransaction();
    }

    @Override
    public void close() throws Exception {
        try {
            System.setSecurityManager(this.currentSecurityManager);
        } catch (final Throwable t) {
            if (t instanceof RuntimeException) {
                throw t;
            } else {
                throw new RuntimeException(t.getMessage(),
                                           t);
            }
        }
    }
}
