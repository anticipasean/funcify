package funcify.st.template;

import funcify.base.session.TypeGenerationSession;
import funcify.base.template.ExpressionGenerationTemplate;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaParameter;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-07-25
 */
public interface STExpressionGenerationTemplate<SWT> extends ExpressionGenerationTemplate<SWT> {

    default JavaExpression lambdaExpression(final TypeGenerationSession<SWT> session,
                                            final SyncList<JavaParameter> lambdaParameters,
                                            final JavaExpression lambdaBody) {
        return session.templateExpression("lambda",
                                          SyncMap.<String, Object>of("parameters",
                                                                     lambdaParameters)
                                                 .put("expression",
                                                      lambdaBody));
    }

    default JavaExpression lambdaExpression(final TypeGenerationSession<SWT> session,
                                            final SyncList<JavaParameter> lambdaParameters,
                                            final JavaCodeBlock lambdaCodeBlock) {
        return session.templateExpression("lambda",
                                          SyncMap.<String, Object>of("parameters",
                                                                     lambdaParameters)
                                                 .put("code_block",
                                                      lambdaCodeBlock));
    }

    default JavaExpression castAsExpression(final TypeGenerationSession<SWT> session,
                                            final String variableName,
                                            final JavaType castType) {
        return session.templateExpression("cast_as",
                                          SyncMap.of("variable_name",
                                                     variableName,
                                                     "java_type",
                                                     castType));
    }

    default JavaExpression castAsExpression(final TypeGenerationSession<SWT> session,
                                            final JavaExpression javaExpression,
                                            final JavaType castType) {
        return session.templateExpression("cast_as",
                                          SyncMap.of("expression",
                                                     javaExpression,
                                                     "java_type",
                                                     castType));
    }

    default JavaExpression nullCheckExpression(final TypeGenerationSession<SWT> session,
                                               final JavaExpression javaExpression) {
        return session.templateExpression("null_check",
                                          SyncMap.of("expression",
                                                     javaExpression));
    }

    default JavaExpression nullCheckExpression(final TypeGenerationSession<SWT> session,
                                               final JavaParameter parameter) {
        return session.templateExpression("null_check",
                                          SyncMap.of("parameter",
                                                     parameter));
    }

    default JavaExpression nullCheckExpression(final TypeGenerationSession<SWT> session,
                                               final String variableName) {
        return session.templateExpression("null_check",
                                          SyncMap.of("name",
                                                     variableName));
    }


}
