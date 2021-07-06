package funcify.template.quartet.conjunct;

import funcify.ensemble.Quartet;
import funcify.function.Fn1;
import funcify.function.Fn4;
import funcify.template.quartet.FlattenableQuartetTemplate;
import java.util.Objects;

/**
 * @author smccarron
 * @created 2021-05-07
 */
public interface FlattenableConjunctQuartetTemplate<W> extends ConjunctQuartetTemplate<W>, FlattenableQuartetTemplate<W> {

    default <A, B, C, D, E, F, G, H> Quartet<W, E, F, G, H> flatMap(final Quartet<W, A, B, C, D> container,
                                                                    final Fn4<? super A, ? super B, ? super C, ? super D, ? extends Quartet<W, E, F, G, H>> flatMapper) {
        return fold(container,
                    (A a, B b, C c, D d) -> {
                        return Objects.requireNonNull(flatMapper,
                                                      () -> "flatMapper")
                                      .apply(a,
                                             b,
                                             c,
                                             d)
                                      .narrowT1();
                    });
    }

    @Override
    default <A, B, C, D, E> Quartet<W, E, B, C, D> flatMapFirst(final Quartet<W, A, B, C, D> container,
                                                                final Fn1<? super A, ? extends Quartet<W, E, B, C, D>> flatMapper) {
        return flatMap(container,
                       (a, b, c, d) -> Objects.requireNonNull(flatMapper,
                                                              () -> "flatMapper")
                                              .apply(a));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, E, C, D> flatMapSecond(final Quartet<W, A, B, C, D> container,
                                                                 final Fn1<? super B, ? extends Quartet<W, A, E, C, D>> flatMapper) {
        return flatMap(container,
                       (a, b, c, d) -> Objects.requireNonNull(flatMapper,
                                                              () -> "flatMapper")
                                              .apply(b));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, E, D> flatMapThird(final Quartet<W, A, B, C, D> container,
                                                                final Fn1<? super C, ? extends Quartet<W, A, B, E, D>> flatMapper) {
        return flatMap(container,
                       (a, b, c, d) -> Objects.requireNonNull(flatMapper,
                                                              () -> "flatMapper")
                                              .apply(c));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, C, E> flatMapFourth(final Quartet<W, A, B, C, D> container,
                                                                 final Fn1<? super D, ? extends Quartet<W, A, B, C, E>> flatMapper) {
        return flatMap(container,
                       (a, b, c, d) -> Objects.requireNonNull(flatMapper,
                                                              () -> "flatMapper")
                                              .apply(d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, E, B, C, D> mapFirst(final Quartet<W, A, B, C, D> container,
                                                            final Fn1<? super A, ? extends E> mapper) {
        return flatMap(container,
                       (A a, B b, C c, D d) -> from(Objects.requireNonNull(mapper,
                                                                           () -> "mapper")
                                                           .apply(a),
                                                    b,
                                                    c,
                                                    d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, E, C, D> mapSecond(final Quartet<W, A, B, C, D> container,
                                                             final Fn1<? super B, ? extends E> mapper) {
        return flatMap(container,
                       (A a, B b, C c, D d) -> from(a,
                                                    Objects.requireNonNull(mapper,
                                                                           () -> "mapper")
                                                           .apply(b),
                                                    c,
                                                    d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, E, D> mapThird(final Quartet<W, A, B, C, D> container,
                                                            final Fn1<? super C, ? extends E> mapper) {
        return flatMap(container,
                       (A a, B b, C c, D d) -> from(a,
                                                    b,
                                                    Objects.requireNonNull(mapper,
                                                                           () -> "mapper")
                                                           .apply(c),
                                                    d));
    }

    @Override
    default <A, B, C, D, E> Quartet<W, A, B, C, E> mapFourth(final Quartet<W, A, B, C, D> container,
                                                             final Fn1<? super D, ? extends E> mapper) {
        return flatMap(container,
                       (A a, B b, C c, D d) -> from(a,
                                                    b,
                                                    c,
                                                    Objects.requireNonNull(mapper,
                                                                           () -> "mapper")
                                                           .apply(d)));
    }
}
