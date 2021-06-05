package funcify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import funcify.tool.SyncMap;
import org.junit.jupiter.api.Test;

/**
 * @author smccarron
 * @created 2021-06-03
 */
public class SyncMapTest {

    @Test
    public void serializeSyncMapTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        String output = "";
        try {
            output = objectMapper.writeValueAsString(SyncMap.of("one",
                                                                1)
                                                            .put("two",
                                                                 2));
        } catch (JsonProcessingException e) {
            fail("error: " + e.getMessage());
        }
        assertFalse(output::isEmpty);
//        System.out.println(output);
    }

    @Test
    public void deserializeSyncMapTest() {
        String input = "{\"one\":1,\"two\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        SyncMap<String, Integer> syncMapOutput = SyncMap.empty();
        try {
            syncMapOutput = objectMapper.readValue(input,
                                                   TypeFactory.defaultInstance()
                                                              .constructParametricType(SyncMap.class,
                                                                                       TypeFactory.defaultInstance()
                                                                                                  .constructType(String.class),
                                                                                       TypeFactory.defaultInstance()
                                                                                                  .constructType(Integer.class)));
        } catch (JsonProcessingException e) {
            fail("error: " + e.getMessage());
        }
        assertEquals(syncMapOutput.getOrElse("one",
                                             -1),
                     1);
//        System.out.println(syncMapOutput.toString());
    }

}
