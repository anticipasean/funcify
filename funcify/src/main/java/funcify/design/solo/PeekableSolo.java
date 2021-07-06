package funcify.design.solo;

import funcify.ensemble.Solo;
import funcify.template.solo.PeekableSoloTemplate;
import java.util.function.Consumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableSolo<W, A> extends Solo<W, A> {

    PeekableSoloTemplate<W> factory();

    default PeekableSolo<W, A> peek(final Consumer<? super A> consumer) {
        return factory().peek(this,
                              consumer)
                        .narrowT1();
    }

}
