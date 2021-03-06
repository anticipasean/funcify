package funcify.typedef.javastatement;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javatype.JavaType;
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
public class AssignmentStatement implements JavaStatement {

    @JsonProperty("annotations")
    @Default
    private SyncList<JavaAnnotation> annotations = SyncList.empty();

    @JsonProperty("assignee_type")
    private JavaType assigneeType;

    @JsonProperty("assignee_name")
    private String assigneeName;

    @Default
    @JsonProperty("expressions")
    private SyncList<JavaExpression> expressions = SyncList.empty();

}
