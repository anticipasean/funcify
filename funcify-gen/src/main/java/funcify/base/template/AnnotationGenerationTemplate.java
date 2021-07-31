package funcify.base.template;

import funcify.base.session.TypeGenerationSession;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;

/**
 * @author smccarron
 * @created 2021-07-31
 */
public interface AnnotationGenerationTemplate<SWT> extends ExpressionGenerationTemplate<SWT> {

    default JavaAnnotation annotation(final TypeGenerationSession<SWT> session,
                                      final String name,
                                      final SyncMap<String, Object> parameters) {
        return session.annotation(name,
                                  parameters);
    }

    default JavaAnnotation valueAnnotation(final TypeGenerationSession<SWT> session,
                                           final String annotationName,
                                           final Object annotationValue) {
        return annotation(session,
                          annotationName,
                          SyncMap.of("value",
                                     annotationValue));
    }

}
