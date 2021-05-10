package funcify.flattenable;

import funcify.conjunct.ConjunctDuet;
import funcify.conjunct.ConjunctSolo;
import funcify.function.Fn2;
import funcify.option.Option;
import funcify.template.solo.FlattenableConjunctSoloTemplate;
import funcify.tuple.Tuple2;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface FlattenableConjunctSolo<W, A> extends ConjunctSolo<W, A>, FlattenableSolo<W, A> {

    @Override
    FlattenableConjunctSoloTemplate<W> factory();

    @Override
    default <B> FlattenableConjunctSolo<W, B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

    default Option<A> filter(Predicate<? super A> condition) {
        return fold(Option::of).filter(condition);
    }

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
