package funcify.design.trio.disjunct;

import funcify.ensemble.Trio;
import funcify.option.Option;
import funcify.template.trio.disjunct.DisjunctTrioTemplate;
import funcify.tuple.Tuple3;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-10
 */
public interface DisjunctTrio<W, A, B, C> extends Trio<W, A, B, C> {

    DisjunctTrioTemplate<W> factory();

    Trio<W, A, B, C> first(final A value);

    Trio<W, A, B, C> second(final B value);

    Trio<W, A, B, C> third(final C value);

    <D> D fold(Function<? super A, ? extends D> ifFirst,
               Function<? super B, ? extends D> ifSecond,
               Function<? super C, ? extends D> ifThird);


    default Option<A> getFirst() {
        return fold(Option::of,
                    b -> Option.none(),
                    c -> Option.none());
    }

    default Option<B> getSecond() {
        return fold(a -> Option.none(),
                    Option::of,
                    c -> Option.none());
    }

    default Option<C> getThird() {
        return fold(a -> Option.none(),
                    b -> Option.none(),
                    Option::of);
    }

    default Tuple3<Option<A>, Option<B>, Option<C>> project() {
        return Tuple3.of(fold(Option::of,
                              __ -> Option.none(),
                              __ -> Option.none()),
                         fold(__ -> Option.none(),
                              Option::of,
                              __ -> Option.none()),
                         fold(__ -> Option.none(),
                              __ -> Option.none(),
                              Option::of));
    }

}
