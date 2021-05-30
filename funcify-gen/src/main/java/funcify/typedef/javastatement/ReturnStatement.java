package funcify.typedef.javastatement;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.javaexpr.JavaExpression;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
public class ReturnStatement implements JavaStatement {

    @JsonProperty("is_return")
    private final boolean returnStatement = true;

    @Default
    @JsonProperty("expressions")
    private List<JavaExpression> expressions = new ArrayList<>();

}
