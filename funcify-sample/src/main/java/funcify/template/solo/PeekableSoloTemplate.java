package funcify.template.solo;

import funcify.ensemble.Solo;
import java.util.function.Consumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableSoloTemplate<W> {

    <A> Solo<W, A> peek(final Solo<W, A> container,
                        final Consumer<? super A> consumer);

}
