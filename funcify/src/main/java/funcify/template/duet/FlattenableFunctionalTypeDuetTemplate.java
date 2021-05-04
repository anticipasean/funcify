package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableFunctionalTypeDuetTemplate<W> extends FlattenableDuetTemplate<W> {

    <A, B> Duet<W, A, B> fromFunction(Function<A, B> function);


}
