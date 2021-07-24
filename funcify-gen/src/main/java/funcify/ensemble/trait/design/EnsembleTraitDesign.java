package funcify.ensemble.trait.design;

import funcify.ensemble.EnsembleKind;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface EnsembleTraitDesign {

    EnsembleTraitDesign createOrFindApplicableTraitInterfaceForEnsembleKind(final EnsembleKind ensembleKind);


}
