package funcify;

import funcify.testing.NonZeroExitStatusCheckingTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * @author smccarron
 * @created 2021-05-25
 */
public class FuncifyClassGeneratorTest {


    @Test
    public void generateTypesToConsoleTest() {
        try {
            try (NonZeroExitStatusCheckingTransaction trans = NonZeroExitStatusCheckingTransaction.newInstance()) {
                FuncifyClassGenerator.main(new String[]{"-p"});
            }
        } catch (final Throwable t) {
            Assertions.fail(t);
        }
    }

}
