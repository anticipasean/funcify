package funcify.function;

import funcify.ensemble.Quartet;
import funcify.function.Fn3.Fn3W;

/**
 * @author smccarron
 * @created 2021-05-07
 */
@FunctionalInterface
public interface Fn3<A, B, C, D> extends Quartet<Fn3W, A, B, C, D> {

    static enum Fn3W {

    }

    D apply(A a,
            B b,
            C c);


}
