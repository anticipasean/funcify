package funcify.template.definition;

import static java.util.Arrays.asList;

import funcify.JavaTypeFactory;
import funcify.template.session.TypeGenerationSession;
import funcify.typedef.JavaParameter;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface ExpressionGenerationTemplate<SWT> extends JavaTypeFactory {

    default <TD, MD, CD, SD, ED> ED simpleExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                     final String... text) {
        return simpleExpression(session, asList(text));
    }

    <TD, MD, CD, SD, ED> ED simpleExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                             final List<String> text);

    default <TD, MD, CD, SD, ED> ED templateExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                       final String templateName,
                                                       final String... templateParameter) {
        return templateExpression(session,
                                  templateName,
                                  asList(templateParameter));
    }

    <TD, MD, CD, SD, ED> ED templateExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final String templateName,
                                               final List<String> templateParameters);

    @SuppressWarnings("unchecked")
    <TD, MD, CD, SD, ED> ED lambdaExpression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                             final List<JavaParameter> parameters,
                                             final ED... lambdaBodyExpression);

}
