package funcify.reactor.router;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.One;
import reactor.util.context.Context;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author smccarron
 * @created 2021-08-20
 */
public class EventFlowTest {

    private final One<SandwichEvent> sink = Sinks.<SandwichEvent>one();
    private final AtomicReference<String> outputHolder = new AtomicReference<>();
    private final Consumer<String> outputHolderUpdater = s -> {
        outputHolder.compareAndSet(outputHolder.get(), s);
    };
    private final Subscriber<SliceBread> sliceBreadSubscriber = eventSubscriber(SliceBread.class, sliceBread -> {
        outputHolderUpdater.accept(sliceBread.getNumberOfSlices() + " slices of bread");
    });
    private final Subscriber<SliceVegetables> sliceVeggiesSubscriber = eventSubscriber(SliceVegetables.class,
                                                                                       sliceVegetables -> outputHolderUpdater.accept(
                                                                                               "slices of " +
                                                                                               sliceVegetables.getTypesOfVegetables()
                                                                                                              .stream()
                                                                                                              .collect(Collectors.joining(
                                                                                                                      ", ",
                                                                                                                      "[ ",
                                                                                                                      " ]"))));
    private final Subscriber<StackSlicedIngredients> stackSlicedSubscriber = eventSubscriber(StackSlicedIngredients.class,
                                                                                             stackSlicedIngredients -> outputHolderUpdater.accept(
                                                                                                     "in the following order " +
                                                                                                     stackSlicedIngredients.getBottomToTopIngredients()
                                                                                                                           .stream()
                                                                                                                           .collect(
                                                                                                                                   Collectors.joining(
                                                                                                                                           ", ",
                                                                                                                                           "[ ",
                                                                                                                                           " ]"))));
    private final Subscriber<ToastSandwich> toastSandwichSubscriber1 = eventSubscriber(ToastSandwich.class,
                                                                                       e -> outputHolderUpdater.accept(
                                                                                               "can toast " + e.getBreadType()));
    private final Subscriber<ToastSandwich> toastSandwichSubscriber2 = eventSubscriber(ToastSandwich.class,
                                                                                       e -> outputHolderUpdater.accept(
                                                                                               "can not toast " +
                                                                                               e.getBreadType()));
    private final EventFlow<SandwichEvent> eventFlow = getEventFlow(sliceBreadSubscriber,
                                                                    sliceVeggiesSubscriber,
                                                                    stackSlicedSubscriber,
                                                                    toastSandwichSubscriber1,
                                                                    toastSandwichSubscriber2);

    static EventFlow<SandwichEvent> getEventFlow(final Subscriber<SliceBread> sliceBreadSubscriber,
                                                 final Subscriber<SliceVegetables> sliceVegetablesSubscriber,
                                                 final Subscriber<StackSlicedIngredients> stackSlicedIngredientsSubscriber,
                                                 final Subscriber<ToastSandwich> toastSandwichSubscriber1,
                                                 final Subscriber<ToastSandwich> toastSandwichSubscriber2) {
        return EventFlowFactory.defaultBuilder()
                               .beginFlow(SandwichEvent.class)
                               .forEvent(SliceBread.class)
                               .routeTo(sliceBreadSubscriber)
                               .forEvent(SliceVegetables.class,
                                         sliceVegetables -> sliceVegetables.getTypesOfVegetables().size() > 2)
                               .routeTo(sliceVegetablesSubscriber)
                               .forEvent(StackSlicedIngredients.class)
                               .routeTo(stackSlicedIngredientsSubscriber)
                               .forEvent(ToastSandwich.class)
                               .routeTo(toastSandwichSubscriber1)
                               .and(toastSandwichSubscriber2)
                               .endFlow();
    }

    static <E> Subscriber<E> eventSubscriber(final Class<E> eventType,
                                             final Consumer<? super E> onNextHandler) {
        return new BaseSubscriber<E>() {
            @Override
            protected void hookOnSubscribe(final Subscription subscription) {
                Optional.ofNullable(subscription).ifPresent(s -> s.request(1));
            }

            @Override
            protected void hookOnNext(final E value) {
                onNextHandler.accept(value);
            }

        };
    }

    @BeforeEach
    void setUp() {
        outputHolder.compareAndSet(outputHolder.get(), null);
    }

    @Test
    public void routeToFirstEventTypeInEventFlow() {
        sink.tryEmitValue(SliceBread.of(2));
        sink.asMono().subscribe(sandwichEvent -> eventFlow.apply(sandwichEvent).onComplete((submissionStatus, throwable) -> {
            if (throwable != null) {
                Assertions.fail(throwable);
            }
        })).dispose();
        Assertions.assertEquals("2 slices of bread", outputHolder.get());

    }

