package funcify;

import funcify.ensemble.basetype.session.TypeGenerationSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TypeTemplate {

    default Charset getCharacterEncoding() {
        return StandardCharsets.UTF_8;
    }

    List<String> getDestinationTypePackagePathSegments();

    default Optional<Path> getDestinationTypePackageAsPath() {
        return Optional.ofNullable(getDestinationTypePackagePathSegments())
                       .filter(l -> l.size() > 0)
                       .flatMap(l -> {
                           try {
                               return l.size() == 1 ? Optional.of(Paths.get(l.get(0))) : Optional.of(Paths.get(l.get(0),
                                                                                                               l.stream()
                                                                                                                .skip(1)
                                                                                                                .toArray(String[]::new)));
                           } catch (final Throwable t) {
                               return Optional.empty();
                           }
                       });
    }

    String getStringTemplateGroupFileName();

    String getStringTemplateGroupFilePathString();

    default STGroupFile getStringTemplateGroupFile() throws IOException {
        try {
            return new STGroupFile(getStringTemplateGroupFilePathString(),
                                   getCharacterEncoding().displayName());
        } catch (Exception e) {
            throw new IOException(e.getMessage(),
                                  e);

        }
    }

    TypeGenerationSession createTypesForSession(final TypeGenerationSession session);


}
