package funcify.design.solo;

import funcify.ensemble.Solo;
import funcify.template.solo.FlattenableSoloTemplate;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface FlattenableSolo<W, A> extends Solo<W, A> {

    FlattenableSoloTemplate<W> factory();

    default <B, S extends Solo<W, B>> S flatMap1(final Function<? super A, ? extends S> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <B> FlattenableSolo<W, B> flatMap(final Function<? super A, ? extends FlattenableSolo<W, B>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <B> FlattenableSolo<W, B> map(final Function<? super A, ? extends B> mapper) {
        return factory().map(this,
                             mapper)
                        .narrowT1();
    }

    default <B, C> FlattenableSolo<W, C> zip(final FlattenableSolo<W, B> container2,
                                             final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return factory().zip(this,
                             container2,
                             combiner)
                        .narrowT1();
    }

    default <B> FlattenableSolo<W, Tuple2<A, B>> zip(final FlattenableSolo<W, B> container2) {
        return factory().zip(this,
                             container2)
                        .narrowT1();
    }

}
