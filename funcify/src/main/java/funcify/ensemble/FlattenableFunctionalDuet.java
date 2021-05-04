package funcify.ensemble;

import funcify.template.duet.FlattenableFunctionalTypeDuetTemplate;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableFunctionalDuet<W, A, B> extends FlattenableDuet<W, A, B> {

    static <W, A, B> FlattenableFunctionalDuet<W, A, B> narrowK(Duet<W, A, B> wideInstance) {
        return (FlattenableFunctionalDuet<W, A, B>) wideInstance;
    }

    @Override
    FlattenableFunctionalTypeDuetTemplate<W> factory();

    default <C> FlattenableFunctionalDuet<W, A, C> flatMap(final Function<? super B, ? extends FlattenableFunctionalDuet<W, A, C>> flatMapper) {
        return narrowK(factory().flatMap(this,
                                         flatMapper));
    }

}
