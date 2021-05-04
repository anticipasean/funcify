package funcify.option;

import funcify.choice.DisjunctSolo;
import funcify.ensemble.FlattenableSolo;
import funcify.ensemble.Solo;
import funcify.option.Option.OptionW;
import funcify.option.factory.OptionFactory;
import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public interface Option<A> extends FlattenableSolo<Option<?>, OptionW, A>, Solo<OptionW, A>, DisjunctSolo<OptionW, A> {

    static enum OptionW {

    }

    static <T> Option<T> narrowK(Solo<OptionW, T> wideInstance) {
        return (Option<T>) wideInstance;
    }

    public static <T> Option<T> of(T value) {
        return narrowK(new OptionFactory().from(value));
    }

    @Override
    default FlattenableSoloTemplate<OptionW> factory() {
        return new OptionFactory();
    }

    @Override
    default <B> Option<B> flatMap(final Function<? super A, ? extends FlattenableSolo<Option<?>, OptionW, B>> flatMapper) {
        return narrowK(FlattenableSolo.super.flatMap(flatMapper));
    }

    @Override
    default <B> Option<B> map(final Function<? super A, ? extends B> mapper) {
        return narrowK(FlattenableSolo.super.map(mapper));
    }

    @Override
    default <B, C> Option<C> zip(final FlattenableSolo<Option<?>, OptionW, B> container2,
                                 final BiFunction<? super A, ? super B, ? extends C> combiner) {
        return narrowK(FlattenableSolo.super.zip(container2,
                                                 combiner));
    }
}
