package funcify.choice;

import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctSolo<W, A> {

    <B> B fold(Function<A, B> mapper);

}
