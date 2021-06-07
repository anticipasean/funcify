package funcify.factory.session;

import funcify.ensemble.EnsembleKind;
import funcify.factory.session.EnsembleTypeGenerationSession.EnsembleTypeGenerationSessionWT;
import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleTypeGenerationSession<TD, MD, CD, SD, ED> extends
                                                                   TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> {

    /**
     * Session Witness Type for Higher Kinded Typing
     */
    public static enum EnsembleTypeGenerationSessionWT {

    }

    static <TD, MD, CD, SD, ED> EnsembleTypeGenerationSession<TD, MD, CD, SD, ED> narrowK(final TypeGenerationSession<EnsembleTypeGenerationSessionWT, TD, MD, CD, SD, ED> typeGenerationSession) {
        return (EnsembleTypeGenerationSession<TD, MD, CD, SD, ED>) typeGenerationSession;
    }

    SyncList<EnsembleKind> getEnsembleKinds();

    TD getBaseEnsembleInterfaceTypeDefinition();

    EnsembleTypeGenerationSession<TD, MD, CD, SD, ED> withBaseEnsembleInterfaceTypeDefinition(final TD baseEnsembleInterfaceTypeDefinition);

    SyncMap<EnsembleKind, TD> getEnsembleInterfaceTypeDefinitionsByEnsembleKind();

    EnsembleTypeGenerationSession<TD, MD, CD, SD, ED> withEnsembleInterfaceTypeDefinitionsByEnsembleKind(final SyncMap<EnsembleKind, TD> ensembleInterfaceTypeDefinitionsByEnsembleKind);

}
