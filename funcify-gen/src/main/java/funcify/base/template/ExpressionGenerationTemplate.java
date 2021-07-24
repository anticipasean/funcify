package funcify.base.template;

import funcify.base.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaParameter;
import funcify.typedef.javaexpr.JavaExpression;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationTemplate<SWT> extends JavaTypeGenerationTemplate {

    default JavaExpression simpleExpression(final TypeGenerationSession<SWT> session,
                                            final String... text) {
        return simpleExpression(session,
                                SyncList.of(text));
    }

    default JavaExpression simpleExpression(final TypeGenerationSession<SWT> session,
                                            final SyncList<String> text) {
        return session.simpleExpression(text);
    }

    default JavaExpression templateExpression(final TypeGenerationSession<SWT> session,
                                              final String templateName,
                                              final String... templateParameter) {
        return templateExpression(session,
                                  templateName,
                                  SyncList.of(templateParameter));
    }

    default JavaExpression templateExpression(final TypeGenerationSession<SWT> session,
                                              final String templateName,
                                              final SyncList<String> templateParameters) {
        return session.templateExpression(templateName,
                                          templateParameters);
    }

    default JavaExpression lambdaExpression(final TypeGenerationSession<SWT> session,
                                            final SyncList<JavaParameter> parameters,
                                            final SyncList<JavaExpression> lambdaBodyExpression) {
        return session.lambdaExpression(parameters,
                                        lambdaBodyExpression);
    }

}
