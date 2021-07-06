package funcify.template.trio.conjunct;

import funcify.ensemble.Trio;
import funcify.function.Fn1;
import funcify.function.Fn3;
import funcify.template.trio.FlattenableTrioTemplate;
import java.util.Objects;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface FlattenableConjunctTrioTemplate<W> extends ConjunctTrioTemplate<W>, FlattenableTrioTemplate<W> {

    default <A, B, C, D, E, F> Trio<W, D, E, F> flatMap(final Trio<W, A, B, C> container,
                                                        final Fn3<? super A, ? super B, ? super C, ? extends Trio<W, D, E, F>> flatMapper) {
        return fold(container,
                    (A a, B b, C c) -> {
                        return Objects.requireNonNull(flatMapper,
                                                      () -> "flatMapper")
                                      .apply(a,
                                             b,
                                             c)
                                      .narrowT1();
                    });
    }

    @Override
    default <A, B, C, D> Trio<W, D, B, C> flatMapFirst(final Trio<W, A, B, C> container,
                                                       final Fn1<? super A, ? extends Trio<W, D, B, C>> flatMapper) {
        return flatMap(container,
                       (a, b, c) -> Objects.requireNonNull(flatMapper,
                                                           () -> "flatMapper")
                                           .apply(a));
    }

    @Override
    default <A, B, C, D> Trio<W, A, D, C> flatMapSecond(final Trio<W, A, B, C> container,
                                                        final Fn1<? super B, ? extends Trio<W, A, D, C>> flatMapper) {
        return flatMap(container,
                       (a, b, c) -> Objects.requireNonNull(flatMapper,
                                                           () -> "flatMapper")
                                           .apply(b));
    }

    @Override
    default <A, B, C, D> Trio<W, A, B, D> flatMapThird(final Trio<W, A, B, C> container,
                                                       final Fn1<? super C, ? extends Trio<W, A, B, D>> flatMapper) {
        return flatMap(container,
                       (a, b, c) -> Objects.requireNonNull(flatMapper,
                                                           () -> "flatMapper")
                                           .apply(c));
    }

    @Override
    default <A, B, C, D> Trio<W, D, B, C> mapFirst(final Trio<W, A, B, C> container,
                                                   final Fn1<? super A, ? extends D> mapper) {
        return flatMap(container,
                       (A a, B b, C c) -> from(Objects.requireNonNull(mapper,
                                                                      () -> "mapper")
                                                      .apply(a),
                                               b,
                                               c));
    }

    @Override
    default <A, B, C, D> Trio<W, A, D, C> mapSecond(final Trio<W, A, B, C> container,
                                                    final Fn1<? super B, ? extends D> mapper) {
        return flatMap(container,
                       (A a, B b, C c) -> from(a,
                                               Objects.requireNonNull(mapper,
                                                                      () -> "mapper")
                                                      .apply(b),
                                               c));
    }

    @Override
    default <A, B, C, D> Trio<W, A, B, D> mapThird(final Trio<W, A, B, C> container,
                                                   final Fn1<? super C, ? extends D> mapper) {
        return flatMap(container,
                       (A a, B b, C c) -> from(a,
                                               b,
                                               Objects.requireNonNull(mapper,
                                                                      () -> "mapper")
                                                      .apply(c)));
    }
}
