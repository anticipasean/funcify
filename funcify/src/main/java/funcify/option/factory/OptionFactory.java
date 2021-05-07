package funcify.option.factory;

import static java.util.Objects.requireNonNull;

import funcify.ensemble.Solo;
import funcify.option.Option;
import funcify.option.Option.OptionW;
import funcify.template.solo.FlattenableDisjunctSoloTemplate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author smccarron
 * @created 2021-04-28
 */
public class OptionFactory implements FlattenableDisjunctSoloTemplate<OptionW> {

    private static enum FactoryHolder {
        INSTANCE(new OptionFactory());

        private final OptionFactory optionFactory;

        FactoryHolder(final OptionFactory optionFactory) {
            this.optionFactory = optionFactory;
        }

        public OptionFactory getOptionFactory() {
            return optionFactory;
        }
    }

    private OptionFactory() {
    }

    public static OptionFactory getInstance() {
        return FactoryHolder.INSTANCE.getOptionFactory();
    }

    @Override
    public <B> Solo<OptionW, B> empty() {
        return NoneHolder.INSTANCE.getNone();
    }

    @Override
    public <B> Solo<OptionW, B> from(final B value) {
        return value == null ? NoneHolder.INSTANCE.getNone() : new Some<>(value);
    }

    @Override
    public <A, B> B fold(final Solo<OptionW, A> container,
                         final Function<? super A, ? extends B> ifPresent,
                         final Supplier<B> ifAbsent) {
        return requireNonNull(container,
                              () -> "container").convert(Option::narrowK)
                                                .fold(ifPresent,
                                                      ifAbsent);
    }

    private static class Some<T> implements Option<T> {

        private final T value;

        private Some(final T value) {
            this.value = value;
        }


        @Override
        public <B> B fold(final Function<? super T, ? extends B> ifPresent,
                          final Supplier<? extends B> ifAbsent) {
            requireNonNull(ifAbsent,
                           () -> "ifAbsent");
            return requireNonNull(ifPresent,
                                  () -> "ifPresent").apply(value);
        }
    }

    private static class None<T> implements Option<T> {

        @Override
        public <B> B fold(final Function<? super T, ? extends B> ifPresent,
                          final Supplier<? extends B> ifAbsent) {
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
