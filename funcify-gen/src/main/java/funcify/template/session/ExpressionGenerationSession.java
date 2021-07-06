package funcify.template.session;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaParameter;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationSession<SWT, TD, MD, CD, SD, ED> {

    SyncList<ED> getExpressionsInStatement(final SD statementDef);

    ED simpleExpression(final SyncList<String> text);


    ED templateExpression(final String templateName,
                          final SyncList<String> templateParameters);


    @SuppressWarnings("unchecked")
    ED lambdaExpression(final SyncList<JavaParameter> parameters,
                        final ED... lambdaBodyExpression);
}
