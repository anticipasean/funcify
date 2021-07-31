package funcify.ensemble.basetype.session;

import funcify.base.session.AbstractTypeGenerationSession;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.ensemble.trait.name.EnsembleTraitName;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaTypeDefinition;
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
public class DefaultEnsembleTypeGenerationSession extends AbstractTypeGenerationSession<ETSWT> implements
                                                                                               EnsembleTypeGenerationSession {

    private final Path destinationDirectoryPath;

    @Default
    private final SyncList<EnsembleKind> ensembleKinds = SyncList.empty();

    private final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition;

    @Default
    private final SyncMap<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleTraitName, JavaTypeDefinition> ensembleTraitTypeDefinitionsByEnsembleTraitName = SyncMap.empty();

}
