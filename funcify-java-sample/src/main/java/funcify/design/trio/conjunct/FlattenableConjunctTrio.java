package funcify.design.trio.conjunct;

import funcify.design.trio.FilterableTrio;
import funcify.design.trio.FlattenableTrio;
import funcify.function.Fn1;
import funcify.function.Fn3;
import funcify.template.trio.conjunct.FlattenableConjunctTrioTemplate;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface FlattenableConjunctTrio<W, A, B, C> extends ConjunctTrio<W, A, B, C>, FlattenableTrio<W, A, B, C>,
                                                             FilterableTrio<W, A, B, C> {

    FlattenableConjunctTrioTemplate<W> factory();

    default <D, E, F> FlattenableConjunctTrio<W, D, E, F> flatMap(final Fn3<? super A, ? super B, ? super C, ? extends FlattenableTrio<W, D, E, F>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }


    @Override
    default <D> FlattenableConjunctTrio<W, D, B, C> flatMapFirst(final Fn1<? super A, ? extends FlattenableTrio<W, D, B, C>> flatMapper) {
        return FlattenableTrio.super.flatMapFirst(flatMapper)
                                    .narrowT1();
    }


    @Override
    default <D> FlattenableConjunctTrio<W, A, D, C> flatMapSecond(final Fn1<? super B, ? extends FlattenableTrio<W, A, D, C>> flatMapper) {
        return FlattenableTrio.super.flatMapSecond(flatMapper)
                                    .narrowT1();
    }


    @Override
    default <D> FlattenableConjunctTrio<W, A, B, D> flatMapThird(final Fn1<? super C, ? extends FlattenableTrio<W, A, B, D>> flatMapper) {
        return FlattenableTrio.super.flatMapThird(flatMapper)
                                    .narrowT1();
    }

    @Override
    default <D> FlattenableConjunctTrio<W, D, B, C> mapFirst(final Fn1<? super A, ? extends D> mapper) {
        return FlattenableTrio.super.<D>mapFirst(mapper).narrowT1();
    }

    @Override
    default <D> FlattenableConjunctTrio<W, A, D, C> mapSecond(final Fn1<? super B, ? extends D> mapper) {
        return FlattenableTrio.super.<D>mapSecond(mapper).narrowT1();
    }

    @Override
    default <D> FlattenableConjunctTrio<W, A, B, D> mapThird(final Fn1<? super C, ? extends D> mapper) {
        return FlattenableTrio.super.<D>mapThird(mapper).narrowT1();
    }


}
