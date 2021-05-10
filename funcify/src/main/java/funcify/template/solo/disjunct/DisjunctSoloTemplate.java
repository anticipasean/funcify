package funcify.template.solo.disjunct;

import funcify.ensemble.Solo;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface DisjunctSoloTemplate<W> {

    <A> Solo<W, A> from(A value);

    <A> Solo<W, A> empty();

    <A, B> B fold(Solo<W, A> container,
                  Function<? super A, ? extends B> ifPresent,
                  Supplier<B> ifAbsent);


}
