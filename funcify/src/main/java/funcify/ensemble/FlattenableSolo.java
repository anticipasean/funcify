package funcify.ensemble;

import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSolo<F extends FlattenableSolo<F, W, ?>, W, A> extends Solo<W, A> {

    FlattenableSoloTemplate<W> factory();

    static <F extends FlattenableSolo<F, W, ?>, W, A> FlattenableSolo<F, W, A> narrowK(Solo<W, A> wideInstance) {
        return (FlattenableSolo<F, W, A>) wideInstance;
    }

    default <B> FlattenableSolo<F, W, B> from(final B value) {
        return narrowK(factory().from(value));
    }

    default <B> FlattenableSolo<F, W, B> flatMap(final Function<? super A, ? extends FlattenableSolo<F, W, B>> flatMapper) {
        return narrowK(factory().flatMap(this,
                                         flatMapper));
    }

    default <B> FlattenableSolo<F, W, B> map(final Function<? super A, ? extends B> mapper) {
        return narrowK(factory().map(this,
                                     mapper));
    }

    default <B, C> FlattenableSolo<F, W, C> zip(final FlattenableSolo<F, W, B> container2,
                                                final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return narrowK(factory().zip(this,
                                     container2,
                                     combiner));
    }

}
