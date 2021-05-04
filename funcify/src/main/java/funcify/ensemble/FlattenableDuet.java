package funcify.ensemble;

import funcify.template.duet.FlattenableDuetTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableDuet<W, A, B> extends Duet<W, A, B> {

    FlattenableDuetTemplate<W> factory();

    static <W, A, B> FlattenableDuet<W, A, B> narrowK(Duet<W, A, B> wideInstance) {
        return (FlattenableDuet<W, A, B>) wideInstance;
    }

    default <C> FlattenableDuet<W, A, C> map(final Function<? super B, ? extends C> mapper) {
        return narrowK(factory().map(this,
                                     mapper));
    }

    default <C> FlattenableDuet<W, A, C> zip(final FlattenableDuet<W, A, B> container2,
                                             final BiFunction<? super B, ? super B, ? extends C> combiner) {
        return narrowK(factory().zip(this,
                                     container2,
                                     combiner));
    }

}
