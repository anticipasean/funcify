package funcify.template;

import funcify.ensemble.basetype.session.TypeGenerationSession;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TypeGenerationTemplate<V, R> {

    List<String> getDestinationTypePackagePathSegments();

    String getStringTemplateGroupFileName();

    String getStringTemplateGroupFilePathString();

    TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session);


}
