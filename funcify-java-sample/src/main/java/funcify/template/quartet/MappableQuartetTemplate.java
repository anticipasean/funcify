package funcify.template.quartet;

import funcify.ensemble.Quartet;
import funcify.function.Fn1;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface MappableQuartetTemplate<W> {

    <A, B, C, D, E> Quartet<W, E, B, C, D> mapFirst(final Quartet<W, A, B, C, D> container,
                                                    final Fn1<? super A, ? extends E> mapper);

    <A, B, C, D, E> Quartet<W, A, E, C, D> mapSecond(final Quartet<W, A, B, C, D> container,
                                                     final Fn1<? super B, ? extends E> mapper);

    <A, B, C, D, E> Quartet<W, A, B, E, D> mapThird(final Quartet<W, A, B, C, D> container,
                                                    final Fn1<? super C, ? extends E> mapper);

    <A, B, C, D, E> Quartet<W, A, B, C, E> mapFourth(final Quartet<W, A, B, C, D> container,
                                                     final Fn1<? super D, ? extends E> mapper);
}
