package funcify.flattenable;

import funcify.conjunct.ConjunctDuet;
import funcify.template.duet.FlattenableConjunctDuetTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface FlattenableConjunctDuet<W, A, B> extends ConjunctDuet<W, A, B>, FlattenableDuet<W, A, B> {

    FlattenableConjunctDuetTemplate<W> factory();

    default <C, D> FlattenableConjunctDuet<W, C, D> flatMap(final BiFunction<? super A, ? super B, ? extends FlattenableConjunctDuet<W, C, D>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <C, D> FlattenableConjunctDuet<W, C, D> bimap(final Function<? super A, ? extends C> mapper1,
                                                          final Function<? super B, ? extends D> mapper2) {
        return factory().bimap(this,
                               mapper1,
                               mapper2)
                        .narrowT2();
    }

    default <C, D, E, F> FlattenableConjunctDuet<W, E, F> zipBoth(final FlattenableConjunctDuet<W, C, D> container,
                                                                  final BiFunction<? super A, ? super C, ? extends E> combiner1,
                                                                  final BiFunction<? super B, ? super D, ? extends F> combiner2) {
        return factory().zipBoth(this,
                                 container,
                                 combiner1,
                                 combiner2)
                        .narrowT2();
    }
}
