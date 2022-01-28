package funcify.template.duet.disjunct;

import funcify.ensemble.Duet;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableDisjunctDuetTemplate<W> extends DisjunctDuetTemplate<W> {

    default <A, B> Duet<W, A, B> bipeek(final Duet<W, A, B> container,
                                        final Consumer<? super A> consumer1,
                                        final Consumer<? super B> consumer2) {
        return fold(container,
                    (A a) -> {
                        Objects.requireNonNull(consumer1,
                                               () -> "consumer1")
                               .accept(a);
                        return container;
                    },
                    (B b) -> {
                        Objects.requireNonNull(consumer2,
                                               () -> "consumer2")
                               .accept(b);
                        return container;
                    });
    }

    default <A, B> Duet<W, A, B> peekFirst(final Duet<W, A, B> container,
                                           final Consumer<? super A> consumer) {
        return fold(container,
                    (A a) -> {
                        Objects.requireNonNull(consumer,
                                               () -> "consumer")
                               .accept(a);
                        return container;
                    },
                    (B b) -> {
                        return container;
                    });
    }

    default <A, B> Duet<W, A, B> peekSecond(final Duet<W, A, B> container,
                                            final Consumer<? super B> consumer) {
        return fold(container,
                    (A a) -> {
                        return container;
                    },
                    (B b) -> {
                        Objects.requireNonNull(consumer,
                                               () -> "consumer")
                               .accept(b);
                        return container;
                    });
    }

}
