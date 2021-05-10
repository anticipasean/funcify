package funcify.flattenable;

import funcify.conjunct.ConjunctDuet;
import funcify.conjunct.ConjunctSolo;
import funcify.conjunct.ConjunctTrio;
import funcify.ensemble.Duet;
import funcify.function.Fn1;
import funcify.function.Fn3;
import funcify.template.duet.FlattenableConjunctDuetTemplate;
import funcify.tuple.Tuple1;
import funcify.tuple.Tuple3;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public interface FlattenableConjunctDuet<W, A, B> extends ConjunctDuet<W, A, B>, FlattenableDuet<W, A, B> {

    FlattenableConjunctDuetTemplate<W> factory();

    default <C, D> FlattenableConjunctDuet<W, C, D> flatMap(final BiFunction<? super A, ? super B, ? extends FlattenableConjunctDuet<W, C, D>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    default <C, D> FlattenableConjunctDuet<W, C, D> bimap(final Function<? super A, ? extends C> mapper1,
                                                          final Function<? super B, ? extends D> mapper2) {
        return factory().bimap(this,
                               mapper1,
                               mapper2)
                        .narrowT2();
    }

    default <C, D, E, F> FlattenableConjunctDuet<W, E, F> zipBoth(final FlattenableConjunctDuet<W, C, D> container,
                                                                  final BiFunction<? super A, ? super C, ? extends E> combiner1,
                                                                  final BiFunction<? super B, ? super D, ? extends F> combiner2) {
        return factory().zipBoth(this,
                                 container,
                                 combiner1,
                                 combiner2)
                        .narrowT2();
    }

    default <W3, C> ConjunctTrio<W3, A, B, C> append(C value,
                                                     Fn3<? super A, ? super B, ? super C, ? extends ConjunctTrio<W3, A, B, C>> mapper) {
        return factory().append(this,
                                value,
                                mapper);
    }

    default <W3, C> ConjunctTrio<W3, C, A, B> prepend(C value,
                                                      Fn3<? super C, ? super A, ? super B, ? extends ConjunctTrio<W3, C, A, B>> mapper) {
        return factory().prepend(this,
                                 value,
                                 mapper);
    }

    default <W1> ConjunctSolo<W1, A> dropRight(Fn1<? super A, ? extends ConjunctSolo<W1, A>> mapper) {
        return factory().dropRight(this,
                                   mapper);
    }

    default <W1> ConjunctSolo<W1, B> dropLeft(Fn1<? super B, ? extends ConjunctSolo<W1, B>> mapper) {
        return factory().dropLeft(this,
                                  mapper);
    }

    default <C> Tuple3<A, B, C> append(C value) {
        return factory().append(this,
                                value,
                                Tuple3::of);
    }

    default <C> Tuple3<C, A, B> prepend(C value) {
        return factory().prepend(this,
                                 value,
                                 Tuple3::of);
    }

    default Tuple1<A> dropRight() {
        return factory().dropRight(this,
                                   Tuple1::of);
    }

    default Tuple1<B> dropLeft() {
        return factory().dropLeft(this,
                                  Tuple1::of);
    }

    default Duet<W, B, A> swap() {
        return factory().swap(this);
    }
}
