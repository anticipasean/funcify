package funcify.template.duet.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface IterableDisjunctDuetTemplate<W> extends DisjunctDuetTemplate<W> {

    default <A, B> Iterator<A> iteratorForFirst(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a) -> Stream.of(a)
                                   .iterator(),
                    (B b) -> Stream.<A>empty().iterator());
    }

    default <A, B> Iterator<B> iteratorForSecond(Duet<W, A, B> container) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    (A a) -> Stream.<B>empty().iterator(),
                    (B b) -> Stream.of(b)
                                   .iterator());
    }

    default <A, B> Iterable<A> firstIterable(Duet<W, A, B> container) {
        return () -> iteratorForFirst(container);
    }

    default <A, B> Iterable<B> secondIterable(Duet<W, A, B> container) {
        return () -> iteratorForSecond(container);
    }

    default <I extends Iterable<? extends A>, A, B> Duet<W, A, B> firstFromIterable(final I iterable,
                                                                                    final B defaultSecond) {
        if (iterable != null) {
            for (A current : iterable) {
                return current == null ? second(defaultSecond) : first(current);
            }
        }
        return second(defaultSecond);
    }

    default <I extends Iterable<? extends B>, A, B> Duet<W, A, B> secondFromIterable(final I iterable,
                                                                                     final A defaultFirst) {
        if (iterable != null) {
            for (B current : iterable) {
                return current == null ? first(defaultFirst) : second(current);
            }
        }
        return first(defaultFirst);
    }


}
