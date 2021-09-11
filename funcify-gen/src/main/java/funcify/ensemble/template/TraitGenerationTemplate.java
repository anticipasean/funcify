package funcify.ensemble.template;

import funcify.ensemble.EnsembleKind;
import funcify.template.TypeGenerationTemplate;
import funcify.trait.Trait;
import java.util.Set;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TraitGenerationTemplate<V, R> extends TypeGenerationTemplate<V, R> {

    Set<Trait> getTraits();

    default String getTraitNameForEnsembleKind(final EnsembleKind ek) {
        return Trait.generateTraitNameFrom(ek, getTraits());
    }

}
