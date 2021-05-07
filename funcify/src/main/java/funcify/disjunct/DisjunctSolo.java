package funcify.disjunct;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.flattenable.FlattenableSolo;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A Solo that can <b>either</b> hold a value for type parameter A <b>or</b> be empty
 *
 * @author smccarron
 * @created 2021-04-28
 */
public interface DisjunctSolo<W, A> extends Solo<W, A> {

    <B> DisjunctSolo<W, B> from(final B value);
    /**
     * Only disjunct solo types can have a valid empty or "none" state Conjunct types should not be empty
     *
     * @param <B>
     * @return
     */
    DisjunctSolo<W, A> empty();

    <B> B fold(Function<? super A, ? extends B> ifPresent,
               Supplier<? extends B> ifAbsent);

    default A orElse(A defaultIfAbsent) {
        return fold(a -> a,
                    () -> defaultIfAbsent);
    }

    default A orElseGet(Supplier<A> defaultIfAbsent) {
        return fold(a -> a,
                    requireNonNull(defaultIfAbsent,
                                   () -> "defaultIfAbsent"));
    }

    default <E extends Throwable> A orElseThrow(E throwable) throws E {
        final A result = fold(a -> a,
                              () -> null);
        if (result != null) {
            return result;
        } else {
            throw throwable;
        }
    }

    default DisjunctSolo<W, A> orElseUse(Supplier<? extends DisjunctSolo<W, A>> alternativeSupplier) {
        return fold(a -> this,
                    requireNonNull(alternativeSupplier,
                                   () -> "alternativeSupplier"));
    }

}
