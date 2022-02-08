package funcify.reactor.predicate;

import funcify.reactor.reflect.TypeOps;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2/6/22
 */
class SubtypeConditionImpl<PT, CT> implements SubtypeCondition<PT, CT> {

    private final Class<PT> parentType;

    private final List<PossibleSubtypeCondition<PT, ?>> possibleSubtypeConditions;

    SubtypeConditionImpl(final Class<PT> parentType,
                         final List<PossibleSubtypeCondition<PT, ?>> possibleSubtypeConditions) {
        this.parentType = parentType;
        this.possibleSubtypeConditions = possibleSubtypeConditions;
    }

    static <PT, CT> SubtypeCondition<PT, CT> of(final Class<PT> parentType,
                                                final List<PossibleSubtypeCondition<PT, ?>> possibleSubtypeConditions) {
        return new SubtypeConditionImpl<>(parentType, possibleSubtypeConditions);
    }

    @Override
    public <CT1 extends PT> SubtypeCondition<PT, CT1> subtype(final Class<CT1> subtype,
                                                              final Predicate<? super CT1> subtypeCondition) {
        possibleSubtypeConditions.add(PossibleSubtypeCondition.of(subtype,
                                                                  pt -> TypeOps.toType(pt, subtype)
                                                                               .filter(subtypeCondition)
                                                                               .isPresent()));
        return new SubtypeConditionImpl<>(parentType, possibleSubtypeConditions);
    }

    @Override
    public boolean test(final PT parentTypeInstance) {
        return !possibleSubtypeConditions.isEmpty() && parentTypeInstance != null &&
               possibleSubtypeConditions.stream().anyMatch(cond -> cond.getChildTypeCondition().test(parentTypeInstance));
    }
}
