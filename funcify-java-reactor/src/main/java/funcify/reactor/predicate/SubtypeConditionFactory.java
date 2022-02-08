package funcify.reactor.predicate;


import java.util.ArrayList;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-09-03
 */
public interface SubtypeConditionFactory {

    static <PT> SubtypeCondition<PT, ?> forParentType(final Class<PT> parentType) {
        final List<PossibleSubtypeCondition<PT, ?>> possibleSubtypeConditions = new ArrayList<>();
        return SubtypeConditionImpl.of(parentType, possibleSubtypeConditions);
    }


}
