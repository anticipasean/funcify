package funcify.ensemble.trait.name;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-07-25
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter(AccessLevel.PUBLIC)
@Builder(access = AccessLevel.PACKAGE)
@ToString
@EqualsAndHashCode
public class DefaultEnsembleTraitName implements EnsembleTraitName {

    @NonNull
    private final String traitComponent;

    @NonNull
    @Default
    private final String subTraitComponent = "";

    @NonNull
    private final String ensembleComponent;

    @Override
    public String getFullName() {
        return String.join("",
                           getTraitComponent(),
                           getSubTraitComponent(),
                           getEnsembleComponent());
    }
}
