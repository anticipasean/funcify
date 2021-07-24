package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaParameter;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationSession<SWT> {

    SyncList<JavaExpression> getExpressionsInStatement(final JavaStatement statementDef);

    JavaExpression simpleExpression(final SyncList<String> text);


    JavaExpression templateExpression(final String templateName,
                                      final SyncList<String> templateParameters);
    JavaExpression lambdaExpression(final SyncList<JavaParameter> parameters,
                                    final SyncList<JavaExpression> lambdaBodyExpression);
}
