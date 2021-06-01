package funcify.template.session;

import funcify.typedef.JavaParameter;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationSession<SWT, TD, MD, CD, SD, ED> {

    List<ED> getExpressionsInStatement(final SD statementDef);

    ED simpleExpression(final List<String> text);


    ED templateExpression(final String templateName,
                          final List<String> templateParameters);


    @SuppressWarnings("unchecked")
    ED lambdaExpression(final List<JavaParameter> parameters,
                        final ED... lambdaBodyExpression);
}
