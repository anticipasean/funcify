package funcify.reactor.predicate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2/6/22
 */
@AllArgsConstructor(staticName = "of")
@Getter
class PossibleSubtypeCondition<PT, CT> {

    @NonNull
    private final Class<CT> childType;

    @NonNull
    private final Predicate<? super PT> childTypeCondition;

}
