package funcify.template.solo;

import static funcify.template.error.Errable.throwUncheckedThrowable;
import static java.util.Objects.requireNonNull;

import funcify.function.Fn0.ErrableFn0;
import funcify.function.Fn1.ErrableFn1;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface ErrableSoloTemplate<W> {


    default <A, T extends Throwable> A checkedSupplierCall(ErrableFn0<A, T> errableSupplier) throws T {
        try {
            return requireNonNull(errableSupplier,
                                  () -> "errableSupplier").get();
        } catch (Throwable t) {
            throw throwUncheckedThrowable(t);
        }
    }

    default <A, B, T extends Throwable> B checkedFunctionCall(ErrableFn1<A, B, T> errableFunction,
                                                              A inputParameter) throws T {
        try {
            return requireNonNull(errableFunction,
                                  () -> "errableFunction").apply(inputParameter);
        } catch (Throwable t) {
            throw throwUncheckedThrowable(t);
        }
    }

}
