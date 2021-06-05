package funcify;

import funcify.tool.SyncList;
import funcify.tool.SyncMap;
import funcify.typedef.JavaTypeDefinition;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-20
 */
@AllArgsConstructor
@Builder
@With
@Getter
@ToString
public class DefaultGenerationSession {

    private final Path destinationDirectoryPath;

    @Default
    private final SyncList<EnsembleKind> ensembleKinds = SyncList.empty();

    private final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition;

    @Default
    private final SyncMap<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind = SyncMap.empty();


}
