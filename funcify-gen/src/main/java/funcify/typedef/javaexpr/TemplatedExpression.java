package funcify.typedef.javaexpr;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.SyncList;
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

    @JsonProperty("is_lambda")
    private final boolean lambda = false;

    @JsonProperty("template_call")
    private String templateCall;

    @JsonProperty("template_parameters")
    private SyncList<String> templateParameters;

}
