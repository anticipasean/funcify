package funcify.design.solo.conjunct;

import funcify.design.solo.FilterableSolo;
import funcify.design.solo.FlattenableSolo;
import funcify.design.solo.PeekableSolo;
import funcify.template.solo.conjunct.FlattenableConjunctSoloTemplate;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface FlattenableConjunctSolo<W, A> extends ConjunctSolo<W, A>, FlattenableSolo<W, A>, FilterableSolo<W, A>,
                                                       PeekableSolo<W, A> {

    @Override
    FlattenableConjunctSoloTemplate<W> factory();

    @Override
    default <B> FlattenableConjunctSolo<W, B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

}
