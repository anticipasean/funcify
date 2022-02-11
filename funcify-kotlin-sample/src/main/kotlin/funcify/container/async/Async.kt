package funcify.container.async

import arrow.core.*
import funcify.container.async.AsyncFactory.DeferredValue
import funcify.container.async.AsyncFactory.DeferredValue.CompletionStageValue
import funcify.container.async.AsyncFactory.DeferredValue.FluxValue
import funcify.container.attempt.Try
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executor
import java.util.stream.Collectors
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

        fun <V> succeeded(values: PersistentList<V>): Async<V> {
            return AsyncFactory.AsyncCompletedSuccess<V>(values)
        }

        fun <V> errored(throwable: Throwable): Async<V> {
            return AsyncFactory.AsyncCompletedFailure<V>(throwable)
        }

        fun <V> deferred(deferredValue: DeferredValue<V>): Async<V> {
            return AsyncFactory.AsyncDeferredValue<V>(deferredValue)
        }

        fun <V> succeededSingle(value: V): Async<V> {
            return succeeded(persistentListOf(value))
        }

        fun <V> empty(): Async<V> {
            return succeeded(persistentListOf())
        }

        fun <V> deferFromSupplier(executor: Executor, supplier: () -> V): Async<V> {
            return fromSingleValueCompletionStage(CompletableFuture.supplyAsync({ supplier.invoke() }, executor))
        }

        fun <V> deferFromStream(executor: Executor, supplier: () -> Stream<V>): Async<V> {
            return fromFlux(Flux.fromStream { supplier.invoke() }
                                    .subscribeOn(Schedulers.fromExecutor(executor)))
        }

        fun <V> deferFromIterable(executor: Executor, iterable: Iterable<V>): Async<V> {
            return fromFlux(Flux.fromIterable(iterable)
                                    .subscribeOn(Schedulers.fromExecutor(executor)))
        }

        fun <V> deferFromSequence(executor: Executor, sequence: Sequence<V>): Async<V> {
            return deferFromIterable(executor, sequence.asIterable())
        }

        fun <V> fromAttempt(attempt: Try<V>): Async<V> {
            return attempt.fold({ v: V ->
                                    succeededSingle(v)
                                }, { throwable: Throwable ->
                                    errored(throwable)
                                })
        }

        fun <V> fromStreamAttempt(attempt: Try<Stream<V>>): Async<V> {
            return attempt.fold({ v: Stream<V> ->
                                    succeeded(v.asSequence()
                                                      .toPersistentList())
                                }, { throwable: Throwable ->
                                    errored(throwable)
                                })
        }

        fun <V> fromStreamOfAttempts(attemptStream: Stream<Try<V>>): Async<V> {
            return fromFlux(Flux.fromStream(attemptStream)
                                    .flatMap { attempt -> attempt.fold({ v -> Flux.just(v) }, { thr -> Flux.error(thr) }) })
        }

        fun <V> fromSingleValueCompletionStage(completionStage: CompletionStage<V>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::join)
                            .getFailure()
                            .map { e -> errored<V>(e) }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attempt(completionStage.toCompletableFuture()::join)
                            .getSuccess()
                            .map { v -> succeededSingle<V>(v) }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing successful result value despite not completing exceptionally")) }
                }
                else -> {
                    deferred(CompletionStageValue<V>(completionStage.thenApply { v -> Stream.of(v) }))
                }
            }
        }

        fun <V> fromCompletionStage(completionStage: CompletionStage<Stream<V>>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::join)
                            .getFailure()
                            .map { e -> errored<V>(e) }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attempt(completionStage.toCompletableFuture()::join)
                            .getSuccess()
                            .map { vStream ->
                                succeeded<V>(vStream.asSequence()
                                                     .toPersistentList())
                            }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing successful result value despite not completing exceptionally")) }
                }
                else -> {
                    deferred(CompletionStageValue<V>(completionStage.thenApply { stream -> stream }))
                }
            }
        }

        fun <V> fromNullableValuesStreamCompletionStage(completionStage: CompletionStage<Stream<V?>>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::join)
                            .getFailure()
                            .map { e -> errored<V>(e) }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attempt(completionStage.toCompletableFuture()::join)
                            .getSuccess()
                            .map { vStream ->
                                succeeded<V>(vStream.flatMap { v ->
                                    v.toOption()
                                            .fold({ Stream.empty() }, { nonNullV -> Stream.of(nonNullV) })
                                }
                                                     .asSequence()
                                                     .toPersistentList())
                            }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage output is missing successful result value despite not completing exceptionally")) }
                }
                else -> {
                    deferred(CompletionStageValue<V>(completionStage.thenApply { stream ->
                        stream.flatMap { v ->
                            v.toOption()
                                    .fold({ Stream.empty() }, { nonNullV -> Stream.of(nonNullV) })
                        }
                    }))
                }
            }
        }

        fun <V> fromNullableSingleValueCompletionStage(completionStage: CompletionStage<V?>): Async<V> {
            return when {
                completionStage.toCompletableFuture().isCompletedExceptionally -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::get)
                            .getFailure()
                            .map { e -> errored<V>(e) }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage input is missing exception despite completing exceptionally")) }
                }
                completionStage.toCompletableFuture().isDone -> {
                    Try.attemptNullable(completionStage.toCompletableFuture()::get)
                            .getSuccess()
                            .map { vOpt ->
                                vOpt.fold({ succeeded<V>(persistentListOf()) }) { v ->
                                    succeededSingle<V>(v)
                                }
                            }
                            .getOrElse { errored<V>(IllegalArgumentException("completion_stage input is missing successful result value despite completing unexceptionally")) }
                }
                else -> {
                    deferred(CompletionStageValue<V>(completionStage.thenCompose { v ->
                        v.toOption()
                                .fold({
                                          CompletableFuture.completedFuture(Stream.empty())
                                      }, { value ->
                                          CompletableFuture.completedFuture(Stream.of(value))
                                      })
                    }))
                }
            }
        }

        fun <V> fromFlux(fluxValue: Flux<V>): Async<V> {
            return deferred(FluxValue<V>(fluxValue))
        }

        fun <V> fromNullableFluxValue(fluxValue: Flux<V?>): Async<V> {
            return deferred(FluxValue<V>(fluxValue.filter { v -> v != null }
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
        return fold({ pList ->
                        pList.asSequence()
                    }, {
                        emptySequence()
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> {
                                Try.attempt(defVal.inputStreamStage.toCompletableFuture()::join)
                                        .map { stream -> stream.asSequence() }
                                        .fold({ seq -> seq }, { emptySequence() })
                                        .map { v -> v }
                            }
                            is FluxValue -> {
                                Try.attempt(defVal.inputFlux::toStream)
                                        .sequence()
                                        .flatMap { stream -> stream.asSequence() }
                            }
                        }
                    })
    }


    fun <R> map(mapper: (V) -> R): Async<R> {
        return fold({ vList ->
                        succeeded(vList.asIterable()
                                          .map(mapper::invoke)
                                          .toPersistentList())
                    }, { throwable: Throwable ->
                        errored<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStreamStage.thenApply { stream -> stream.map(mapper::invoke) })
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.map(mapper::invoke))
                            }
                        }
                    })
    }

    fun <R> map(executor: Executor, mapper: (V) -> R): Async<R> {
        return fold({ vList ->
                        fromCompletionStage(CompletableFuture.supplyAsync({
                                                                              vList.stream()
                                                                                      .map(mapper::invoke)
                                                                          }, executor))
                    }, { throwable: Throwable ->
                        errored<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStreamStage.thenApplyAsync({ vStream -> vStream.map(mapper::invoke) },
                                                                                                     executor))
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.publishOn(Schedulers.fromExecutor(executor))
                                                    .map(mapper::invoke))
                            }
                        }
                    })
    }

    fun <R> flatMap(mapper: (V) -> Async<R>): Async<R> {
        return fold({ vList ->
                        Try.fromOptional(vList.stream()
                                                 .map { v -> mapper.invoke(v) }
                                                 .map { asyncV ->
                                                     asyncV.toFlux()
                                                             .map { r -> r }
                                                 }
                                                 .reduce { f1: Flux<R>, f2: Flux<R> -> f1.concatWith(f2) })
                                .fold({ sFlux -> fromFlux(sFlux) }, { thr -> errored(thr) })
                    }, { throwable: Throwable ->
                        errored<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStreamStage.thenCompose { vStream ->
                                    vStream.map(mapper::invoke)
                                            .map { asyncV -> asyncV.toCompletionStage() }
                                            .reduce { streamStage1, streamStage2 ->
                                                streamStage1.thenCombine(streamStage2) { s1, s2 ->
                                                    Stream.concat(s1, s2)
                                                }
                                            }
                                            .orElseGet { CompletableFuture.completedFuture(Stream.empty()) }
                                            .thenApply { s -> s.map { r -> r } }
                                })
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.flatMap { v ->
                                    mapper.invoke(v)
                                            .toFlux()
                                            .map { r -> r }
                                })
                            }
                        }
                    })
    }

    fun <R> flatMap(executor: Executor, mapper: (V) -> Async<R>): Async<R> {
        return fold({ vList ->
                        fromFlux(Flux.fromStream(vList.stream())
                                         .publishOn(Schedulers.fromExecutor(executor))
                                         .map { v -> mapper.invoke(v) }
                                         .flatMap { asyncV ->
                                             asyncV.toFlux()
                                         })
                    }, { throwable: Throwable ->
                        errored<R>(throwable)
                    }, { deferredValue: DeferredValue<V> ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage<R>(deferredValue.inputStreamStage.thenComposeAsync({ vStream ->
                                                                                                           vStream.map(mapper::invoke)
                                                                                                                   .map { asyncV -> asyncV.toCompletionStage() }
                                                                                                                   .reduce { streamStage1, streamStage2 ->
                                                                                                                       streamStage1.thenCombine(
                                                                                                                               streamStage2) { s1, s2 ->
                                                                                                                           Stream.concat(s1, s2)
                                                                                                                       }
                                                                                                                   }
                                                                                                                   .orElseGet {
                                                                                                                       CompletableFuture.completedFuture(
                                                                                                                               Stream.empty())
                                                                                                                   }
                                                                                                                   .thenApply { s -> s.map { r -> r } }
                                                                                                       }, executor))
                            }
                            is FluxValue -> {
                                fromFlux<R>(deferredValue.inputFlux.flatMap { v ->
                                    mapper.invoke(v)
                                            .toFlux()
                                            .map { r -> r }
                                })
                            }
                        }
                    })
    }

    fun filter(condition: (V) -> Boolean): Async<V> {
        return fold({ vList ->
                        succeeded(vList.asIterable()
                                          .filter(condition::invoke)
                                          .toPersistentList())
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStreamStage.thenApply { vStream ->
                                    vStream.filter(condition::invoke)
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.filter(condition::invoke))
                            }
                        }
                    })
    }

    fun filterToOption(condition: (V) -> Boolean): Async<Option<V>> {
        return fold({ vList ->
                        succeeded(vList.asSequence()
                                          .map { v ->
                                              v.some()
                                                      .filter(condition::invoke)
                                          }
                                          .toPersistentList())
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStreamStage.thenApply { vStream ->
                                    vStream.map { v ->
                                        v.some()
                                                .filter(condition::invoke)
                                    }
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.map { v ->
                                    v.some()
                                            .filter(condition::invoke)
                                })
                            }
                        }
                    })
    }

    fun filter(condition: (V) -> Boolean, ifConditionNotMet: (V) -> Throwable): Async<V> {
        return fold({ vList ->
                        fromFlux(Flux.fromStream(vList::stream)
                                         .flatMap { v ->
                                             Try.success(v)
                                                     .filter(condition::invoke, ifConditionNotMet::invoke)
                                                     .fold({ s -> Flux.just(s) }, { thr -> Flux.error(thr) })
                                         })
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromCompletionStage(deferredValue.inputStreamStage.thenApply { vStream ->
                                    vStream.map { v ->
                                        Try.success(v)
                                                .filter(condition::invoke, ifConditionNotMet::invoke)
                                                .orElseThrow()
                                    }
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.flatMap { v ->
                                    Try.success(v)
                                            .filter(condition::invoke, ifConditionNotMet::invoke)
                                            .fold({ s -> Flux.just(s) }, { thr -> Flux.error(thr) })
                                })
                            }
                        }
                    })
    }

    fun <A, R> zip(other: Async<A>, combiner: (V, A) -> R): Async<R> {
        return fold({ vList ->
                        other.fold({ aStream ->
                                       fromStreamAttempt(Try.attempt({
                                                                         vList.asSequence()
                                                                                 .zip(aStream.asSequence(), combiner::invoke)
                                                                     })
                                                                 .map { seq ->
                                                                     seq.asStream()
                                                                 })
                                   }, { thr ->
                                       errored(thr)
                                   }, { defVal ->
                                       when (defVal) {
                                           is CompletionStageValue -> {
                                               fromCompletionStage(defVal.inputStreamStage.thenCombine(CompletableFuture.completedFuture(vList)) { aValStream, vValStream ->
                                                   vValStream.asSequence()
                                                           .zip(aValStream.asSequence(), combiner::invoke)
                                                           .asStream()
                                               })
                                           }
                                           is FluxValue -> {
                                               fromFlux(Flux.fromStream(vList::stream)
                                                                .zipWith(defVal.inputFlux, combiner::invoke))
                                           }
                                       }
                                   })
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                if (!(other is AsyncFactory.AsyncDeferredValue<*> && other.deferredValue is FluxValue<*>)) {
                                    fromCompletionStage(deferredValue.inputStreamStage.thenCombine(other.toCompletionStage()) { vVal, aVal ->
                                        vVal.asSequence()
                                                .zip(aVal.asSequence(), combiner::invoke)
                                                .asStream()
                                    })
                                } else {
                                    fromFlux(Mono.fromCompletionStage(deferredValue.inputStreamStage)
                                                     .flatMapMany { stream -> Flux.fromStream(stream) }
                                                     .zipWith(other.toFlux(), combiner::invoke))
                                }
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.zipWith(other.toFlux()) { vVal, aVal -> combiner.invoke(vVal, aVal) })
                            }
                        }
                    })
    }

    fun <A, R> zip(executor: Executor, other: Async<A>, combiner: (V, A) -> R): Async<R> {
        return fold({ vList ->
                        other.fold({ aList ->
                                       fromFlux(Flux.fromStream(vList::stream)
                                                        .publishOn(Schedulers.fromExecutor(executor))
                                                        .zipWith(Flux.fromStream(aList::stream), combiner::invoke))
                                   }, { thr ->
                                       errored(thr)
                                   }, { defVal ->
                                       when (defVal) {
                                           is CompletionStageValue -> {
                                               fromCompletionStage(defVal.inputStreamStage.thenCombineAsync(CompletableFuture.completedFuture(vList),
                                                                                                            { aValStream, vValStream ->
                                                                                                                vValStream.asSequence()
                                                                                                                        .zip(aValStream.asSequence(),
                                                                                                                             combiner::invoke)
                                                                                                                        .asStream()
                                                                                                            },
                                                                                                            executor))
                                           }
                                           is FluxValue -> {
                                               fromFlux(Flux.fromStream(vList::stream)
                                                                .publishOn(Schedulers.fromExecutor(executor))
                                                                .zipWith(defVal.inputFlux, combiner::invoke))
                                           }
                                       }
                                   })
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                if (!(other is AsyncFactory.AsyncDeferredValue<*> && other.deferredValue is FluxValue<*>)) {
                                    fromCompletionStage(deferredValue.inputStreamStage.thenCombineAsync(other.toCompletionStage(), { vVal, aVal ->
                                        vVal.asSequence()
                                                .zip(aVal.asSequence(), combiner::invoke)
                                                .asStream()
                                    }, executor))
                                } else {
                                    fromFlux(Mono.fromCompletionStage(deferredValue.inputStreamStage)
                                                     .publishOn(Schedulers.fromExecutor(executor))
                                                     .flatMapMany { stream -> Flux.fromStream(stream) }
                                                     .zipWith(other.toFlux(), combiner::invoke))
                                }
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.publishOn(Schedulers.fromExecutor(executor))
                                                 .zipWith(other.toFlux(), combiner::invoke))
                            }
                        }
                    })
    }

    fun blockFirst(): Try<V> {
        return fold({ vList ->
                        Try.fromOption(vList.firstOrNone())
                    }, { thr ->
                        Try.failure(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStreamStage.toCompletableFuture()::join)
                                    .map { stream -> stream.findFirst() }
                                    .flatMap { firstOpt -> Try.fromOptional(firstOpt) }
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::blockFirst) {
                                IllegalArgumentException("no values were returned for flux subscriptions (or none that is non-null)")
                            }
                        }
                    })
    }

    fun blockFirstOption(): Option<V> {
        return blockFirst().getSuccess()
    }

    fun blockFirstOrElseGet(defaultSupplier: () -> @UnsafeVariance V): V {
        return blockFirst().getSuccess()
                .getOrElse(defaultSupplier)
    }

    fun blockFirstOrElseThrow(): V {
        return blockFirst().orElseThrow()
    }

    fun block(): Try<Stream<out V>> {
        return fold({ vList ->
                        Try.success(vList.stream())
                    }, { thr ->
                        Try.failure(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStreamStage.toCompletableFuture()::join)
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::toStream) {
                                IllegalArgumentException("no stream of values was returned for flux subscriptions (or none that is non-null)")
                            }
                        }
                    })
    }

    fun blockOrElseThrow(): Stream<out V> {
        return block().orElseThrow()
    }

    fun blockOrElseGet(defaultSupplier: () -> Stream<@UnsafeVariance V>): Stream<out V> {
        return block().orElseGet(defaultSupplier)
    }

    fun blockLast(): Try<V> {
        return fold({ vStream ->
                        Try.attempt { vStream.toList() }
                                .map { l ->
                                    if (l.isEmpty()) {
                                        None
                                    } else {
                                        l[l.size - 1].some()
                                    }
                                }
                                .flatMap { vOpt -> Try.fromOption(vOpt) }
                    }, { thr ->
                        Try.failure(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStreamStage.toCompletableFuture()::join)
                                    .flatMap { strm -> Try.fromOptional(strm.findFirst()) }
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::blockLast) {
                                NoSuchElementException("no stream of values was returned for flux subscriptions (or none that is non-null)")
                            }
                        }
                    })
    }

    fun blockLastOption(): Option<V> {
        return fold({ vList ->
                        vList.lastOrNone()
                    }, {
                        None
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Try.attempt(defVal.inputStreamStage.toCompletableFuture()::join)
                                    .map { stream -> stream.collect(Collectors.toList()) }
                                    .map { list ->
                                        list.takeLast(1)
                                                .stream()
                                                .findFirst()
                                    }
                                    .flatMap { lastOptional -> Try.fromOptional(lastOptional) }
                                    .getSuccess()
                            is FluxValue -> Try.attemptNullable(defVal.inputFlux::blockLast) {
                                IllegalArgumentException("no stream of values was returned for flux subscriptions (or none that is non-null)")
                            }
                                    .getSuccess()
                        }
                    })
    }

    fun blockLastOrElseGet(defaultSupplier: () -> @UnsafeVariance V): V {
        return blockLast().orElseGet(defaultSupplier)
    }

    fun blockLastOrElseThrow(): V {
        return blockLast().orElseThrow()
    }

    fun <R> reduce(initial: R, accumulator: (R, V) -> R, combiner: (R, R) -> R = { _, r2 -> r2 }): Async<R> {
        return fold({ vList ->
                        succeededSingle(vList.stream()
                                                .reduce(initial, accumulator::invoke, combiner::invoke))
                    }, { thr ->
                        errored(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> {
                                fromSingleValueCompletionStage(defVal.inputStreamStage.thenApply { vStream ->
                                    vStream.reduce(initial, accumulator::invoke, combiner::invoke)
                                })
                            }
                            is FluxValue -> {
                                fromFlux(defVal.inputFlux.reduce(initial, accumulator::invoke)
                                                 .flux())
                            }
                        }
                    })
    }

    fun <R> reduce(executor: Executor, initial: R, accumulator: (R, V) -> R, combiner: (R, R) -> R = { _, r2 -> r2 }): Async<R> {
        return fold({ vList ->
                        fromSingleValueCompletionStage(CompletableFuture.supplyAsync({
                                                                                         vList.stream()
                                                                                                 .reduce(initial,
                                                                                                         accumulator::invoke,
                                                                                                         combiner::invoke)
                                                                                     }, executor))
                    }, { thr ->
                        errored(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> {
                                fromSingleValueCompletionStage(defVal.inputStreamStage.thenApplyAsync({ vStream ->
                                                                                                          vStream.reduce(initial,
                                                                                                                         accumulator::invoke,
                                                                                                                         combiner::invoke)
                                                                                                      }, executor))
                            }
                            is FluxValue -> {
                                fromFlux(defVal.inputFlux.publishOn(Schedulers.fromExecutor(executor))
                                                 .reduce(initial, accumulator::invoke)
                                                 .flux())
                            }
                        }
                    })
    }

    /**
     * In parallel reduction, a combiner function, one handling the combining of any two leaf nodes of any given set of reduction trees,
     * must be provided to ensure values are not lost in the result:
     * `parallelReduce(executor, persistentListOf<String>(), { pList, nextStr -> pList.add(nextStr) }, { pList1, pList2 -> pList1.addAll(pList2) })`
     * When reduction is not done in parallel, there is only one leaf node, so the combiner function can be defaulted to `{ result1, result2 -> result2 }`
     */
    fun <R> parallelReduce(executor: Executor, initial: R, accumulator: (R, V) -> R, combiner: (R, R) -> R): Async<R> {
        return fold({ vList ->
                        fromSingleValueCompletionStage(CompletableFuture.supplyAsync({
                                                                                         vList.stream()
                                                                                                 .parallel()
                                                                                                 .reduce(initial,
                                                                                                         accumulator::invoke,
                                                                                                         combiner::invoke)
                                                                                     }, executor))
                    }, { thr ->
                        errored(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> {
                                fromSingleValueCompletionStage(defVal.inputStreamStage.thenApplyAsync({ vStream ->
                                                                                                          vStream.parallel()
                                                                                                                  .reduce(initial,
                                                                                                                          accumulator::invoke,
                                                                                                                          combiner::invoke)
                                                                                                      }, executor))
                            }
                            is FluxValue -> {
                                fromFlux(defVal.inputFlux.publishOn(Schedulers.fromExecutor(executor))
                                                 .parallel()
                                                 .reduce({ initial }, accumulator::invoke)
                                                 .sequential())
                            }
                        }
                    })
    }

    fun partition(condition: (V) -> Boolean): Async<Pair<Sequence<V>, Sequence<V>>> {
        return fold({ vList ->
                        vList.map({ v ->
                                      v.some()
                                              .filter(condition)
                                              .fold({ v.right() }, { v.left() })
                                  })
                                .asSequence()
                                .separateEither()
                                .let { pair ->
                                    succeededSingle(Pair(pair.first.map { v -> v }, pair.second.map { v -> v }))
                                }
                    }, { thr ->
                        errored(thr)
                    }, { deferredValue ->
                        when (deferredValue) {
                            is CompletionStageValue -> {
                                fromSingleValueCompletionStage(deferredValue.inputStreamStage.thenApply { vStream ->
                                    vStream.map({ v ->
                                                    v.some()
                                                            .filter(condition)
                                                            .fold({ v.right() }, { v.left() })
                                                })
                                            .asSequence()
                                            .separateEither()
                                            .let { pair ->
                                                Pair(pair.first.map { v -> v }, pair.second.map { v -> v })
                                            }
                                })
                            }
                            is FluxValue -> {
                                fromFlux(deferredValue.inputFlux.map({ v ->
                                                                         v.some()
                                                                                 .filter(condition)
                                                                                 .fold({ v.right() }, { v.left() })

                                                                     })
                                                 .collect({ Pair(Stream.builder(), Stream.builder()) },
                                                          { pairStreamBuilders: Pair<Stream.Builder<V>, Stream.Builder<V>>, metUnmetEither: Either<V, V> ->
                                                              metUnmetEither.fold({ lv -> pairStreamBuilders.first.add(lv) },
                                                                                  { rv -> pairStreamBuilders.second.add(rv) })
                                                          })
                                                 .map({ pairStreamBuilders ->
                                                          Pair(pairStreamBuilders.first.build()
                                                                       .asSequence(),
                                                               pairStreamBuilders.second.build()
                                                                       .asSequence())
                                                      })
                                                 .flux())
                            }
                        }
                    })
    }

    fun toFlux(): Flux<out V> {
        return fold({ vList ->
                        Flux.fromStream(vList::stream)
                    }, { thr ->
                        Flux.error(thr)
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> Mono.fromCompletionStage(defVal.inputStreamStage)
                                    .flatMapMany { s -> Flux.fromStream(s) }
                            is FluxValue -> defVal.inputFlux
                        }
                    })
    }

    fun toCompletionStage(): CompletionStage<out Stream<out V>> {
        return fold({ vList ->
                        CompletableFuture.completedFuture(vList.stream())
                    }, { thr ->
                        CompletableFuture<Stream<out V>>().apply { completeExceptionally(thr) }
                    }, { defVal ->
                        when (defVal) {
                            is CompletionStageValue -> defVal.inputStreamStage // use of Flux#toStream would be a blocking operation
                            is FluxValue -> defVal.inputFlux.collectList()
                                    .toFuture()
                                    .thenApply { vList -> vList.stream() }
                        }
                    })
    }

    fun <R> fold(succeededHandler: (PersistentList<V>) -> R, erroredHandler: (Throwable) -> R, deferredHandler: (DeferredValue<V>) -> R): R


}