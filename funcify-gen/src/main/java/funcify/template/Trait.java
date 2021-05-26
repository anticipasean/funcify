package funcify.template;

import funcify.EnsembleKind;
import java.util.Map;

/**
 * @author smccarron
 * @created 2021-05-18
 */
//@AllArgsConstructor
//@Getter
//@Builder
public interface Trait<D> extends TraitComponentFactory {

    String name();

    Map<EnsembleKind, D> templateDefinitionsByEnsembleKind();

    Map<EnsembleKind, D> designDefinitionsByEnsembleKind();

    @Override
    <TC> TC buildTraitComponent(DefinitionTemplate<TC> template, EnsembleKind ensembleKind);
}
