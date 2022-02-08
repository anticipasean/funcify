package funcify.reactor.predicate;

import funcify.reactor.iterable.IterableOps;
import funcify.reactor.reflect.TypeOps;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author smccarron
 * @created 2021-08-05
 */
public interface PredicateOps {

    static <T> Predicate<T> and(final Predicate<T> condition1,
                                final Predicate<T> condition2) {
        return Objects.requireNonNull(condition1, () -> "condition1").and(Objects.requireNonNull(condition2, () -> "condition2"));
    }

    static <A, B> BiPredicate<Object, Object> bothOfType(final Class<A> type1,
                                                         final Class<B> type2) {
        return (o1, o2) -> TypeOps.isInstanceOfType(o1, type1) && TypeOps.isInstanceOfType(o2, type2);
    }

    static <A, B> BiPredicate<Object, Object> bothOfTypeAnd(final Class<A> type1,
                                                            final Class<B> type2,
                                                            final BiPredicate<A, B> typedCondition) {
        return bothOfType(type1, type2).and((o1, o2) -> {
            final Optional<A> aOpt = TypeOps.toType(o1, type1);
            final Optional<B> bOpt = TypeOps.toType(o2, type2);
            return aOpt.isPresent() && bOpt.isPresent() && typedCondition.test(aOpt.get(), bOpt.get());

        });
    }

    static <I extends Iterable<T>, T, S extends T> Predicate<I> hasAtLeastOneElementOfSubtype(final Class<S> subType) {
        Objects.requireNonNull(subType, () -> "subtype");
        return iter -> IterableOps.fromIterable(iter).anyMatch(subType::isInstance);

    }

    static <I extends Iterable<T>, T, S extends T> Predicate<I> hasAtLeastSize(final int sizeThreshold) {
        return iter -> IterableOps.fromIterable(iter).count() >= sizeThreshold;

    }

    @SafeVarargs
    static <I extends Iterable<T>, T> Predicate<I> hasAtLeastOneElementOfEachSubtype(final Class<? extends T>... subtypes) {
        Objects.requireNonNull(subtypes, () -> "subtypes");
        return iter -> {
            if (iter == null || subtypes.length == 0 || iter.spliterator().getExactSizeIfKnown() == 0) {
                return false;
            }
            final Set<Class<? extends T>> subTypesSet = Arrays.stream(subtypes).collect(Collectors.toSet());
            final Map<Class<? extends T>, Integer> typesEncountered = new HashMap<>();
            iter.forEach(item -> {
                final Optional<Class<? extends T>> subTypeMatch = subTypesSet.stream()
                                                                             .filter(st -> st.isInstance(item))
                                                                             .findFirst();
                if (subTypeMatch.isPresent()) {
                    typesEncountered.putIfAbsent(subTypeMatch.get(), 0);
                    typesEncountered.computeIfPresent(subTypeMatch.get(), (st, count) -> count + 1);
                }
            });
            return typesEncountered.keySet().size() == subTypesSet.size();
        };

    }

    static <I extends Iterable<Class<? extends T>>, T> Predicate<T> matchesAtLeastOneSubtypeIn(final I subtypesIterable) {
        Objects.requireNonNull(subtypesIterable, () -> "subtypesIterable");
        return item -> {
            return IterableOps.fromIterable(subtypesIterable)
                              .filter(Objects::nonNull)
                              .anyMatch(subtype -> subtype.isInstance(item));
        };
    }

    @SafeVarargs
    static <T> Predicate<T> matchesAtLeastOneSubtypeIn(final Class<? extends T>... subtypes) {
        Objects.requireNonNull(subtypes, () -> "subtypes");
        return item -> {
            return Arrays.stream(subtypes).filter(Objects::nonNull).anyMatch(subtype -> subtype.isInstance(item));
        };
    }

    static <T> SubtypeCondition<T, ?> matchesConditionForAtLeastOneSubtypeOf(final Class<T> parentType) {
        return SubtypeConditionFactory.forParentType(parentType);
    }

}
