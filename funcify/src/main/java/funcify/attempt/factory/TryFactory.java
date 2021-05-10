package funcify.attempt.factory;

import static java.util.Objects.requireNonNull;

import funcify.attempt.Try;
import funcify.attempt.Try.TryW;
import funcify.ensemble.Duet;
import funcify.function.Fn0;
import funcify.function.Fn0.ErrableFn0;
import funcify.function.Fn1;
import funcify.function.Fn1.ErrableFn1;
import funcify.option.Option;
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

    private <A> Duet<TryW, A, Throwable> narrowFirst(final A value1) {
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
                throw new IllegalArgumentException("value2 must be a throwable for Try instance creation");
            }
        } catch (final Throwable t) {
            return (Duet<TryW, A, B>) narrowSecond(t);
        }
    }

    private <A, B extends Throwable> Duet<TryW, A, Throwable> narrowSecond(final B value2) {
        return new Failure<>(requireNonNull(value2,
                                            () -> "value2"));
    }

    public final <A> Duet<TryW, A, Throwable> catching(final Fn0<? extends A> errableOperation) {
        try {
            return first(requireNonNull(errableOperation,
                                        () -> "errableOperation").get());
        } catch (final Throwable t) {
            return second(t);
        }
    }

//    public final <A> Duet<TryW, A, Throwable> catching(final ErrableFn0<? extends A, Throwable> errableOperation) {
//        try {
//            return first(requireNonNull(errableOperation,
//                                        () -> "errableOperation").get());
//        } catch (final Throwable t) {
//            return second(t);
//        }
//    }

    @SafeVarargs
    public final <A, T extends Throwable> Duet<TryW, A, Throwable> catching(final ErrableFn0<? extends A, T> errableOperation,
                                                                            final Class<? extends Throwable>... allowedErrorTypes) {
        try {
            return this.<A, Throwable>first(requireNonNull(errableOperation,
                                                           () -> "errableOperation").get());
        } catch (final Throwable t) {
            if (allowedErrorTypes == null || allowedErrorTypes.length == 0) {
                return this.<A, Throwable>second(t);
            }
            final Optional<Class<? extends Throwable>> errorTypeMatch = Stream.of(allowedErrorTypes)
                                                                              .filter(tType -> tType.isAssignableFrom(t.getClass()))
                                                                              .findFirst();
            if (errorTypeMatch.isPresent()) {
                return this.<A, Throwable>second(t);
            } else {
                throw Errable.throwUncheckedThrowable(t);
            }
        }
    }

    @SafeVarargs
    public final <A, B extends Throwable, C> Duet<TryW, C, Throwable> flatMapCatchingOnly(final Duet<TryW, A, B> container,
                                                                                          final Function<? super A, ? extends Duet<TryW, C, Throwable>> flatMapper,
                                                                                          final Class<? extends Throwable>... allowedErrorType) {
        return fold(container,
                    (A a) -> {
                        return requireNonNull(flatMapper,
                                              () -> "flatMapper").apply(a);
                    },
                    (B b) -> {
                        return Option.fromOptional(Option.of(allowedErrorType)
                                                         .map(Stream::of)
                                                         .orElseGet(Stream::empty)
                                                         .filter(xType -> xType.isAssignableFrom(b.getClass()))
                                                         .findFirst())
                                     .map(xType -> xType.cast(b))
                                     .map(this::<C, Throwable>second)
                                     .orElseThrow(b);
                    });
    }

    @SafeVarargs
    public final <A, B extends Throwable, C> Duet<TryW, C, Throwable> mapCatchingOnly(final Duet<TryW, A, B> container,
                                                                                      final Fn1<? super A, ? extends C> mapper,
                                                                                      final Class<? extends Throwable>... allowedErrorType) {
        return fold(container,
                    (A a) -> {
                        return flatMapCatchingOnly(container,
                                                   (A paramA) -> this.<C>catching(Fn0.of(() -> requireNonNull(mapper,
                                                                                                              () -> "mapper").apply(paramA))).narrowT1(),
                                                   allowedErrorType);
                    },
                    (B b) -> {
                        return Option.fromOptional(Option.of(allowedErrorType)
                                                         .map(Stream::of)
                                                         .orElseGet(Stream::empty)
                                                         .filter(xType -> xType.isAssignableFrom(b.getClass()))
                                                         .findFirst())
                                     .map(xType -> xType.cast(b))
                                     .map(this::<C, Throwable>second)
                                     .orElseThrow(b);
                    });
    }


    @SuppressWarnings("unchecked")
    public <A, B extends Throwable, C> Duet<TryW, C, B> checkedMap(final Duet<TryW, A, B> container,
                                                                   final ErrableFn1<? super A, ? extends C, ? extends B> mapper) {
        return fold(container,
                    (A paramA) -> {
                        try {
                            return first(requireNonNull(mapper,
                                                        () -> "mapper").apply(paramA));
                        } catch (final Throwable b) {
                            return this.<C, B>second((B) b);
                        }
                    },
                    this::<C, B>second);
    }

    @SuppressWarnings("unchecked")
    public <A, B extends Throwable, C> Duet<TryW, C, B> checkedFlatMap(final Duet<TryW, A, B> container,
                                                                       final ErrableFn1<? super A, ? extends Try<C>, ? extends B> flatMapper) {
        return fold(container,
                    (A a) -> {
                        try {
                            return (Duet<TryW, C, B>) requireNonNull(flatMapper,
                                                                     () -> "flatMapper").apply(a);
                        } catch (Throwable b) {
                            return this.<C, B>second((B) b);
                        }
                    },
                    this::<C, B>second);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <A, B, C> C fold(final Duet<TryW, A, B> container,
                            final Function<? super A, ? extends C> ifFirstPresent,
                            final Function<? super B, ? extends C> ifSecondPresent) {
        if (container instanceof Success) {
            return ((Success<A>) container).fold(ifFirstPresent,
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

    private static class Success<S> implements Try<S> {

        private final S value;

        private Success(final S value) {
            this.value = value;
        }

        @Override
        public <C> C fold(final Function<? super S, ? extends C> ifFirstPresent,
                          final Function<? super Throwable, ? extends C> ifSecondPresent) {
            requireNonNull(ifSecondPresent,
                           () -> "ifSecondPresent");
            return requireNonNull(ifFirstPresent,
                                  () -> "ifFirstPresent").apply(value);
        }
    }

    private static class Failure<S, F extends Throwable> implements Try<S> {

        private final F throwable;

        private Failure(final F throwable) {
            this.throwable = throwable;
        }

        @Override
        public <C> C fold(final Function<? super S, ? extends C> ifFirstPresent,
                          final Function<? super Throwable, ? extends C> ifSecondPresent) {
            requireNonNull(ifFirstPresent,
                           () -> "ifFirstPresent");
            return requireNonNull(ifSecondPresent,
                                  () -> "ifSecondPresent").apply(throwable);
        }
    }
}
