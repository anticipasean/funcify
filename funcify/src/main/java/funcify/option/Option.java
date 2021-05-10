package funcify.option;

import funcify.ensemble.Solo;
import funcify.flattenable.FlattenableDisjunctSolo;
import funcify.flattenable.FlattenableSolo;
import funcify.option.Option.OptionW;
import funcify.option.factory.OptionFactory;
import funcify.tuple.Tuple2;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Option<A> extends Iterable<A>, FlattenableDisjunctSolo<OptionW, A> {

    static enum OptionW {

    }

    static <T> Option<T> narrowK(Solo<OptionW, T> wideInstance) {
        return (Option<T>) wideInstance;
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
    default Option<A> empty() {
        return Option.none();
    }

    @Override
    default OptionFactory factory() {
        return OptionFactory.getInstance();
    }

    @Override
    default Option<A> filter(Predicate<? super A> condition) {
        return FlattenableDisjunctSolo.super.filter(condition)
                                            .narrowT1();
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
}
