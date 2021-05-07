package funcify.attempt.factory;

import static java.util.Objects.requireNonNull;

import funcify.attempt.Try;
import funcify.attempt.Try.TryW;
import funcify.ensemble.Duet;
import funcify.function.Fn0.ErrableFn0;
import funcify.template.duet.FlattenableDisjunctDuetTemplate;
import funcify.template.error.Errable;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-05-04
 */
public class TryFactory implements FlattenableDisjunctDuetTemplate<TryW> {

    private static enum FactoryHolder {
        INSTANCE(new TryFactory());

        private final TryFactory tryFactory;

        FactoryHolder(final TryFactory tryFactory) {
            this.tryFactory = tryFactory;
        }

        public TryFactory getTryFactory() {
            return tryFactory;
        }
    }

    private TryFactory() {

    }

    public static TryFactory getInstance() {
        return FactoryHolder.INSTANCE.getTryFactory();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> Duet<TryW, A, B> first(final A value1) {
        try {
            return (Duet<TryW, A, B>) narrowFirst(value1);
        } catch (final Throwable t) {
            return (Duet<TryW, A, B>) narrowSecond(t);
        }
    }

    private <A, B extends Throwable> Duet<TryW, A, B> narrowFirst(final A value1) {
        return new Success<>(requireNonNull(value1,
                                            () -> "value1"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A, B> Duet<TryW, A, B> second(final B value2) {
        try {
            if (value2 instanceof Throwable) {
                return (Duet<TryW, A, B>) narrowSecond((Throwable) value2);
            } else {
                throw new IllegalArgumentException("value2 must be a throwable");
            }
        } catch (final Throwable t) {
            return (Duet<TryW, A, B>) narrowSecond(t);
        }
    }

    private <A, B extends Throwable> Duet<TryW, A, B> narrowSecond(final B value2) {
        return new Failure<>(requireNonNull(value2,
                                            () -> "value2"));
    }


    public final <A> Duet<TryW, A, Throwable> catching(final ErrableFn0<A, Throwable> errableOperation) {
        try {
            return first(requireNonNull(errableOperation,
                                        () -> "errableOperation").get());
        } catch (final Throwable t) {
            return second(t);
        }
    }

    @SafeVarargs
    public final <A, T extends Throwable> Duet<TryW, A, T> catching(final ErrableFn0<A, T> errableOperation,
                                                                    final Class<? extends Throwable>... allowedErrorTypes) {
        try {
            return this.<A, T>first(requireNonNull(errableOperation,
                                                   () -> "errableOperation").get());
        } catch (final Throwable t) {
            if (allowedErrorTypes == null || allowedErrorTypes.length == 0) {
                return Duet.narrowP(this.<A, Throwable>second(t));
            }
            final Optional<Class<? extends Throwable>> errorTypeMatch = Stream.of(allowedErrorTypes)
                                                                              .filter(tType -> tType.isAssignableFrom(t.getClass()))
                                                                              .findFirst();
            if (errorTypeMatch.isPresent()) {
                return Duet.narrowP(this.<A, Throwable>second(t));
            } else {
                throw Errable.throwUncheckedThrowable(t);
            }
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <A, B, C> C fold(final Duet<TryW, A, B> container,
                            final Function<? super A, ? extends C> ifFirstPresent,
                            final Function<? super B, ? extends C> ifSecondPresent) {
        if (container instanceof Success) {
            return ((Success<A, Throwable>) container).fold(ifFirstPresent,
                                                            b -> null);
        } else {
            try {
                return ((Failure<A, Throwable>) container).fold(a -> null,
                                                                t -> requireNonNull(ifSecondPresent,
                                                                                    () -> "ifSecondPresent").apply((B) t));
            } catch (final Throwable t) {
                throw Errable.throwUncheckedThrowable(t);
            }
        }
    }

    private static class Success<A, T extends Throwable> implements Try<A, T> {

        private final A value;

        private Success(final A value) {
            this.value = value;
        }

        @Override
        public <C> C fold(final Function<? super A, ? extends C> ifFirstPresent,
                          final Function<? super T, ? extends C> ifSecondPresent) {
            requireNonNull(ifSecondPresent,
                           () -> "ifSecondPresent");
            return requireNonNull(ifFirstPresent,
                                  () -> "ifFirstPresent").apply(value);
        }
    }

    private static class Failure<A, T extends Throwable> implements Try<A, T> {

        private final T throwable;

        private Failure(final T throwable) {
            this.throwable = throwable;
        }

        @Override
        public <C> C fold(final Function<? super A, ? extends C> ifFirstPresent,
                          final Function<? super T, ? extends C> ifSecondPresent) {
            requireNonNull(ifFirstPresent,
                           () -> "ifFirstPresent");
            return requireNonNull(ifSecondPresent,
                                  () -> "ifSecondPresent").apply(throwable);
        }
    }
}
