package funcify.template.trio;

import funcify.ensemble.Trio;
import funcify.function.Fn1;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FlattenableTrioTemplate<W> extends ZippableTrioTemplate<W> {

    default <A, B, C> Trio<W, A, B, C> flattenFirst(final Trio<W, Trio<W, A, B, C>, B, C> container) {
        return flatMapFirst(container,
                            f -> f);
    }

    default <A, B, C> Trio<W, A, B, C> flattenSecond(final Trio<W, A, Trio<W, A, B, C>, C> container) {
        return flatMapSecond(container,
                             f -> f);
    }

    default <A, B, C> Trio<W, A, B, C> flattenThird(final Trio<W, A, B, Trio<W, A, B, C>> container) {
        return flatMapThird(container,
                            f -> f);
    }


    <A, B, C, D> Trio<W, D, B, C> flatMapFirst(final Trio<W, A, B, C> container,
                                               final Fn1<? super A, ? extends Trio<W, D, B, C>> flatMapper);


    <A, B, C, D> Trio<W, A, D, C> flatMapSecond(final Trio<W, A, B, C> container,
                                                final Fn1<? super B, ? extends Trio<W, A, D, C>> flatMapper);


    <A, B, C, D> Trio<W, A, B, D> flatMapThird(final Trio<W, A, B, C> container,
                                               final Fn1<? super C, ? extends Trio<W, A, B, D>> flatMapper);
}
