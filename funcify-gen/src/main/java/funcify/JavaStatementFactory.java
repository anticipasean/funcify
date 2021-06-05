package funcify;

import funcify.tool.SyncList;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;

/**
 * @author smccarron
 * @created 2021-05-27
 */
public interface JavaStatementFactory extends JavaDefinitionFactory<JavaStatement> {


    @Override
    default JavaStatement expressions(final JavaStatement definition,
                                      final SyncList<JavaExpression> expressions) {
        return null;
    }
}