    @Test
    public void routeToLastEventTypeInEventFlowSkippingFirstTwoEventTypesSpecified() {
        sink.tryEmitValue(StackSlicedIngredients.of(Arrays.asList("bread slice",
                                                                  "sliced turkey",
                                                                  "lettuce",
                                                                  "sliced tomato",
                                                                  "sliced onions",
                                                                  "slice of cheese")));
        sink.asMono().subscribe(sandwichEvent -> eventFlow.apply(sandwichEvent).onComplete((submissionStatus, throwable) -> {
            if (throwable != null) {
                Assertions.fail(throwable);
            }
        })).dispose();
        Assertions.assertEquals(
                "in the following order [ bread slice, sliced turkey, lettuce, sliced tomato, sliced onions, slice of cheese ]",
                outputHolder.get());
    }

    @Test
    public void routeToUnhandledEventTypeInEventFlow() {
        sink.tryEmitValue(SpreadDressing.of("pesto"));
        sink.asMono()
            .subscribe(event -> eventFlow.apply(event)
                                         .ifFailed(throwable -> outputHolderUpdater.accept(throwable.getClass().getName())))
            .dispose();
        Assertions.assertEquals(EventFlow.UnhandledEventConditionException.class.getName(), outputHolder.get());
    }

    @Test
    public void failToRouteToEventTypeWithConditionIfConditionUnmet() {
        sink.tryEmitValue(SliceVegetables.of(Arrays.asList("tomatoes", "onions")));
        sink.asMono()
            .subscribe(event -> eventFlow.apply(event)
                                         .ifFailed(throwable -> outputHolderUpdater.accept(throwable.getClass().getName())))
            .dispose();
        Assertions.assertEquals(EventFlow.UnhandledEventConditionException.class.getName(), outputHolder.get());
    }

    @Test
    public void routeToEventTypeWithCondition() {
        sink.tryEmitValue(SliceVegetables.of(Arrays.asList("tomatoes", "onions", "green peppers")));
        sink.asMono().subscribe(event -> eventFlow.apply(event).onComplete((submissionStatus, throwable) -> {
            if (throwable != null) {
                Assertions.fail(throwable);
            }
        })).dispose();
        Assertions.assertEquals("slices of [ tomatoes, onions, green peppers ]", outputHolder.get());
    }

    @Test
    public void routeToTwoSubscribersOfEventType() {
        sink.tryEmitValue(ToastSandwich.of("focaccia"));
        sink.asMono().subscribe(event -> eventFlow.apply(event).onComplete((submissionStatus, throwable) -> {
            if (throwable != null) {
                Assertions.fail(throwable);
            }
        })).dispose();
        Assertions.assertEquals("can not toast focaccia", outputHolder.get());
    }

    /**
     * Tests use of subscriber with context supplied
     */
    @Test
    public void contextPassingTest() {
        final Subscriber<? super ToastSandwich> reactiveStreamsSubscriber = eventSubscriberWithContext(toastSandwichSubscriber1,
                                                                                                       Context.of(String.class,
                                                                                                                  "lightly"));
        Flux.just(ToastSandwich.of("whole wheat"))
            .transformDeferredContextual((toastSandwichFlux, contextView) -> toastSandwichFlux.map(toastSandwich -> {
                if (contextView.hasKey(String.class)) {
                    return ToastSandwich.of(String.join(" ", contextView.get(String.class), toastSandwich.getBreadType()));
                }
                return toastSandwich;
            }))
            .subscribe(reactiveStreamsSubscriber);
        Assertions.assertEquals("can toast lightly whole wheat", outputHolder.get());

    }

    static <E> Subscriber<E> eventSubscriberWithContext(final Subscriber<E> subscriber,
                                                        final Context context) {
        return new BaseSubscriber<E>() {

            @Override
            public Context currentContext() {
                return Optional.ofNullable(context).orElseGet(Context::empty);
            }

            @Override
            protected void hookOnSubscribe(final Subscription subscription) {
                Optional.ofNullable(subscription).ifPresent(s -> s.request(1));
            }

            @Override
            protected void hookOnNext(final E value) {
                Optional.ofNullable(subscriber).ifPresent(s -> s.onNext(value));
            }

        };
    }

    static interface SandwichEvent {

    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @ToString
    static class SliceBread implements SandwichEvent {

        private final int numberOfSlices;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @ToString
    static class SliceVegetables implements SandwichEvent {

        private final List<String> typesOfVegetables;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @ToString
    static class StackSlicedIngredients implements SandwichEvent {

        private final List<String> bottomToTopIngredients;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @ToString
    static class SpreadDressing implements SandwichEvent {

        private final String dressingType;
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    @ToString
    static class ToastSandwich implements SandwichEvent {

        private final String breadType;
    }

}
