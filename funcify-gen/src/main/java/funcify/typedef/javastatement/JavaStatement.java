package funcify.typedef.javastatement;

import funcify.Definition;
import funcify.typedef.javaexpr.JavaExpression;
import java.util.List;

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

    List<JavaExpression> getExpressions();
}
