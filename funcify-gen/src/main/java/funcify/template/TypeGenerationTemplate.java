package funcify.template;

import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TypeGenerationTemplate<V, R> {

    static final String STRING_TEMPLATE_GROUP_FILE_EXTENSION = ".stg";

    List<String> getDestinationTypePackagePathSegments();

    default String getStringTemplateGroupFileName() {
        return Optional.ofNullable(getStringTemplateGroupFilePath())
                       .filter(p -> p.getNameCount() > 0)
                       .map(Path::getFileName)
                       .map(p -> {
                           final String fullFileName = p.toString();
                           int fileExtensionStart = fullFileName.indexOf(STRING_TEMPLATE_GROUP_FILE_EXTENSION);
                           return fileExtensionStart >= 0 ? fullFileName.substring(0, fileExtensionStart) : fullFileName;
                       })
                       .orElseThrow(() -> new FuncifyCodeGenException(String.format(
                           "string_template_group_file_path did not match expected file extension [ %s ] so could not extract file name",
                           STRING_TEMPLATE_GROUP_FILE_EXTENSION)));
    }

    Path getStringTemplateGroupFilePath();

    TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session);


}
