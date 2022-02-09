package funcify.container.async

import reactor.core.publisher.Flux
import java.util.concurrent.CompletionStage


/**
 *
 * @author smccarron
 * @created 2/8/22
 */
object AsyncFactory {

    sealed class DeferredValue<out V> {

        data class CompletionStageValue<V>(val inputStage: CompletionStage<V>) : DeferredValue<V>()

        data class FluxValue<V>(val inputFlux: Flux<V>) : DeferredValue<V>()
    }

    data class AsyncCompletedSuccess<V>(val value: V) : Async<V> {

        override fun <R> fold(completedSuccessHandler: (V) -> R,
                              completedFailureHandler: (Throwable) -> R,
                              deferredValueHandler: (DeferredValue<V>) -> R): R {
            return completedSuccessHandler.invoke(value)
        }

    }

    data class AsyncCompletedFailure<V>(val throwable: Throwable) : Async<V> {

        override fun <R> fold(completedSuccessHandler: (V) -> R,
                              completedFailureHandler: (Throwable) -> R,
                              deferredValueHandler: (DeferredValue<V>) -> R): R {
            return completedFailureHandler.invoke(throwable)
        }

    }

    data class AsyncDeferredValue<V>(val deferredValue: DeferredValue<V>) : Async<V> {

        override fun <R> fold(completedSuccessHandler: (V) -> R,
                              completedFailureHandler: (Throwable) -> R,
                              deferredValueHandler: (DeferredValue<V>) -> R): R {
            return deferredValueHandler.invoke(deferredValue)
        }

    }

}