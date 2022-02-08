package funcify.reactor.router;

import java.util.concurrent.Executor;
import java.util.function.Function;

import funcify.reactor.adt.Try;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author smccarron
 * @created 2/6/22
 */
public interface ReactiveTransformerFactory {

    static <R, S> ReactiveTransformer<R, S> createTransformer(final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of("",
                                              Schedulers.boundedElastic(),
                                              transformationFunction);
    }

    static <R, S> ReactiveTransformer<R, S> createTransformer(final Scheduler scheduler,
                                                              final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of("",
                                              scheduler,
                                              transformationFunction);
    }

    static <R, S> ReactiveTransformer<R, S> createTransformer(final Executor asyncExecutor,
                                                              final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of("",
                                              Schedulers.fromExecutor(asyncExecutor),
                                              transformationFunction);
    }

    static <R, S> ReactiveTransformer<R, S> createTransformer(final String onBehalfOf,
                                                              final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of(onBehalfOf,
                                              Schedulers.boundedElastic(),
                                              transformationFunction);
    }

    static <R, S> ReactiveTransformer<R, S> createTransformer(final String onBehalfOf,
                                                              final Scheduler scheduler,
                                                              final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of(onBehalfOf,
                                              scheduler,
                                              transformationFunction);
    }

    static <R, S> ReactiveTransformer<R, S> createTransformer(final String onBehalfOf,
                                                              final Executor asyncExecutor,
                                                              final Function<? super R, ? extends Try<? extends S>> transformationFunction) {
        return TryBasedReactiveTransformer.of(onBehalfOf,
                                              Schedulers.fromExecutor(asyncExecutor),
                                              transformationFunction);
    }

}

