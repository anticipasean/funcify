package funcify.template.quartet;

import funcify.ensemble.Quartet;
import funcify.function.Fn1;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface FlattenableQuartetTemplate<W> extends ZippableQuartetTemplate<W> {

    default <A, B, C, D> Quartet<W, A, B, C, D> flattenFirst(final Quartet<W, Quartet<W, A, B, C, D>, B, C, D> container) {
        return flatMapFirst(container,
                            f -> f);
    }

    default <A, B, C, D> Quartet<W, A, B, C, D> flattenSecond(final Quartet<W, A, Quartet<W, A, B, C, D>, C, D> container) {
        return flatMapSecond(container,
                             f -> f);
    }

    default <A, B, C, D> Quartet<W, A, B, C, D> flattenThird(final Quartet<W, A, B, Quartet<W, A, B, C, D>, D> container) {
        return flatMapThird(container,
                            f -> f);
    }

    default <A, B, C, D> Quartet<W, A, B, C, D> flattenFourth(final Quartet<W, A, B, C, Quartet<W, A, B, C, D>> container) {
        return flatMapFourth(container,
                             f -> f);
    }


    <A, B, C, D, E> Quartet<W, E, B, C, D> flatMapFirst(final Quartet<W, A, B, C, D> container,
                                                        final Fn1<? super A, ? extends Quartet<W, E, B, C, D>> flatMapper);

    <A, B, C, D, E> Quartet<W, A, E, C, D> flatMapSecond(final Quartet<W, A, B, C, D> container,
                                                         final Fn1<? super B, ? extends Quartet<W, A, E, C, D>> flatMapper);

    <A, B, C, D, E> Quartet<W, A, B, E, D> flatMapThird(final Quartet<W, A, B, C, D> container,
                                                        final Fn1<? super C, ? extends Quartet<W, A, B, E, D>> flatMapper);

    <A, B, C, D, E> Quartet<W, A, B, C, E> flatMapFourth(final Quartet<W, A, B, C, D> container,
                                                         final Fn1<? super D, ? extends Quartet<W, A, B, C, E>> flatMapper);
}
