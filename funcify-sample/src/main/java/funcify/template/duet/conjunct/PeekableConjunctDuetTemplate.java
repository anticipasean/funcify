package funcify.template.duet.conjunct;

import funcify.ensemble.Duet;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableConjunctDuetTemplate<W> extends ConjunctDuetTemplate<W> {

    default <A, B> Duet<W, A, B> peek(final Duet<W, A, B> container,
                                      final BiConsumer<? super A, ? super B> consumer) {
        return fold(container,
                    (A a, B b) -> {
                        Objects.requireNonNull(consumer,
                                               () -> "consumer")
                               .accept(a,
                                       b);
                        return container;
                    });
    }

}
