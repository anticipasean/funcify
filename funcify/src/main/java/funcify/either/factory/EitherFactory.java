package funcify.either.factory;

import static java.util.Objects.requireNonNull;

import funcify.either.Either;
import funcify.either.Either.EitherW;
import funcify.ensemble.Duet;
import funcify.template.duet.FlattenableDisjunctDuetTemplate;
import funcify.template.duet.IterableDisjunctDuetTemplate;
import java.util.function.Function;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-05-06
 */
public class EitherFactory implements FlattenableDisjunctDuetTemplate<EitherW>, IterableDisjunctDuetTemplate<EitherW> {

    private static enum FactoryHolder {
        INSTANCE(new EitherFactory());

        private final EitherFactory eitherFactory;

        FactoryHolder(final EitherFactory eitherFactory) {
            this.eitherFactory = eitherFactory;
        }

        public EitherFactory getEitherFactory() {
            return eitherFactory;
        }
    }

    private EitherFactory() {

    }

    public static EitherFactory getInstance() {
        return FactoryHolder.INSTANCE.getEitherFactory();
    }

    @Override
    public <A, B, C> C fold(final Duet<EitherW, A, B> container,
                            final Function<? super A, ? extends C> ifFirstPresent,
                            final Function<? super B, ? extends C> ifSecondPresent) {
        return requireNonNull(container,
                              () -> "container").convert(Either::narrowK)
                                                .fold(ifFirstPresent,
                                                      ifSecondPresent);
    }

    @Override
    public <A, B> Duet<EitherW, A, B> first(final A value1) {
        return new Left<>(requireNonNull(value1,
                                         () -> "value1 [left]"));
    }

    @Override
    public <A, B> Duet<EitherW, A, B> second(final B value2) {
        return new Right<>(requireNonNull(value2,
                                          () -> "value2 [right]"));
    }

    @AllArgsConstructor
    private static class Left<L, R> implements Either<L, R> {

        private final L leftValue;

        @Override
        public <C> C fold(final Function<? super L, ? extends C> ifFirstPresent,
                          final Function<? super R, ? extends C> ifSecondPresent) {
            requireNonNull(ifSecondPresent,
                           () -> "ifSecondPresent");
            return requireNonNull(ifFirstPresent,
                                  () -> "ifFirstPresent").apply(leftValue);
        }
    }

    @AllArgsConstructor
    private static class Right<L, R> implements Either<L, R> {

        private final R rightValue;

        @Override
        public <C> C fold(final Function<? super L, ? extends C> ifFirstPresent,
                          final Function<? super R, ? extends C> ifSecondPresent) {
            requireNonNull(ifFirstPresent,
                           () -> "ifFirstPresent");
            return requireNonNull(ifSecondPresent,
                                  () -> "ifSecondPresent").apply(rightValue);
        }
    }
}
