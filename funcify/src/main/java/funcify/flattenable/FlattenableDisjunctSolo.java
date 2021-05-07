package funcify.flattenable;

import static java.util.Objects.requireNonNull;

import funcify.disjunct.DisjunctSolo;
import funcify.template.solo.FlattenableDisjunctSoloTemplate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface FlattenableDisjunctSolo<W, A> extends DisjunctSolo<W, A>, FlattenableSolo<W, A> {

    @Override
    FlattenableDisjunctSoloTemplate<W> factory();

    @Override
    default <B> FlattenableDisjunctSolo<W, B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

    @Override
    FlattenableDisjunctSolo<W, A> empty();

    @Override
    default <B> FlattenableDisjunctSolo<W, B> flatMap(final Function<? super A, ? extends FlattenableSolo<W, B>> flatMapper) {
        return FlattenableSolo.super.flatMap(flatMapper)
                                    .narrowT1();
    }

    default FlattenableDisjunctSolo<W, A> filter(Predicate<? super A> condition) {
        return flatMap((A paramA) -> {
            return requireNonNull(condition,
                                  () -> "condition").test(paramA) ? from(paramA) : empty();
        });
    }

}
