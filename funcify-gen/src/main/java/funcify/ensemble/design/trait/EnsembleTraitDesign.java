package funcify.ensemble.design.trait;

import funcify.ensemble.EnsembleKind;

/**
 * @author smccarron
 * @created 2021-07-03
 */
public interface EnsembleTraitDesign<TD, MD, CD, SD, ED> {

    EnsembleTraitDesign<TD, MD, CD, SD, ED> createOrFindApplicableTraitInterfaceForEnsembleKind(final EnsembleKind ensembleKind);


}
