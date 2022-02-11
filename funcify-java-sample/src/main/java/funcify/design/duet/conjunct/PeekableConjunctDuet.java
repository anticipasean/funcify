package funcify.design.duet.conjunct;

import funcify.template.duet.conjunct.PeekableConjunctDuetTemplate;
import java.util.function.BiConsumer;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface PeekableConjunctDuet<W, A, B> extends ConjunctDuet<W, A, B> {

    PeekableConjunctDuetTemplate<W> factory();

    default PeekableConjunctDuet<W, A, B> peek(final BiConsumer<? super A, ? super B> consumer) {
        return factory().peek(this,
                              consumer)
                        .narrowT1();
    }

}
