package funcify.base.session;

import funcify.tool.container.SyncMap;
import funcify.typedef.JavaAnnotation;

/**
 * @param <SWT> - Session Witness Type
 * @author smccarron
 * @created 2021-07-31
 */
public interface AnnotationGenerationSession<SWT> extends ExpressionGenerationSession<SWT> {

    JavaAnnotation annotation(final String name,
                              final SyncMap<String, Object> parameters);

}
