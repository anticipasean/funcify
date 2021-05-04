package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface ZippableSoloTemplate<W> extends MappableSoloTemplate<W> {

    <B> Solo<W, B> from(B value);

    <A, B, F extends Function<A, B>> Solo<W, B> ap(Solo<W, A> container,
                                                   Solo<W, F> containerWithFunction);

    <A, B, C> Solo<W, C> zip(Solo<W, A> container1,
                             Solo<W, B> container2,
                             BiFunction<? super A, ? super B, ? extends C> combiner);

}
