package funcify.option;

import funcify.design.solo.FlattenableSolo;
import funcify.design.solo.disjunct.DisjunctSolo;
import funcify.design.solo.disjunct.FlattenableDisjunctSolo;
import funcify.ensemble.Solo;
import funcify.option.Option.OptionW;
import funcify.option.factory.OptionFactory;
import funcify.tuple.Tuple2;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Option<A> extends Iterable<A>, FlattenableDisjunctSolo<OptionW, A> {

    static enum OptionW {

    }

    static <T> Option<T> narrowK(Solo<OptionW, T> soloInstance) {
        return (Option<T>) soloInstance;
    }

    static <T> Option<T> of(T value) {
        return OptionFactory.getInstance()
                            .from(value)
                            .narrowT1();
    }

    static <T> Option<T> none() {
        return narrowK(OptionFactory.getInstance()
                                    .empty());
    }

    static <T> Option<T> fromOptional(Optional<T> optional) {
        return of(optional).flatMap(opt -> opt.map(Option::of)
                                              .orElseGet(Option::none));
    }

    @Override
    default <B> Option<B> from(final B value) {
        return factory().from(value)
                        .narrowT1();
    }

    @Override
    default Option<A> empty() {
        return Option.none();
    }

    @Override
    default OptionFactory factory() {
        return OptionFactory.getInstance();
    }

    @Override
    default <B> Option<B> flatMap(final Function<? super A, ? extends FlattenableSolo<OptionW, B>> flatMapper) {
        return FlattenableDisjunctSolo.super.flatMap(flatMapper)
                                            .narrowT1();
    }

    @Override
    default <B> Option<B> map(final Function<? super A, ? extends B> mapper) {
        return FlattenableDisjunctSolo.super.map(mapper)
                                            .narrowT1();
    }

    @Override
    default <B, C> Option<C> zip(final FlattenableSolo<OptionW, B> container2,
                                 final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return FlattenableDisjunctSolo.super.zip(container2,
                                                 combiner)
                                            .narrowT1();
    }

    @Override
    default <B> Option<Tuple2<A, B>> zip(final FlattenableSolo<OptionW, B> container2) {
        return FlattenableDisjunctSolo.super.zip(container2)
                                            .narrowT1();
    }

    @Override
    default Iterator<A> iterator() {
        return factory().toIterator(this);
    }

    @Override
    default Option<A> orElseUse(Supplier<? extends DisjunctSolo<OptionW, A>> alternativeSupplier) {
        return FlattenableDisjunctSolo.super.orElseUse(alternativeSupplier)
                                            .narrowT1();
    }
}
