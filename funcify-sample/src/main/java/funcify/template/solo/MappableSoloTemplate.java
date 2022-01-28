package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface MappableSoloTemplate<W> {

    <A, B> Solo<W, B> map(Solo<W, A> container,
                          Function<? super A, ? extends B> mapper);

}
