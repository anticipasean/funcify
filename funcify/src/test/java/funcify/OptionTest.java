package funcify;


import funcify.option.Option;
import org.junit.Test;

/**
 * @author smccarron
 * @created 2021-04-28
 */
class OptionTest {

    @Test
    void flatMapTest() {
        Option.of("Hello").map(s -> s.substring(1));

    }
}