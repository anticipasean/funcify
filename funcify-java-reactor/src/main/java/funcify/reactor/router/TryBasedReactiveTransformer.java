package funcify.reactor.router;


import funcify.reactor.adt.Try;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Objects;
import java.util.function.Function;


/**
 * A default transformer that receives
 *
 * @author smccarron
 * @created 2/6/22
 */
@AllArgsConstructor(staticName = "of")
@Builder
@Getter
public class TryBasedReactiveTransformer<R, S> implements ReactiveTransformer<R, S> {

    @NonNull
    @Default
    private final String onBehalfOf = "";

    @NonNull
    private final Scheduler scheduler;

    @NonNull
    private final Function<? super R, ? extends Try<? extends S>> transformationFunction;

    @Override
    public Publisher<? extends S> apply(final Publisher<? extends R> receivedTypePublisher) {
        Objects.requireNonNull(receivedTypePublisher, () -> "receivedTypePublisher");
        return Flux.from(receivedTypePublisher)
                   .publishOn(scheduler)
                   .flatMap(r -> transformationFunction.apply(r).fold(Mono::just, Mono::error));
    }

}

