package funcify.template;

import funcify.EnsembleKind;
import funcify.GenerationSession;
import funcify.typedef.JavaTypeDefinition;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public class TraitAssembler {

    public static <D, TD> D updateDefinitionFor(D definition,
                                                BiFunction<D, TD, D> definitionUpdater,
                                                Trait<D> trait,
                                                DefinitionTemplate<TD> definitionTemplate,
                                                EnsembleKind ensembleKind) {
        return definitionUpdater.apply(definition,
                                       trait.buildTraitComponent(definitionTemplate,
                                                                 ensembleKind));
    }

    public static <D> D createDefinition(final DefinitionTemplate<D> definitionTemplate,
                                         final EnsembleKind ensembleKind) {
        return definitionTemplate.javaPackage("funcify.ensemble",
                                              definitionTemplate.name(ensembleKind.getSimpleClassName()));
    }

    public GenerationSession assembleTraits(final GenerationSession generationSession) {
        final Map<EnsembleKind, JavaTypeDefinition> traitTemplateDefinitionsByEnsembleKind;
        final Map<EnsembleKind, Set<JavaTypeDefinition>> traitDesignDefinitionsByEnsembleKind;

        generationSession.getEnsembleKinds()
                         .stream()
                         .map(ek -> null);

        return generationSession;

    }

}
