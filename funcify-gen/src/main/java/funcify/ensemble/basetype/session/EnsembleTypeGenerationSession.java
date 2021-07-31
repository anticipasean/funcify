package funcify.ensemble.basetype.session;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.EnsembleTypeGenerationSession.ETSWT;
import funcify.base.session.TypeGenerationSession;
import funcify.ensemble.trait.name.EnsembleTraitName;
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

    SyncMap<EnsembleTraitName, JavaTypeDefinition> getEnsembleTraitTypeDefinitionsByEnsembleTraitName();

    EnsembleTypeGenerationSession withEnsembleInterfaceTypeDefinitionsByEnsembleKind(final SyncMap<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind);

}
