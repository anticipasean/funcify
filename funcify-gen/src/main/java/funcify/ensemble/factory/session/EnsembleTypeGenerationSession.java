package funcify.ensemble.factory.session;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.factory.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaTypeDefinition;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface EnsembleTypeGenerationSession extends TypeGenerationSession<ETSWT> {

    /**
     * Session Witness Type for Higher Kinded Typing
     */
    public static enum ETSWT {

    }

    static  EnsembleTypeGenerationSession narrowK(final TypeGenerationSession<ETSWT> typeGenerationSession) {
        return (EnsembleTypeGenerationSession) typeGenerationSession;
    }

    SyncList<EnsembleKind> getEnsembleKinds();

    JavaTypeDefinition getBaseEnsembleInterfaceTypeDefinition();

    EnsembleTypeGenerationSession withBaseEnsembleInterfaceTypeDefinition(final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition);

    SyncMap<EnsembleKind, JavaTypeDefinition> getEnsembleInterfaceTypeDefinitionsByEnsembleKind();

    EnsembleTypeGenerationSession withEnsembleInterfaceTypeDefinitionsByEnsembleKind(final SyncMap<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind);

}
