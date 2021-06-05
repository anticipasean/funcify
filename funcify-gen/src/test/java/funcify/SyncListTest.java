package funcify;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import funcify.tool.SyncList;
import org.junit.jupiter.api.Test;

/**
 * @author smccarron
 * @created 2021-06-02
 */
public class SyncListTest {

    @Test
    public void serializeSyncListTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        String output = "";
        try {
            output = objectMapper.writeValueAsString(SyncList.of(1,
                                                                 2,
                                                                 3));
        } catch (JsonProcessingException e) {
            fail("error: " + e.getMessage());
        }
        assertFalse(output::isEmpty);
        System.out.println(output);
    }

    @Test
    public void deserializeSyncListTest() {
        String input = "[1,2,3]";
        ObjectMapper objectMapper = new ObjectMapper();
        SyncList<Integer> syncListOutput = SyncList.empty();
        try {
            syncListOutput = objectMapper.readValue(input,
                                   TypeFactory.defaultInstance()
                                              .constructParametricType(SyncList.class,
                                                                       TypeFactory.defaultInstance()
                                                                                  .constructType(Integer.class)));
        } catch (JsonProcessingException e) {
            fail("error: " + e.getMessage());
        }
        System.out.println(syncListOutput.toString());
    }

}
