package funcify.tuple;

import funcify.ensemble.Solo;
import funcify.design.solo.conjunct.FlattenableConjunctSolo;
import funcify.design.solo.FlattenableSolo;
import funcify.tuple.Tuple1.Tuple1W;
import funcify.tuple.factory.Tuple1Factory;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface Tuple1<A> extends FlattenableConjunctSolo<Tuple1W, A>, Iterable<A> {

    static enum Tuple1W {

    }

    static <A> Tuple1<A> narrowK(final Solo<Tuple1W, A> wideSoloInstance) {
        return (Tuple1<A>) wideSoloInstance;
    }

    static <A> Tuple1<A> of(final A value) {
        return Tuple1Factory.getInstance()
                            .from(value)
                            .narrowT1();
    }

    A _1();

    @Override
    default Tuple1Factory factory() {
        return Tuple1Factory.getInstance();
    }

    //TODO: append method conjunct solo to duet to trio...

    @Override
    default <B> Tuple1<B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

    @Override
    default <B> Tuple1<B> flatMap(final Function<? super A, ? extends FlattenableSolo<Tuple1W, B>> flatMapper) {
        return factory().flatMap(this,
                                 flatMapper)
                        .narrowT1();
    }

    static <A> Tuple1<A> flatten(Tuple1<Tuple1<A>> container) {
        return Tuple1Factory.getInstance()
                            .flatten(Solo.widenP(container))
                            .narrowT1();
    }

    @Override
    default <B> Tuple1<B> map(final Function<? super A, ? extends B> mapper) {
        return factory().map(this,
                             mapper)
                        .narrowT1();
    }

    @Override
    default <B, C> Tuple1<C> zip(final FlattenableSolo<Tuple1W, B> container2,
                                 final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return factory().zip(this,
                             container2,
                             combiner)
                        .narrowT1();
    }

    @Override
    default Iterator<A> iterator() {
        return factory().toIterator(this);
    }
}
