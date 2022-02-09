package funcify.container.async

import arrow.core.None
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import funcify.container.async.AsyncFactory.DeferredValue
import funcify.container.async.AsyncFactory.DeferredValue.CompletionStageValue
import funcify.container.async.AsyncFactory.DeferredValue.FluxValue
import funcify.container.attempt.Try
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.stream.Stream
import kotlin.streams.asSequence
import kotlin.streams.asStream


/**
 *
 * @author smccarron
 * @created 2/8/22
 */
interface Async<out V> : Iterable<V> {

    companion object {

        fun <V> of(value: V): Async<V> {
            return AsyncFactory.AsyncCompletedSuccess<V>(value)
        }

        fun <V> ofError(throwable: Throwable): Async<V> {
            return AsyncFactory.AsyncCompletedFailure<V>(throwable)
        }

        fun <V> ofDeferred(deferredValue: DeferredValue<V>): Async<V> {
            return AsyncFactory.AsyncDeferredValue<V>(deferredValue)
        }

        fun <V> empty(): Async<V> {
            return ofDeferred(FluxValue<V>(Flux.empty()))
        }

        fun <V> fromCompletionStage(completionStage: CompletionStage<V>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::join)
                            .getFailure()
                            .map { e -> ofError<V>(e) }
                            .getOrElse { ofError<V>(IllegalArgumentException("completion_stage output is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attempt(completionStage.toCompletableFuture()::join)
                            .getSuccess()
                            .map { v -> of<V>(v) }
                            .getOrElse { ofError<V>(IllegalArgumentException("completion_stage output is missing successful result value despite not completing exceptionally")) }
                }
                else -> {
                    ofDeferred(CompletionStageValue<V>(completionStage))
                }
            }
        }

        fun <V> fromNullableCompletionStageValue(completionStage: CompletionStage<V?>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::get)
                            .getFailure()
                            .map { e -> ofError<V>(e) }
                            .getOrElse { ofError<V>(IllegalArgumentException("completion_stage input is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::get)
                            .getSuccess()
                            .map { vOpt -> vOpt.fold({ ofError<V>(NoSuchElementException("completed_value is null or not present")) }) { v -> of<V>(v) } }
                            .getOrElse { ofError<V>(IllegalArgumentException("completion_stage input is missing successful result value despite completing unexceptionally")) }
                }
                else -> {
                    ofDeferred(CompletionStageValue<V>(completionStage.thenCompose { v ->
                        v.toOption()
                                .fold({
                                          CompletableFuture<V>().apply { completeExceptionally(IllegalArgumentException("completed_value is null or not present")) }
                                      }) { value ->
                                    CompletableFuture.completedFuture(value)
                                }
                    }))
                }
            }
        }

        fun <V> fromFlux(fluxValue: Flux<V>): Async<V> {
            return ofDeferred(FluxValue<V>(fluxValue))
        }

        fun <V> fromNullableFluxValue(fluxValue: Flux<V?>): Async<V> {
            return ofDeferred(FluxValue<V>(fluxValue.filter { v -> v != null }
                                                   .map { v -> v!! }))
        }

    }

    /**
     * Blocking method
     */
    override fun iterator(): Iterator<V> {
        return sequence().iterator()
    }

    /**
     * Blocking method
     */
    override fun spliterator(): Spliterator<@UnsafeVariance V> {
        return sequence().asStream()
                .spliterator()
    }

