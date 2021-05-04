package funcify.template.duet;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-30
 */
public interface FlattenableDisjunctDuetTemplate<W> extends FlattenableDuetTemplate<W> {

    <A, B> Duet<W, A, B> first(A value1);

    <A, B> Duet<W, A, B> second(B value2);

    <A, B, C> C fold(Duet<W, A, B> container,
                     Function<? super A, ? extends C> ifFirstPresent,
                     Function<? super B, ? extends C> ifSecondPresent);

    @Override
    default <A, B, C> Duet<W, A, C> flatMap(final Duet<W, A, B> container,
                                            final Function<? super B, ? extends Duet<W, A, C>> flatMapper) {
        return flatMap2(container, flatMapper);
    }

    default <A, B, C> Duet<W, C, B> flatMap1(final Duet<W, A, B> container,
                                             final Function<? super A, ? extends Duet<W, C, B>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"),
                    b -> second(b));
    }

    default <A, B, C> Duet<W, A, C> flatMap2(final Duet<W, A, B> container,
                                             final Function<? super B, ? extends Duet<W, A, C>> flatMapper) {
        return fold(requireNonNull(container,
                                   () -> "container"),
                    a -> first(a),
                    requireNonNull(flatMapper,
                                   () -> "flatMapper"));
    }

    default <A, B> Duet<W, A, B> flatten1(final Duet<W, Duet<W, A, B>, B> container) {
        return flatMap1(container,
                        b -> b);
    }

    default <A, B> Duet<W, A, B> flatten2(final Duet<W, A, Duet<W, A, B>> container) {
        return flatMap2(container,
                        b -> b);
    }

}
