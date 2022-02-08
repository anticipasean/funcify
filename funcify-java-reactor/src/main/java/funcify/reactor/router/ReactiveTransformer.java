package funcify.reactor.router;

import org.reactivestreams.Publisher;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function type that takes a publisher of a {@code <R>} received type and transforms it into a publisher of type to {@code <S>}
 * send
 *
 * @author smccarron
 * @created 2/6/22
 */
public interface ReactiveTransformer<R, S> extends Function<Publisher<? extends R>, Publisher<? extends S>> {

    static <R, S> ReactiveTransformer<R, S> narrow(final ReactiveTransformer<? super R, ? extends S> widenedInstance) {
        @SuppressWarnings("unchecked") final ReactiveTransformer<R, S> narrowedInstance = (ReactiveTransformer<R, S>) widenedInstance;
        return narrowedInstance;
    }

    static <R, S> ReactiveTransformer<R, S> of(final Function<Publisher<? extends R>, Publisher<? extends S>> function) {
        Objects.requireNonNull(function, () -> "function");
        return function::apply;
    }

    @Override
    Publisher<? extends S> apply(Publisher<? extends R> receivedTypePublisher);

    default <T> ReactiveTransformer<R, T> map(final Function<? super Publisher<? extends S>, ? extends Publisher<? extends T>> mapper) {
        Objects.requireNonNull(mapper, () -> "mapper");
        return receivedTypePublisher -> {
            return mapper.apply(this.apply(receivedTypePublisher));
        };
    }

    default <T> ReactiveTransformer<R, T> flatMap(final Function<? super Publisher<? extends S>, ? extends ReactiveTransformer<R, ? extends T>> flatMapper) {
        Objects.requireNonNull(flatMapper, () -> "flatMapper");
        return receivedTypePublisher -> {
            return ReactiveTransformer.narrow(flatMapper.apply(this.apply(receivedTypePublisher))).apply(receivedTypePublisher);
        };
    }
}

