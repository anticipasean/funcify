package funcify.ensemble.basetype.session;

import funcify.ensemble.EnsembleKind;
import funcify.file.JavaSourceFile;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-31
 */
@AllArgsConstructor
@Builder
@Getter
@With
public class DefaultTypeGenerationSession implements TypeGenerationSession {

    private final Path destinationDirectoryPath;

    @Default
    private final SyncList<EnsembleKind> ensembleKinds = SyncList.empty();

    private final JavaSourceFile baseEnsembleTypeSourceFile;

    @Default
    private final SyncMap<EnsembleKind, JavaSourceFile> ensembleTypeSourceFilesByEnsembleKind = SyncMap.empty();


}
