package funcify.option.factory;

import static java.util.Objects.requireNonNull;

import funcify.option.Option;
import funcify.option.Option.OptionW;
import funcify.ensemble.Solo;
import funcify.template.solo.FlattenableSoloTemplate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public class OptionFactory implements FlattenableSoloTemplate<OptionW> {

    @Override
    public <B> Solo<OptionW, B> from(final B value) {
        return value == null ? NoneHolder.INSTANCE.getNone() : new Some<>(value);
    }

    @Override
    public <A, B> Solo<OptionW, B> flatMap(final Solo<OptionW, A> container,
                                           final Function<? super A, ? extends Solo<OptionW, B>> flatMapper) {
        return requireNonNull(container,
                              () -> "container").convert(Option::narrowK).<Solo<OptionW, B>>fold(a -> requireNonNull(flatMapper,
                                                                                                                     () -> "flatMapper").andThen(optB -> narrowK(optB))
                                                                                                                                       .apply(a),
                                                                                                 NoneHolder.INSTANCE::getNone);
    }

    private static class Some<T> implements Option<T> {

        private final T value;

        private Some(final T value) {
            this.value = value;
        }


        @Override
        public <B> B fold(final Function<? super T, ? extends B> ifPresent,
                          final Supplier<B> ifAbsent) {
            requireNonNull(ifAbsent,
                           () -> "ifAbsent");
            return requireNonNull(ifPresent,
                                  () -> "ifPresent").apply(value);
        }
    }

    private static class None<T> implements Option<T> {

        @Override
        public <B> B fold(final Function<? super T, ? extends B> ifPresent,
                          final Supplier<B> ifAbsent) {
            requireNonNull(ifPresent,
                           () -> "ifPresent");
            return requireNonNull(ifAbsent,
                                  () -> "ifAbsent").get();
        }
    }

    private static enum NoneHolder {
        INSTANCE(new None<>());

        private final None<Object> objectNone;

        NoneHolder(final None<Object> objectNone) {
            this.objectNone = objectNone;
        }

        @SuppressWarnings("unchecked")
        public <T> None<T> getNone() {
            return (None<T>) objectNone;
        }
    }

}
