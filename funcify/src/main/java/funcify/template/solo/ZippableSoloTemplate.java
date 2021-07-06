package funcify.template.solo;

import funcify.ensemble.Solo;
import funcify.tuple.Tuple2;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface ZippableSoloTemplate<W> extends MappableSoloTemplate<W> {

    <A, B, F extends Function<? super A, ? extends B>> Solo<W, B> ap(Solo<W, A> container,
                                                                     Solo<W, F> containerWithFunction);

    <A, B, C> Solo<W, C> zip(Solo<W, A> container1,
                             Solo<W, B> container2,
                             BiFunction<? super A, ? super B, ? extends C> combiner);

    default <A, B> Solo<W, Tuple2<A, B>> zip(Solo<W, A> container1,
                                             Solo<W, B> container2) {
        return zip(container1,
                   container2,
                   Tuple2::of);
    }

}
