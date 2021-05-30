package funcify.typedef.javaexpr;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.JavaParameter;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-22
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
public class LambdaExpression implements JavaExpression {

    @JsonProperty("is_lambda")
    private final boolean lambda = true;

    @JsonProperty("parameters")
    private List<JavaParameter> parameters;

    @JsonProperty("lambda_body")
    private String lambdaBody;

}
