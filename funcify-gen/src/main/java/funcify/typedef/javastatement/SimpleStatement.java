package funcify.typedef.javastatement;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.javaexpr.JavaExpression;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-29
 */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
public class SimpleStatement implements JavaStatement {

    @JsonProperty("annotations")
    @Default
    private SyncList<JavaAnnotation> annotations = SyncList.empty();

    @Default
    @JsonProperty("expressions")
    private SyncList<JavaExpression> expressions = SyncList.empty();

}
