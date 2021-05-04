package funcify.template.duet;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface MappableDuetTemplate<W> {

    <A, B, C> Duet<W, A, C> map(Duet<W, A, B> container,
                                Function<? super B, ? extends C> mapper);

}
