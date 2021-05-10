package funcify.design.solo.disjunct;

import funcify.design.solo.FilterableSolo;
import funcify.design.solo.FlattenableSolo;
import funcify.template.solo.disjunct.FlattenableDisjunctSoloTemplate;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public interface FlattenableDisjunctSolo<W, A> extends DisjunctSolo<W, A>, FlattenableSolo<W, A>, FilterableSolo<W, A> {

    @Override
    FlattenableDisjunctSoloTemplate<W> factory();

    @Override
    default <B> FlattenableDisjunctSolo<W, B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

    @Override
    FlattenableDisjunctSolo<W, A> empty();

    @Override
    default <B> FlattenableDisjunctSolo<W, B> flatMap(final Function<? super A, ? extends FlattenableSolo<W, B>> flatMapper) {
        return FlattenableSolo.super.flatMap(flatMapper)
                                    .narrowT1();
    }

}
