package funcify.conjunct;

import java.util.function.Function;

/**
 * A Solo that can hold a value for type parameter A
 *
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctSolo<W, A> {

    <B> ConjunctSolo<W, B> from(final B value);

    <B> B fold(Function<? super A, ? extends B> mapper);

}
