package funcify.container.async

import reactor.core.publisher.Flux
import java.util.concurrent.CompletionStage
import java.util.stream.Stream


/**
 *
 * @author smccarron
 * @created 2/8/22
 */
object AsyncFactory {

    sealed class DeferredValue<out V> {

        data class CompletionStageValue<V>(val inputStreamStage: CompletionStage<Stream<out V>>) : DeferredValue<V>()

        data class FluxValue<V>(val inputFlux: Flux<V>) : DeferredValue<V>()
    }

    data class AsyncCompletedSuccess<V>(val valueStream: Stream<out V>) : Async<V> {

        override fun <R> fold(succeededHandler: (Stream<out V>) -> R,
                              erroredHandler: (Throwable) -> R,
                              deferredHandler: (DeferredValue<V>) -> R): R {
            return succeededHandler.invoke(valueStream)
        }

    }

    data class AsyncCompletedFailure<V>(val throwable: Throwable) : Async<V> {

        override fun <R> fold(succeededHandler: (Stream<out V>) -> R,
                              erroredHandler: (Throwable) -> R,
                              deferredHandler: (DeferredValue<V>) -> R): R {
            return erroredHandler.invoke(throwable)
        }

    }

    data class AsyncDeferredValue<V>(val deferredValue: DeferredValue<V>) : Async<V> {

        override fun <R> fold(succeededHandler: (Stream<out V>) -> R,
                              erroredHandler: (Throwable) -> R,
                              deferredHandler: (DeferredValue<V>) -> R): R {
            return deferredHandler.invoke(deferredValue)
        }

    }

}