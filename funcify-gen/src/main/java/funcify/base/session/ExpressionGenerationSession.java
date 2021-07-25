package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationSession<SWT> {

    SyncList<JavaExpression> getExpressionsInStatement(final JavaStatement statementDef);

    JavaExpression simpleExpression(final SyncList<String> text);


    JavaExpression templateExpression(final String templateFunction,
                                      final SyncMap<String, Object> input);

}
