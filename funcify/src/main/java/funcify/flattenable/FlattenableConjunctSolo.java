package funcify.flattenable;

import funcify.conjunct.ConjunctSolo;
import funcify.option.Option;
import funcify.template.solo.FlattenableConjunctSoloTemplate;
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
}
