package funcify.design.duet.disjunct;

import funcify.template.duet.disjunct.PeekableDisjunctDuetTemplate;
import java.util.function.Consumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableDisjunctDuet<W, A, B> extends DisjunctDuet<W, A, B> {

    PeekableDisjunctDuetTemplate<W> factory();

    default PeekableDisjunctDuet<W, A, B> bipeek(final Consumer<? super A> consumer1,
                                                 final Consumer<? super B> consumer2) {
        return factory().<A, B>bipeek(this,
                                      consumer1,
                                      consumer2).narrowT1();
    }

    default PeekableDisjunctDuet<W, A, B> peekFirst(final Consumer<? super A> consumer) {
        return factory().peekFirst(this,
                                   consumer)
                        .narrowT1();
    }

    default PeekableDisjunctDuet<W, A, B> peekSecond(final Consumer<? super B> consumer) {
        return factory().peekSecond(this,
                                    consumer)
                        .narrowT1();
    }

}
