package funcify.ensemble.trait.name;

import static funcify.tool.StringOps.firstLetterCapitalizer;

import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-07-25
 */
public interface EnsembleTraitName {

    static EnsembleTraitName of(String traitComponentName,
                                String ensembleComponentName) {
        return of(traitComponentName,
                  "",
                  ensembleComponentName);
    }

    static EnsembleTraitName of(String traitComponentName,
                                String subTraitComponentName,
                                String ensembleComponentName) {
        return DefaultEnsembleTraitName.builder()
                                       .traitComponent(firstLetterCapitalizer().apply(traitComponentName))
                                       .subTraitComponent(firstLetterCapitalizer().apply(subTraitComponentName))
                                       .ensembleComponent(firstLetterCapitalizer().apply(ensembleComponentName))
                                       .build();
    }

    String getTraitComponent();

    String getSubTraitComponent();

    String getEnsembleComponent();

    String getFullName();

}
