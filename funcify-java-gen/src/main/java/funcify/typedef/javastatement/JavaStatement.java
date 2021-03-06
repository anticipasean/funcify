package funcify.typedef.javastatement;

import funcify.tool.container.SyncList;
import funcify.typedef.Definition;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.javaexpr.JavaExpression;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaStatement extends Definition<JavaStatement> {

    SyncList<JavaAnnotation> getAnnotations();

    default boolean isReturnStatement() {
        return false;
    }

    default boolean isAssignment() {
        return false;
    }

    SyncList<JavaExpression> getExpressions();
}