    /**
     * Blocking method
     */
    fun sequence(): Sequence<V> {
        return fold({ v ->
                        sequenceOf(v)
                    }, { thr ->
                        emptySequence()
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> {
                                Try.attempt(defVal.inputStage.toCompletableFuture()::join)
                                        .sequence()
                            }
                            is FluxValue -> {
                                defVal.inputFlux.toStream()
                                        .asSequence()
                            }
                        }
                    })
    }


    fun <R> map(function: (V) -> R): Async<R> {
        return fold({ v: V ->
                        of<R>(function.invoke(v))
                    }, { throwable: Throwable ->
                        ofError<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStage.thenApply(function::invoke))
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.map(function::invoke))
                            }
                        }
                    })
    }

    fun <R> flatMapSingle(function: (V) -> Async<R>): Async<R> {
        return fold({ v: V ->
                        function.invoke(v)
                    }, { throwable: Throwable ->
                        ofError<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStage.thenCompose { v ->
                                    function.invoke(v)
                                            .fold({ r ->
                                                      CompletableFuture.completedFuture(r)
                                                  }, { thr ->
                                                      val future: CompletableFuture<R> = CompletableFuture()
                                                      future.completeExceptionally(thr)
                                                      future
                                                  }, { defVal ->
                                                      when (defVal) {
                                                          is CompletionStageValue -> defVal.inputStage
                                                          is FluxValue -> defVal.inputFlux.next()
                                                                  .toFuture() as CompletionStage<R>
                                                      }
                                                  })
                                })
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.flatMap { v ->
                                    function.invoke(v)
                                            .fold({ r ->
                                                      Mono.just(r)
                                                              .flux()
                                                  }, { thr -> Flux.error(thr) }, { defVal ->
                                                      when (defVal) {
                                                          is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                                                  .flux()
                                                          is FluxValue -> defVal.inputFlux
                                                      }
                                                  })
                                })
                            }
                        }
                    })
    }

    fun <R> flatMapMany(function: (V) -> Stream<Async<R>>): Async<R> {
        return fold({ v: V ->
                        fromFlux(function.invoke(v)
                                         .map { asyncInst ->
                                             asyncInst.fold({ r ->
                                                                Mono.just(r)
                                                                        .flux()
                                                            }, { thr ->
                                                                Mono.error<R>(thr)
                                                                        .flux()
                                                            }, { defVal ->
                                                                when (defVal) {
                                                                    is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                                                            .flux()
                                                                    is FluxValue -> defVal.inputFlux
                                                                }
                                                            })
                                         }
                                         .reduce { f1, f2 -> f1.concatWith(f2) }
                                         .orElseGet { Flux.empty<R>() })

                    }, { throwable: Throwable ->
                        ofError<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromFlux<R>(Mono.fromCompletionStage(deferredValue.inputStage.thenApply { v ->
                                    function.invoke(v)
                                            .map { asInst ->
                                                asInst.fold({ r ->
                                                                Mono.just(r)
                                                                        .flux()
                                                            }, { thr ->
                                                                Flux.error(thr)
                                                            }, { defVal ->
                                                                when (defVal) {
                                                                    is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                                                            .flux()
                                                                    is FluxValue -> defVal.inputFlux
                                                                }
                                                            })
                                            }
                                            .reduce { f1, f2 -> f1.concatWith(f2) }
                                            .orElseGet { Flux.empty<R>() }
                                })
                                                    .flatMapMany { flx -> flx })
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.map { v ->
                                    function.invoke(v)
                                            .map { asInst ->
                                                asInst.fold({ r ->
                                                                Mono.just(r)
                                                                        .flux()
                                                            }, { thr ->
                                                                Flux.error(thr)
                                                            }, { defVal ->
                                                                when (defVal) {
                                                                    is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                                                            .flux()
                                                                    is FluxValue -> defVal.inputFlux
                                                                }
                                                            })
                                            }
                                            .reduce { f1, f2 -> f1.concatWith(f2) }
                                            .orElseGet { Flux.empty<R>() }
                                }
                                                    .flatMap { flx -> flx })

                            }

                        }
                    })
    }

    fun filter(function: (V) -> Boolean): Async<Option<V>> {
        return fold({ v ->
                        of(v.some()
                                   .filter(function::invoke))
                    }, { thr ->
                        ofError(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStage.thenApply { v ->
                                    v.some()
                                            .filter(function::invoke)
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.map { v ->
                                    v.some()
                                            .filter(function::invoke)
                                })
                            }
                        }
                    })
    }

    fun filter(function: (V) -> Boolean, ifConditionNotMet: (V) -> Throwable): Async<V> {
        return fold({ v ->
                        v.some()
                                .filter(function::invoke)
                                .map { value -> of(value) }
                                .getOrElse { ofError(ifConditionNotMet.invoke(v)) }
                    }, { thr ->
                        ofError(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStage.thenCompose { v ->
                                    v.some()
                                            .filter(function::invoke)
                                            .map { value -> CompletableFuture.completedFuture(value) }
                                            .getOrElse { CompletableFuture<V>().apply { completeExceptionally(ifConditionNotMet.invoke(v)) } }
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.flatMap { v ->
                                    v.some()
                                            .filter(function::invoke)
                                            .map { value -> Mono.just(value) }
                                            .getOrElse { Mono.error(ifConditionNotMet.invoke(v)) }
                                })
                            }
                        }
                    })
    }

    fun <A, R> zip(other: Async<A>, combiner: (V, A) -> R): Async<R> {
        return fold({ v ->
                        other.fold({ a ->
                                       of(combiner.invoke(v, a))
                                   }, { thr ->
                                       ofError(thr)
                                   }, { defVal ->
                                       when (defVal) {
                                           is CompletionStageValue -> {
                                               fromCompletionStage(defVal.inputStage.thenCombine(CompletableFuture.completedFuture(v)) { aVal, vVal ->
                                                   combiner.invoke(vVal, aVal)
                                               })
                                           }
                                           is FluxValue -> {
                                               fromFlux(defVal.inputFlux.map { a -> combiner.invoke(v, a) })
                                           }
                                       }
                                   })
                    }, { thr ->
                        ofError(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStage.thenCombine(other.fold({ v ->
                                                                                                        CompletableFuture.completedFuture(v)
                                                                                                    }, { thr ->
                                                                                                        CompletableFuture<A>().apply {
                                                                                                            completeExceptionally(thr)
                                                                                                        }
                                                                                                    }, { defVal ->
                                                                                                        when (defVal) {
                                                                                                            is CompletionStageValue -> defVal.inputStage
                                                                                                            is FluxValue -> defVal.inputFlux.next()
                                                                                                                    .toFuture()
                                                                                                        }
                                                                                                    })) { vVal, aVal ->
                                    combiner.invoke(vVal, aVal)
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.zipWith(other.fold({ v ->
                                                                                        Mono.just(v)
                                                                                                .flux()
                                                                                    }, { thr -> Flux.error(thr) }, { defVal ->
                                                                                        when (defVal) {
                                                                                            is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                                                                                    .flux()
                                                                                            is FluxValue -> defVal.inputFlux
                                                                                        }
                                                                                    })) { vVal, aVal -> combiner.invoke(vVal, aVal) })
                            }
                        }
                    })
    }

    fun blockFirst(): Try<V> {
        return fold({ v ->
                        Try.success(v)
                    }, { thr ->
                        Try.failure(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStage.toCompletableFuture()::join)
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::blockFirst) {
                                IllegalArgumentException("no values were returned for flux subscriptions (or none that is non-null)")
                            }
                        }
                    })
    }

    fun blockFirstOption(): Option<V> {
        return fold({ v ->
                        v.some()
                    }, { thr ->
                        None
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStage.toCompletableFuture()::join)
                                    .getSuccess()
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::blockFirst) {
                                IllegalArgumentException("no values were returned for flux subscriptions (or none that is non-null)")
                            }
                                    .getSuccess()
                        }
                    })
    }

    fun block(): Try<Stream<out V>> {
        return fold({ v ->
                        Try.success(Stream.of(v))
                    }, { thr ->
                        Try.failure(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStage.toCompletableFuture()::join)
                                    .map { v -> Stream.of(v) }
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::toStream) {
                                IllegalArgumentException("no stream of values was returned for flux subscriptions (or none that is non-null)")
                            }
                        }
                    })
    }

    fun toFlux(): Flux<out V> {
        return fold({ v ->
                        Mono.just(v)
                                .flux()
                    }, { thr ->
                        Flux.error(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStage)
                                    .flux()
                            is FluxValue -> defVal.inputFlux
                        }
                    })
    }

    fun toFuture(): CompletionStage<out V> {
        return fold({ v ->
                        CompletableFuture.completedFuture(v)
                    }, { thr ->
                        CompletableFuture<V>().apply { completeExceptionally(thr) }
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> defVal.inputStage
                            is FluxValue -> defVal.inputFlux.next()
                                    .toFuture()
                        }
                    })
    }

    fun <R> fold(completedSuccessHandler: (V) -> R, completedFailureHandler: (Throwable) -> R, deferredValueHandler: (DeferredValue<V>) -> R): R


}