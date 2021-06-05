package funcify.template.generation;

import funcify.JavaTypeFactory;
import funcify.template.session.TypeGenerationSession;
import funcify.tool.SyncList;
import funcify.typedef.JavaParameter;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationTemplate<SWT> extends JavaTypeFactory {

    default <TD, MD, CD, SD, ED> ED simpleExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                     final String... text) {
        return simpleExpression(session,
                                SyncList.of(text));
    }

    default <TD, MD, CD, SD, ED> ED simpleExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                     final SyncList<String> text) {
        return session.simpleExpression(text);
    }

    default <TD, MD, CD, SD, ED> ED templateExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final String templateName,
                                                       final String... templateParameter) {
        return templateExpression(session,
                                  templateName,
                                  SyncList.of(templateParameter));
    }

    default <TD, MD, CD, SD, ED> ED templateExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final String templateName,
                                                       final SyncList<String> templateParameters) {
        return session.templateExpression(templateName,
                                          templateParameters);
    }

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> ED lambdaExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                     final SyncList<JavaParameter> parameters,
                                                     final ED... lambdaBodyExpression) {
        return session.lambdaExpression(parameters,
                                        lambdaBodyExpression);
    }

}
