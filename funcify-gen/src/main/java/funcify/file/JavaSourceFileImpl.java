package funcify.file;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-08-28
 */
@AllArgsConstructor(staticName = "of")
@Getter
@Builder
class JavaSourceFileImpl implements JavaSourceFile {

    @Default
    private final List<String> packageNameAsPathSegments = Collections.emptyList();

    private final String className;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Default
    private final Optional<File> file = Optional.empty();
}
