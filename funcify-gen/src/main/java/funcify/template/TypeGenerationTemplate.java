package funcify.template;

import funcify.session.TypeGenerationSession;
import java.nio.file.Path;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TypeGenerationTemplate<V, R> {

    List<String> getDestinationTypePackagePathSegments();

    default String getStringTemplateGroupFileName() {
        return getStringTemplateGroupFilePath().getFileName()
                                               .toString();
    }

    Path getStringTemplateGroupFilePath();

    TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session);


}
