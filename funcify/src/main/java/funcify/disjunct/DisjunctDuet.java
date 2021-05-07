package funcify.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Duet;
import funcify.option.Option;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A Duet that can hold a value for <b>either</b> type parameter A <b>or</b> B at one time but not both
 *
 * @author smccarron
 * @created 2021-04-28
 */
public interface DisjunctDuet<W, A, B> extends Duet<W, A, B> {

    <C> C fold(Function<? super A, ? extends C> ifFirstPresent,
               Function<? super B, ? extends C> ifSecondPresent);

    default A orElseFirst(final A alternative) {
        return fold(a -> a,
                    b -> alternative);
    }

    default B orElseSecond(final B alternative) {
        return fold(a -> alternative,
                    b -> b);
    }

    default A orElseGetFirst(final Supplier<A> alternativeSupplier) {
        return fold(a -> a,
                    b -> requireNonNull(alternativeSupplier,
                                        () -> "alternativeSupplier").get());
    }

    default B orElseGetSecond(final Supplier<B> alternativeSupplier) {
        return fold(a -> requireNonNull(alternativeSupplier,
                                        () -> "alternativeSupplier").get(),
                    b -> b);
    }

    default Option<A> getFirst() {
        return fold(Option::of,
                    r -> Option.none());
    }

    default Option<B> getSecond() {
        return fold(l -> Option.none(),
                    Option::of);
    }

}
