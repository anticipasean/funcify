package funcify.function;

import funcify.ensemble.Quintet;
import funcify.ensemble.Solo;
import funcify.function.Fn4.Fn4W;

/**
 * @author smccarron
 * @created 2021-05-11
 */
@FunctionalInterface
public interface Fn4<A, B, C, D, E> extends Quintet<Fn4W, A, B, C, D, E> {

    static enum Fn4W {

    }

    static <A, B, C, D, E> Fn4<A, B, C, D, E> narrowK(Quintet<Fn4W, A, B, C, D, E> quintetInstance) {
        return (Fn4<A, B, C, D, E>) quintetInstance;
    }

    static <A, B, C, D, E> Fn4<A, B, C, D, E> narrowK(Solo<Solo<Solo<Solo<Solo<Fn4W, A>, B>, C>, D>, E> quintetInstance) {
        return (Fn4<A, B, C, D, E>) quintetInstance;
    }


    E apply(A a,
            B b,
            C c,
            D d);

}
