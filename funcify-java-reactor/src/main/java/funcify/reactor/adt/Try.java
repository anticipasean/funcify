package funcify.reactor.adt;

import funcify.reactor.adt.TryFactory.Failure;
import funcify.reactor.adt.TryFactory.Success;
import funcify.reactor.reflect.TypeOps;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Design pattern for chaining many operations together where some or all of which with the potential of throwing exceptions along
 * the way. Enables callers to avoid introducing CompletableFuture / CompletionStage to a section of code unless they wish to have
 * these operations take place asynchronously or on separate threads, in which case it would be better to use those JVM features
 * where an async executor can be implied or supplied as a parameter
 *
 * @author smccarron
 * @created 2021-08-05
 */
public interface Try<S> {

    static <S> Try<S> success(final S successfulResult) {
        return Success.of(Objects.requireNonNull(successfulResult, () -> {
            return String.format("%s.successful_result value is null; use #nullableSuccess methods if result can be null",
                                 Try.class.getSimpleName());
        }));
    }

    static <S> Try<S> failure(final Throwable throwable) {
        return Failure.of(Objects.requireNonNull(throwable, () -> "throwable"));
    }

    static <S> Try<S> nullableSuccess(final S successfulResult) {
        return nullableSuccess(successfulResult, () -> new NoSuchElementException("result is null"));
    }

    static <S> Try<S> nullableSuccess(final S nullableSuccessObject,
                                      final Supplier<? extends Throwable> ifNull) {
        Objects.requireNonNull(ifNull, () -> "ifNull");
        return Optional.ofNullable(nullableSuccessObject).map(Try::success).orElseGet(() -> Try.nullableFailure(ifNull.get()));
    }

    static <S> Try<S> nullableFailure(final Throwable throwable) {
        return Try.failure(Optional.ofNullable(throwable).orElseGet(() -> new NoSuchElementException("throwable")));
    }

    static Try<Empty> emptySuccess() {
        return Try.success(Empty.INSTANCE);
    }

