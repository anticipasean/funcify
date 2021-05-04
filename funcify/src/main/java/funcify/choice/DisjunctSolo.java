package funcify.choice;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface DisjunctSolo<W, A> {

    <B> B fold(Function<? super A, ? extends B> ifPresent,
               Supplier<B> ifAbsent);

    default A orElse(A defaultIfAbsent) {
        return fold(a -> a,
                    () -> defaultIfAbsent);
    }

    default A orElseGet(Supplier<A> defaultSupplierIfAbsent) {
        return fold(a -> a,
                    defaultSupplierIfAbsent);
    }

    default <X extends Throwable> A orElseThrow(X throwable) throws X {
        final A result = fold(a -> a,
                              () -> null);
        if (result != null) {
            return result;
        } else {
            throw throwable;
        }
    }

}
