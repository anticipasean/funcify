package funcify.tuple;

import funcify.ensemble.Duet;
import funcify.ensemble.Solo;
import funcify.flattenable.FlattenableConjunctDuet;
import funcify.flattenable.FlattenableDuet;
import funcify.tuple.Tuple2.Tuple2W;
import funcify.tuple.factory.Tuple2Factory;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-05-05
 */
public interface Tuple2<A, B> extends FlattenableConjunctDuet<Tuple2W, A, B>, Iterable<Tuple2<A, B>> {

    static enum Tuple2W {

    }

    static <A, B> Tuple2<A, B> of(final A parameter1,
                                  final B parameter2) {
        return Tuple2Factory.getInstance()
                            .both(parameter1,
                                  parameter2)
                            .narrowT1();
    }

    static <A, B> Tuple2<A, B> narrowK(Solo<Solo<Tuple2W, A>, B> wideDuetInstance) {
        return (Tuple2<A, B>) wideDuetInstance;
    }

    static <A, B> Tuple2<A, B> narrowK(Duet<Tuple2W, A, B> wideDuetInstance) {
        return (Tuple2<A, B>) wideDuetInstance;
    }

    @Override
    default Tuple2Factory factory() {
        return Tuple2Factory.getInstance();
    }

    A _1();

    B _2();

    default Tuple1<A> first() {
        return Tuple1.of(_1());
    }

    default Tuple1<B> second() {
        return Tuple1.of(_2());
    }

    @Override
    default <C, D> Tuple2<C, D> flatMap(final BiFunction<? super A, ? super B, ? extends FlattenableConjunctDuet<Tuple2W, C, D>> flatMapper) {
        return FlattenableConjunctDuet.super.flatMap(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <C> Tuple2<C, B> mapFirst(final Function<? super A, ? extends C> mapper) {
        return FlattenableConjunctDuet.super.mapFirst(mapper)
                                            .narrowT2();
    }

    @Override
    default <C> Tuple2<A, C> mapSecond(final Function<? super B, ? extends C> mapper) {
        return FlattenableConjunctDuet.super.mapSecond(mapper)
                                            .narrowT1();
    }

    @Override
    default <C, D> Tuple2<C, D> bimap(final Function<? super A, ? extends C> mapper1,
                                      final Function<? super B, ? extends D> mapper2) {
        return FlattenableConjunctDuet.super.bimap(mapper1,
                                                   mapper2)
                                            .narrowT2();
    }

    @Override
    default <C> Tuple2<C, B> zipFirst(final FlattenableDuet<Tuple2W, A, B> container,
                                      final BiFunction<? super A, ? super A, ? extends C> combiner) {
        return FlattenableConjunctDuet.super.zipFirst(container,
                                                      combiner)
                                            .narrowT2();

    }

    @Override
    default <C> Tuple2<A, C> zipSecond(final FlattenableDuet<Tuple2W, A, B> container,
                                       final BiFunction<? super B, ? super B, ? extends C> combiner) {
        return FlattenableConjunctDuet.super.zipSecond(container,
                                                       combiner)
                                            .narrowT1();

    }

    @Override
    default <C, D, E, F> Tuple2<E, F> zipBoth(final FlattenableConjunctDuet<Tuple2W, C, D> container,
                                              final BiFunction<? super A, ? super C, ? extends E> combiner1,
                                              final BiFunction<? super B, ? super D, ? extends F> combiner2) {
        return FlattenableConjunctDuet.super.zipBoth(container,
                                                     combiner1,
                                                     combiner2)
                                            .narrowT2();
    }

    @Override
    default <C> Tuple2<C, B> flatMapFirst(final Function<? super A, ? extends FlattenableDuet<Tuple2W, C, B>> flatMapper) {
        return FlattenableConjunctDuet.super.flatMapFirst(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <C> Tuple2<A, C> flatMapSecond(final Function<? super B, ? extends FlattenableDuet<Tuple2W, A, C>> flatMapper) {
        return FlattenableConjunctDuet.super.flatMapSecond(flatMapper)
                                            .narrowT1();
    }

    @Override
    default Iterator<Tuple2<A, B>> iterator() {
        return factory().toIterator(this);
    }
}