    static <F extends Throwable> Try<Empty> attempt(final ErrableRunnable<F> function) {
        Objects.requireNonNull(function, () -> "function");
        try {
            function.apply();
            return Try.success(Empty.INSTANCE);
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    static <S, F extends Throwable> Try<S> attempt(final ErrableFn0<S, F> function) {
        Objects.requireNonNull(function, () -> "function");
        try {
            return Try.success(function.apply());
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    static <S, F extends Throwable> Try<Optional<S>> attemptNullable(final ErrableFn0<S, F> function) {
        Objects.requireNonNull(function, () -> "function");
        try {
            return Try.success(Optional.ofNullable(function.apply()));
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    static <I, O> Function<I, Try<O>> lift(final Function<? super I, ? extends O> function) {
        Objects.requireNonNull(function, () -> "function");
        return input -> {
            try {
                return Try.success(function.apply(input));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I, O> Function<I, Try<O>> liftNullable(final Function<? super I, ? extends O> function,
                                                   final Function<? super I, ? extends Throwable> ifNullResult) {
        Objects.requireNonNull(function, () -> "function");
        Objects.requireNonNull(ifNullResult, () -> "ifNullResult");
        return input -> {
            try {
                final Optional<? extends O> result = Optional.ofNullable(function.apply(input));
                if (!result.isPresent()) {
                    return Try.failure(ifNullResult.apply(input));
                }
                return Try.success(result.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I, O> Function<I, Try<O>> liftNullable(final Function<? super I, ? extends O> function) {
        return liftNullable(function, input -> {
            final String message = String.format("input [ type: %s ] resulted in null value for function",
                                                 input == null ? "null" : input.getClass().getName());
            return new IllegalArgumentException(message);
        });
    }

    static <I1, I2, O> BiFunction<I1, I2, Try<O>> lift(final BiFunction<? super I1, ? super I2, ? extends O> function) {
        Objects.requireNonNull(function, () -> "function");
        return (i1, i2) -> {
            try {
                return Try.success(function.apply(i1, i2));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I1, I2, O> BiFunction<I1, I2, Try<O>> liftNullable(final BiFunction<? super I1, ? super I2, ? extends O> function,
                                                               final BiFunction<? super I1, ? super I2, ? extends Throwable> ifNullResult) {
        Objects.requireNonNull(function, () -> "function");
        Objects.requireNonNull(ifNullResult, () -> "ifNullResult");
        return (i1, i2) -> {
            try {
                final Optional<? extends O> result = Optional.ofNullable(function.apply(i1, i2));
                if (!result.isPresent()) {
                    return Try.failure(ifNullResult.apply(i1, i2));
                }
                return Try.success(result.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I1, I2, O> BiFunction<I1, I2, Try<O>> liftNullable(final BiFunction<? super I1, ? super I2, ? extends O> function) {
        return liftNullable(function, (i1, i2) -> {
            final String message = String.format("inputs [ i1.type: %s, i2.type: %s ] resulted in null value for function",
                                                 i1 == null ? "null" : i1.getClass().getName(),
                                                 i2 == null ? "null" : i2.getClass().getName());
            return new IllegalArgumentException(message);
        });
    }

    static <I, O, F extends Throwable> Function<I, Try<O>> liftErrable(final ErrableFn1<? super I, ? extends O, F> function) {
        Objects.requireNonNull(function, () -> "function");
        return input -> {
            try {
                return Try.success(function.apply(input));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I1, I2, O, F extends Throwable> BiFunction<I1, I2, Try<O>> liftErrable(final ErrableFn2<? super I1, ? super I2, ? extends O, F> function) {
        Objects.requireNonNull(function, () -> "function");
        return (i1, i2) -> {
            try {
                return Try.success(function.apply(i1, i2));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I, F extends Throwable> Function<I, Try<Empty>> liftErrableConsumer(final ErrableConsumer1<? super I, F> function) {
        Objects.requireNonNull(function, () -> "function");
        return input -> {
            try {
                function.apply(input);
                return Try.emptySuccess();
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    static <I1, I2, F extends Throwable> BiFunction<I1, I2, Try<Empty>> liftErrableBiConsumer(final ErrableConsumer2<? super I1, ? super I2, F> function) {
        Objects.requireNonNull(function, () -> "function");
        return (input1, input2) -> {
            try {
                function.apply(input1, input2);
                return Try.emptySuccess();
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        };
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <S> Try<S> fromOptional(final Optional<? extends S> optional) {
        Objects.requireNonNull(optional, () -> "optional");
        try {
            return Try.success(optional.get());
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <S, F extends Throwable> Try<S> fromOptional(final Optional<? extends S> optional,
                                                        final Function<NoSuchElementException, F> onOptionalEmpty) {
        Objects.requireNonNull(optional, () -> "optional");
        Objects.requireNonNull(onOptionalEmpty, () -> "onOptionalEmpty");
        try {
            return Try.success(optional.get());
        } catch (final NoSuchElementException t) {
            return Try.failure(onOptionalEmpty.apply(t));
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <S> Try<S> fromOptional(final Optional<? extends S> optional,
                                   final Supplier<? extends S> ifEmpty) {
        Objects.requireNonNull(optional, () -> "optional");
        Objects.requireNonNull(ifEmpty, () -> "ifEmpty");
        try {
            S s = (s = optional.orElse(null)) == null ? ifEmpty.get() : s;
            return Try.success(s);
        } catch (final Throwable t) {
            return Try.failure(t);
        }
    }

    static <T extends Try<? extends S>, S> Try<S> narrow(final T tryInstance) {
        @SuppressWarnings("unchecked") final Try<S> narrowedInstance = (Try<S>) tryInstance;
        return narrowedInstance;
    }

    static <S, F extends Throwable> Try<S> attemptRetryable(final ErrableFn0<S, F> function,
                                                            final int numberOfRetries) {
        Objects.requireNonNull(function, () -> "function");
        if (numberOfRetries < 0) {
            final String message = String.format("number_of_retries must be greater than or equal to 0: [ actual: %d ]",
                                                 numberOfRetries);
            return Try.failure(new IllegalArgumentException(message));
        }
        Try<S> attempt = Try.attempt(function);
        for (int i = 0; attempt.isFailure() && i < numberOfRetries; i++) {
            attempt = Try.attempt(function);
            if (attempt.isSuccess()) {
                return attempt;
            }
        }
        return attempt;
    }

    static <S, F extends Throwable> Try<S> attemptRetryableIf(final ErrableFn0<S, F> function,
                                                              final int numberOfRetries,
                                                              final Predicate<? super Throwable> failureCondition) {
        Objects.requireNonNull(function, () -> "function");
        Objects.requireNonNull(failureCondition, () -> "failureCondition");
        if (numberOfRetries < 0) {
            final String message = String.format("number_of_retries must be greater than or equal to 0: [ actual: %d ]",
                                                 numberOfRetries);
            return Try.failure(new IllegalArgumentException(message));
        }
        Try<S> attempt = Try.attempt(function);
        for (int i = 0; attempt.isFailure() && i < numberOfRetries; i++) {
            attempt = Try.attempt(function);
            if (attempt.isSuccess()) {
                return attempt;
            } else if (!attempt.getFailure().filter(failureCondition).isPresent()) {
                return attempt;
            }
        }
        return attempt;
    }

    static <S, F extends Throwable> Try<S> attemptWithTimeout(final ErrableFn0<S, F> errableFunction,
                                                              final long timeout,
                                                              final TimeUnit unit) {
        Objects.requireNonNull(errableFunction, () -> "errableFunction");
        Objects.requireNonNull(unit, () -> "unit");
        final long validatedTimeout = Math.max(0, timeout);
        try {
            return CompletableFuture.supplyAsync(() -> Try.attempt(errableFunction)).get(validatedTimeout, unit);
        } catch (final Throwable t) {
            if (t instanceof TimeoutException) {
                final String message = String.format("attempt_with_timeout: [ operation reached limit of %d %s ]",
                                                     validatedTimeout,
                                                     unit.name().toLowerCase());
                return Try.failure(new TimeoutException(message));
            }
            if (t instanceof CompletionException) {
                Throwable thr = t;
                while (thr instanceof CompletionException) {
                    thr = thr.getCause();
                }
                return Try.failure(thr);
            }
            return Try.failure(t);
        }
    }

    static <S, F extends Throwable> Try<Optional<S>> attemptNullableWithTimeout(final ErrableFn0<S, F> errableFunction,
                                                                                final long timeout,
                                                                                final TimeUnit unit) {
        Objects.requireNonNull(errableFunction, () -> "errableFunction");
        Objects.requireNonNull(unit, () -> "unit");
        final long validatedTimeout = Math.max(0, timeout);
        try {
            return CompletableFuture.supplyAsync(() -> Try.attemptNullable(errableFunction)).get(validatedTimeout, unit);
        } catch (final Throwable t) {
            if (t instanceof TimeoutException) {
                final String message = String.format("attempt_with_timeout: [ operation reached limit of %d %s ]",
                                                     validatedTimeout,
                                                     unit.name().toLowerCase());
                return Try.failure(new TimeoutException(message));
            }
            if (t instanceof CompletionException) {
                Throwable thr = t;
                while (thr instanceof CompletionException) {
                    thr = thr.getCause();
                }
                return Try.failure(thr);
            }
            return Try.failure(t);
        }
    }

    default boolean isSuccess() {
        return fold(s -> true, f -> false);
    }

    default boolean isFailure() {
        return fold(s -> false, f -> true);
    }

    default Optional<S> getSuccess() {
        return fold(Optional::of, t -> Optional.empty());
    }

    default Optional<Throwable> getFailure() {
        return fold(s -> Optional.empty(), Optional::ofNullable);
    }

    default <F extends Throwable> Try<S> filter(final Predicate<? super S> condition,
                                                final Supplier<F> unmetConditionFailureSupplier) {
        Objects.requireNonNull(condition, () -> "condition");
        Objects.requireNonNull(unmetConditionFailureSupplier, () -> "unmetConditionFailureSupplier");
        return fold(s -> {
            try {
                if (condition.test(s)) {
                    return Try.success(s);
                }
                return Try.failure(unmetConditionFailureSupplier.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <T, F extends Throwable> Try<T> ofType(final Class<T> targetType,
                                                   final Supplier<F> unmetConditionFailureSupplier) {
        Objects.requireNonNull(targetType, () -> "targetType");
        Objects.requireNonNull(unmetConditionFailureSupplier, () -> "unmetConditionFailureSupplier");
        return fold(s -> TypeOps.toType(s, targetType).map(Try::success).orElseGet(() -> {
            try {
                return Try.failure(unmetConditionFailureSupplier.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }), Try::failure);
    }

    default <F extends Throwable> Try<S> filter(final Predicate<? super S> condition,
                                                final Function<? super S, ? extends F> unmetConditionFailureMapper) {
        Objects.requireNonNull(condition, () -> "condition");
        Objects.requireNonNull(unmetConditionFailureMapper, () -> "unmetConditionFailureMapper");
        return fold(s -> {
            try {
                if (condition.test(s)) {
                    return Try.success(s);
                }
                return Try.failure(unmetConditionFailureMapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <F extends Throwable> Try<Optional<S>> filter(final Predicate<? super S> condition) {
        Objects.requireNonNull(condition, () -> "condition");
        return fold(s -> {
            try {
                if (condition.test(s)) {
                    return Try.success(Optional.of(s));
                }
                return Try.success(Optional.empty());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <T, F extends Throwable> Try<T> ofType(final Class<T> targetType,
                                                   final Function<? super S, ? extends F> unmetConditionFailureMapper) {
        Objects.requireNonNull(targetType, () -> "targetType");
        Objects.requireNonNull(unmetConditionFailureMapper, () -> "unmetConditionFailureMapper");
        return fold(s -> TypeOps.toType(s, targetType).map(Try::success).orElseGet(() -> {
            try {
                return Try.failure(unmetConditionFailureMapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }), Try::failure);
    }

    default <R> Try<R> map(final Function<? super S, ? extends R> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                return Try.success(mapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R> Try<R> mapNullable(final Function<? super S, ? extends R> mapper,
                                   final Supplier<? extends R> nonNullResultSupplier) {
        Objects.requireNonNull(mapper, () -> "mapper");
        Objects.requireNonNull(nonNullResultSupplier, () -> "nonNullResultSupplier");
        return fold(s -> {
            try {
                final Optional<? extends R> result = Optional.ofNullable(mapper.apply(s));
                if (!result.isPresent()) {
                    return Try.success(nonNullResultSupplier.get());
                }
                return Try.success(result.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R> Try<Optional<R>> mapNullable(final Function<? super S, ? extends R> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                final Optional<R> result = Optional.ofNullable(mapper.apply(s));
                return Try.success(result);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R, F extends Throwable> Try<R> mapNullable(final Function<? super S, ? extends R> mapper,
                                                        final Function<? super S, ? extends F> ifNullFailureMapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        Objects.requireNonNull(ifNullFailureMapper, () -> "ifNullFailureMapper");
        return fold(s -> {
            try {
                final Optional<R> result = Optional.ofNullable(mapper.apply(s));
                if (result.isPresent()) {
                    return Try.success(result.get());
                } else {
                    return Try.failure(ifNullFailureMapper.apply(s));
                }
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <F extends Throwable> Try<S> mapFailure(final Function<? super Throwable, ? extends F> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(Try::success, throwable -> {
            try {
                return Try.failure(mapper.apply(throwable));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default <R, F extends Throwable> Try<R> attemptMap(final ErrableFn1<? super S, ? extends R, F> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                return Try.success(mapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R, F extends Throwable> Try<R> attemptMapNullable(final ErrableFn1<? super S, ? extends R, F> mapper,
                                                               final Supplier<? extends R> nonNullResultSupplier) {
        Objects.requireNonNull(mapper, () -> "mapper");
        Objects.requireNonNull(nonNullResultSupplier, () -> "nonNullResultSupplier");
        return fold(s -> {
            try {
                final Optional<? extends R> result = Optional.ofNullable(mapper.apply(s));
                if (!result.isPresent()) {
                    return Try.success(nonNullResultSupplier.get());
                }
                return Try.success(result.get());
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R, F extends Throwable> Try<Optional<R>> attemptMapNullable(final ErrableFn1<? super S, ? extends R, F> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                final Optional<R> result = Optional.ofNullable(mapper.apply(s));
                return Try.success(result);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R, F extends Throwable> Try<R> attemptMapNullable(final ErrableFn1<? super S, ? extends R, F> mapper,
                                                               final Function<? super S, ? extends F> ifNullFailureMapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        Objects.requireNonNull(ifNullFailureMapper, () -> "ifNullFailureMapper");
        return fold(s -> {
            try {
                final Optional<R> result = Optional.ofNullable(mapper.apply(s));
                if (result.isPresent()) {
                    return Try.success(result.get());
                } else {
                    return Try.failure(ifNullFailureMapper.apply(s));
                }
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <F extends Throwable> Try<S> attemptMapFailure(final ErrableFn1<? super Throwable, ? extends F, Throwable> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(Try::success, throwable -> {
            try {
                return Try.failure(mapper.apply(throwable));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default Try<Empty> consume(final Consumer<? super S> consumer) {
        Objects.requireNonNull(consumer, () -> "consumer");
        return fold(s -> {
            try {
                consumer.accept(s);
                return Try.success(Empty.INSTANCE);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default Try<Empty> consumeFailure(final Consumer<? super Throwable> consumer) {
        Objects.requireNonNull(consumer, () -> "consumer");
        return fold(s -> Try.success(Empty.INSTANCE), throwable -> {
            try {
                consumer.accept(throwable);
                return Try.success(Empty.INSTANCE);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default <F extends Throwable> Try<Empty> attemptConsume(final ErrableConsumer1<? super S, F> consumer) {
        Objects.requireNonNull(consumer, () -> "consumer");
        return fold(s -> {
            try {
                consumer.apply(s);
                return Try.success(Empty.INSTANCE);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <F extends Throwable> Try<Empty> attemptConsumeFailure(final ErrableConsumer1<? super Throwable, F> consumer) {
        Objects.requireNonNull(consumer, () -> "consumer");
        return fold(s -> Try.success(Empty.INSTANCE), throwable -> {
            try {
                consumer.apply(throwable);
                return Try.success(Empty.INSTANCE);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default <R> Try<R> flatMap(final Function<? super S, ? extends Try<? extends R>> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                return Try.narrow(mapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default <R, F extends Throwable> Try<R> attemptFlatMap(final ErrableFn1<? super S, ? extends Try<? extends R>, F> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(s -> {
            try {
                return Try.narrow(mapper.apply(s));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, Try::failure);
    }

    default Try<S> flatMapFailure(final Function<? super Throwable, ? extends Try<? extends S>> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(Try::success, throwable -> {
            try {
                return Try.narrow(mapper.apply(throwable));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default <F extends Throwable> Try<S> attemptFlatMapFailure(final ErrableFn1<? super Throwable, ? extends Try<? extends S>, F> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return fold(Try::success, throwable -> {
            try {
                return Try.narrow(mapper.apply(throwable));
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default <A, R> Try<R> zip(final Try<A> otherAttempt,
                              final BiFunction<? super S, ? super A, ? extends R> combiner) {
        Objects.requireNonNull(otherAttempt, () -> "otherAttempt");
        Objects.requireNonNull(combiner, () -> "combiner");
        return fold(s -> {
            return otherAttempt.fold(a -> Try.attempt(() -> combiner.apply(s, a)), Try::<R>failure);
        }, Try::failure);
    }

    default <A, R, F extends Throwable> Try<R> attemptZip(final Try<A> otherAttempt,
                                                          final ErrableFn2<? super S, ? super A, ? extends R, F> combiner) {
        Objects.requireNonNull(otherAttempt, () -> "otherAttempt");
        Objects.requireNonNull(combiner, () -> "combiner");
        return fold(s -> {
            return otherAttempt.fold(a -> Try.attempt(() -> combiner.apply(s, a)), Try::<R>failure);
        }, Try::failure);
    }

    default <A, B, R, F extends Throwable> Try<R> attemptZip2(final Try<A> otherAttempt1,
                                                              final Try<B> otherAttempt2,
                                                              final ErrableFn3<? super S, ? super A, ? super B, ? extends R, F> combiner) {
        Objects.requireNonNull(otherAttempt1, () -> "otherAttempt1");
        Objects.requireNonNull(combiner, () -> "combiner");
        return fold(s -> {
            return otherAttempt1.fold(a -> {
                return otherAttempt2.fold(b -> {
                    return Try.attempt(() -> combiner.apply(s, a, b));
                }, Try::<R>failure);
            }, Try::<R>failure);
        }, Try::failure);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    default <A, R> Try<R> zip(final Optional<A> optional,
                              final BiFunction<? super S, ? super A, ? extends R> combiner) {
        return zip(Try.fromOptional(Objects.requireNonNull(optional, () -> "optional")), combiner);
    }

    default S orElse(final S defaultValue) {
        return fold(s -> s, throwable -> defaultValue);
    }

    /**
     * Enables caller to fetch the result value if successful
     * <p></p>
     * Only if an error occurred at some point in the processing chain before this call does this method then request a default
     * value from the supplier
     *
     * @param defaultValueSupplier - called if failure case
     * @return the result success value or the result of the default value supplier
     */
    default S orElseGet(final Supplier<? extends S> defaultValueSupplier) {
        Objects.requireNonNull(defaultValueSupplier, () -> "defaultValueSupplier");
        return fold(s -> s, throwable -> defaultValueSupplier.get());
    }

    /**
     * Enables caller to retrieve the result value directly, wrapping any error that may have occurred in the function application
     * chain before it is thrown
     *
     * @param exceptionWrapper - function that takes the error and wraps it in another error type
     * @param <F>              - any throwable
     * @return the result value if a success, or else throws an exception wrapping the error that occurred in the process
     * @throws F - the wrapper type for the exception
     */
    default <F extends Throwable> S orElseThrow(final Function<Throwable, ? extends F> exceptionWrapper) throws F {
        Objects.requireNonNull(exceptionWrapper, () -> "exceptionWrapper");
        if (isSuccess()) {
            return getSuccess().get();
        } else {
            throw exceptionWrapper.apply(getFailure().get());
        }
    }

    /**
     * Enables caller to retrieve the result value directly, throwing any error that may have occurred in the function application
     * if it was an unchecked exception type or wrapping then throwing the error in a {@link RuntimeException}
     *
     * @param <F> - some unchecked exception throwable
     * @return the result value if a success, or else throws an unchecked exception
     * @throws F - the unchecked exception type or a wrapped checked exception in a {@link RuntimeException}
     */
    default <F extends RuntimeException> S orElseThrow() throws F {
        return fold(s -> s, throwable -> {
            if (throwable instanceof RuntimeException) {
                throw ((RuntimeException) throwable);
            } else {
                final String methodNameWhereFailed =
                        throwable.getStackTrace().length > 0 ? throwable.getStackTrace()[0].getMethodName() : "";
                final String message = String.format(
                        "a checked exception [ type: %s ] occurred during [ %s ] function application chain [ method_name: %s ]",
                        throwable.getClass().getSimpleName(),
                        Try.class.getSimpleName(),
                        methodNameWhereFailed);
                throw new RuntimeException(message, throwable);
            }
        });
    }

    default void ifFailed(final Consumer<? super Throwable> errorHandler) {
        Objects.requireNonNull(errorHandler, () -> "errorHandler");
        getFailure().ifPresent(errorHandler);
    }

    default void ifSuccess(final Consumer<? super S> successHandler) {
        Objects.requireNonNull(successHandler, () -> "successHandler");
        getSuccess().ifPresent(successHandler);
    }

    /**
     * Provides the success value and a null value for the throwable if successful
     * <p></p>
     * Provides a null value for success and the throwable value if failed at some point in the processing chain
     *
     * @param handler
     */
    default void onComplete(final BiConsumer<? super S, ? super Throwable> handler) {
        Objects.requireNonNull(handler, () -> "handler");
        fold(s -> {
            handler.accept(s, null);
            return Try.emptySuccess();
        }, throwable -> {
            handler.accept(null, throwable);
            return Try.emptySuccess();
        });
    }

    /**
     * Attempts to apply an errable consumer that:
     * <p></p>
     * Success Case: Takes both the success result value in the Success case and null for the throwable
     * <p></p>
     * Failure Case: Takes the throwable and null for the success result value
     * <p></p>
     * Since this consumer is errable but the return type for this method is {@code void}, in the event of a failure, that
     * throwable, and not the one from the Failure, will be rethrown on its own if it is a subtype of {@link
     * RuntimeException} or it will be wrapped in a {@link RuntimeException}
     *
     * @param handler
     * @param <F>     - checked exception failure type
     */
    default <F extends Throwable> void attemptOnComplete(final ErrableConsumer2<? super S, ? super Throwable, F> handler) {
        Objects.requireNonNull(handler, () -> "handler");
        fold(s -> {
            try {
                handler.apply(s, null);
                return Try.emptySuccess();
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, throwable -> {
            try {
                handler.apply(null, throwable);
                return Try.emptySuccess();
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }).orElseThrow();
    }

    default Stream<S> stream() {
        return fold(Stream::of, throwable -> Stream.empty());
    }

    default Try<S> peek(final Consumer<? super S> successObserver,
                        final Consumer<? super Throwable> failureObserver) {
        Objects.requireNonNull(successObserver, () -> "successObserver");
        Objects.requireNonNull(failureObserver, () -> "failureObserver");
        return fold(s -> {
            try {
                successObserver.accept(s);
                return Try.success(s);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        }, throwable -> {
            try {
                failureObserver.accept(throwable);
                return Try.failure(throwable);
            } catch (final Throwable t) {
                return Try.failure(t);
            }
        });
    }

    default Try<S> peekIfSuccess(final Consumer<? super S> successObserver) {
        Objects.requireNonNull(successObserver, () -> "successObserver");
        return fold(s -> {
            successObserver.accept(s);
            return Try.success(s);
        }, Try::failure);
    }

    default Try<S> peekIfFailure(final Consumer<? super Throwable> failureObserver) {
        Objects.requireNonNull(failureObserver, () -> "failureObserver");
        return fold(Try::success, throwable -> {
            failureObserver.accept(throwable);
            return Try.failure(throwable);
        });
    }

    default <R> R to(final Function<Try<S>, ? extends R> transformer) {
        return Objects.requireNonNull(transformer, () -> "transformer").apply(this);
    }

    <R> R fold(final Function<? super S, ? extends R> successHandler,
               final Function<Throwable, ? extends R> failureHandler);

    /**
     * This enum instance is used instead of {@link Void} since void cannot be instantiated and thus would require {@code null}
     * values being passed down through the method chain ( which some other libraries choose to do but it comes with some
     * penalties )
     * <p></p>
     * The use of this instance pairs well semantically with methods that require it, like {@link
     * #consume(Consumer)} since they do in fact leave the {@link #success(Object)} value {@link Empty}
     */
    enum Empty {
        INSTANCE;
    }

    @FunctionalInterface
    interface ErrableRunnable<X extends Throwable> {

        void apply() throws X;

    }

    @FunctionalInterface
    interface ErrableFn0<T, X extends Throwable> {

        T apply() throws X;

    }

    @FunctionalInterface
    interface ErrableFn1<I, O, X extends Throwable> {

        O apply(I input) throws X;

    }

    @FunctionalInterface
    interface ErrableFn2<I1, I2, O, X extends Throwable> {

        O apply(I1 input1,
                I2 input2) throws X;

    }

    @FunctionalInterface
    interface ErrableFn3<I1, I2, I3, O, X extends Throwable> {

        O apply(I1 input1,
                I2 input2,
                I3 input3) throws X;

    }

    @FunctionalInterface
    interface ErrableConsumer1<I, X extends Throwable> {

        void apply(I input) throws X;

    }

    @FunctionalInterface
    interface ErrableConsumer2<I1, I2, X extends Throwable> {

        void apply(I1 input1,
                   I2 input2) throws X;

    }


}
