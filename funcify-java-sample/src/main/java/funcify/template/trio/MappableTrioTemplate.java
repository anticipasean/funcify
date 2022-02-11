package funcify.template.trio;

import funcify.ensemble.Trio;
import funcify.function.Fn1;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface MappableTrioTemplate<W> {

    <A, B, C, D> Trio<W, D, B, C> mapFirst(final Trio<W, A, B, C> container,
                                           final Fn1<? super A, ? extends D> mapper);

    <A, B, C, D> Trio<W, A, D, C> mapSecond(final Trio<W, A, B, C> container,
                                            final Fn1<? super B, ? extends D> mapper);

    <A, B, C, D> Trio<W, A, B, D> mapThird(final Trio<W, A, B, C> container,
                                           final Fn1<? super C, ? extends D> mapper);
}
