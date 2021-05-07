package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface ConjunctSoloTemplate<W> {

    <B> Solo<W, B> from(B value);

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> mapper);

}
