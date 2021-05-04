package funcify.ensemble;

import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSolo<W, A> extends Solo<W, A> {

    FlattenableSoloTemplate<W> factory();

    static <W, A> FlattenableSolo<W, A> narrowK(Solo<W, A> wideInstance) {
        return (FlattenableSolo<W, A>) wideInstance;
    }

    default <B> FlattenableSolo<W, B> from(final B value) {
        return narrowK(factory().from(value));
    }

    default <B> FlattenableSolo<W, B> flatMap(final Function<? super A, ? extends FlattenableSolo<W, B>> flatMapper) {
        return narrowK(factory().flatMap(this,
                                         flatMapper));
    }

    default <B> FlattenableSolo<W, B> map(final Function<? super A, ? extends B> mapper) {
        return narrowK(factory().map(this,
                                     mapper));
    }

    default <B, C> FlattenableSolo<W, C> zip(final FlattenableSolo<W, B> container2,
                                             final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return narrowK(factory().zip(this,
                                     container2,
                                     combiner));
    }

}
