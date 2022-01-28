package funcify.design.trio;

import funcify.ensemble.Trio;
import funcify.function.Fn1;
import funcify.template.trio.FlattenableTrioTemplate;

/**
 * @author smccarron
 * @created 2021-05-11
 */
public interface FlattenableTrio<W, A, B, C> extends Trio<W, A, B, C> {

    FlattenableTrioTemplate<W> factory();

    default <D> FlattenableTrio<W, D, B, C> flatMapFirst(final Fn1<? super A, ? extends FlattenableTrio<W, D, B, C>> flatMapper) {
        return factory().flatMapFirst(this,
                                      flatMapper)
                        .narrowT1();
    }

    default <D> FlattenableTrio<W, A, D, C> flatMapSecond(final Fn1<? super B, ? extends FlattenableTrio<W, A, D, C>> flatMapper) {
        return factory().flatMapSecond(this,
                                       flatMapper)
                        .narrowT1();
    }

    default <D> FlattenableTrio<W, A, B, D> flatMapThird(final Fn1<? super C, ? extends FlattenableTrio<W, A, B, D>> flatMapper) {
        return factory().flatMapThird(this,
                                      flatMapper)
                        .narrowT1();
    }

    default <D> FlattenableTrio<W, D, B, C> mapFirst(final Fn1<? super A, ? extends D> mapper) {
        return factory().<A, B, C, D>mapFirst(this,
                                              mapper).narrowT1();
    }

    default <D> FlattenableTrio<W, A, D, C> mapSecond(final Fn1<? super B, ? extends D> mapper) {
        return factory().<A, B, C, D>mapSecond(this,
                                               mapper).narrowT1();
    }

    default <D> FlattenableTrio<W, A, B, D> mapThird(final Fn1<? super C, ? extends D> mapper) {
        return factory().<A, B, C, D>mapThird(this,
                                              mapper).narrowT1();
    }

}
