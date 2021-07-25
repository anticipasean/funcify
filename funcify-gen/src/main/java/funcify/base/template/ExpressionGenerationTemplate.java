package funcify.base.template;

import funcify.base.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
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
                                              final String inputKey,
                                              final Object inputValue) {
        return templateExpression(session,
                                  templateName,
                                  SyncMap.of(inputKey,
                                             inputValue));
    }

    default JavaExpression templateExpression(final TypeGenerationSession<SWT> session,
                                              final String templateName,
                                              final String inputKey1,
                                              final Object inputValue1,
                                              final String inputKey2,
                                              final Object inputValue2) {
        return templateExpression(session,
                                  templateName,
                                  SyncMap.of(inputKey1,
                                             inputValue1,
                                             inputKey2,
                                             inputValue2));
    }

    default JavaExpression templateExpression(final TypeGenerationSession<SWT> session,
                                              final String templateName,
                                              final SyncMap<String, Object> input) {
        return session.templateExpression(templateName,
                                          input);
    }

}
