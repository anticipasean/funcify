package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface DisjunctSoloTemplate<W> {

    <B> Solo<W, B> from(B value);

    <A> Solo<W, A> empty();

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> ifPresent,
                  Supplier<B> ifAbsent);

}
