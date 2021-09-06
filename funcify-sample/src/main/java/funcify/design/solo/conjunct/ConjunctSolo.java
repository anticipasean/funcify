package funcify.design.solo.conjunct;

import funcify.design.duet.conjunct.ConjunctDuet;
import funcify.ensemble.Solo;
import funcify.function.Fn2;
import funcify.template.solo.conjunct.ConjunctSoloTemplate;
import funcify.tuple.Tuple2;
import java.util.function.Function;

/**
 * A Solo that can hold a value for type parameter A
 *
 * @author smccarron
 * @created 2021-05-02
 */
public interface ConjunctSolo<W, A> extends Solo<W, A> {

    ConjunctSoloTemplate<W> factory();

    <B> ConjunctSolo<W, B> from(final B value);

    <B> B fold(Function<? super A, ? extends B> mapper);

    default <W2, B> ConjunctDuet<W2, A, B> append(B value,
                                                  Fn2<? super A, ? super B, ? extends ConjunctDuet<W2, A, B>> mapper) {
        return factory().append(this,
                                value,
                                mapper);
    }

    default <B> Tuple2<A, B> append(B value) {
        return factory().append(this,
                                value,
                                Tuple2::of)
                        .narrowT1();
    }

    default <W2, B> ConjunctDuet<W2, B, A> prepend(B value,
                                                   Fn2<? super B, ? super A, ? extends ConjunctDuet<W2, B, A>> mapper) {
        return factory().prepend(this,
                                 value,
                                 mapper);
    }

    default <B> Tuple2<B, A> prepend(B value) {
        return factory().prepend(this,
                                 value,
                                 Tuple2::of);
    }

}
