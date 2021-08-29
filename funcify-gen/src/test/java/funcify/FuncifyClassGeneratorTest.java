package funcify;

import org.junit.jupiter.api.Test;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public class FuncifyClassGeneratorTest {

    @Test
    public void generateTypesToConsoleTest() {
        FuncifyClassGenerator.main(new String[]{"-p"});
    }

}
