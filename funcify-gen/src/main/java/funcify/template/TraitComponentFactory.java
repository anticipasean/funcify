package funcify.template;

import funcify.EnsembleKind;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface TraitComponentFactory {

    <TC> TC buildTraitComponent(DefinitionTemplate<TC> template,
                                EnsembleKind ensembleKind);

}
