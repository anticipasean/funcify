package funcify.typedef.javastatement;

import funcify.typedef.Definition;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.tool.container.SyncList;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaStatement extends Definition<JavaStatement> {

    default boolean isReturnStatement() {
        return false;
    }

    default boolean isAssignment() {
        return false;
    }

    SyncList<JavaExpression> getExpressions();
}
