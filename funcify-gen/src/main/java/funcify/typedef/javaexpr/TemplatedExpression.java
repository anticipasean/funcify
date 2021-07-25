package funcify.typedef.javaexpr;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-25
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
public class TemplatedExpression implements JavaExpression {

    @JsonProperty("template_function")
    private String templateFunction;

    @JsonProperty("input")
    private SyncMap<String, Object> input;

}
