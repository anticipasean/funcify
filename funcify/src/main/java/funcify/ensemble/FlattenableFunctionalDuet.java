package funcify.ensemble;

import funcify.template.duet.FlattenableFunctionalTypeDuetTemplate;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableFunctionalDuet<F extends FlattenableFunctionalDuet<F, W, ?, ?>, W, A, B> extends
                                                                                                     FlattenableDuet<F, W, A, B> {

    static <F extends FlattenableFunctionalDuet<F, W, ?, ?>, W, A, B> FlattenableFunctionalDuet<F, W, A, B> narrowK(Duet<W, A, B> wideInstance) {
        return (FlattenableFunctionalDuet<F, W, A, B>) wideInstance;
    }

    @Override
    FlattenableFunctionalTypeDuetTemplate<W> factory();

    default <C> FlattenableFunctionalDuet<F, W, A, C> flatMap(final Function<? super B, ? extends FlattenableFunctionalDuet<F, W, A, C>> flatMapper) {
        return narrowK(factory().flatMap(this,
                                         flatMapper));
    }

}
