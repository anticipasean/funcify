package funcify.reactor.router;


import funcify.reactor.adt.Try;
import funcify.reactor.error.FuncifyReactorException;
import funcify.reactor.reflect.TypeOps;
import funcify.reactor.router.ReactiveEventFlowFactory.AnyEventAction;
import funcify.reactor.router.ReactiveEventFlowFactory.EventCondition;
import funcify.reactor.router.ReactiveEventFlowFactory.NextSpecificEventAction;
import funcify.reactor.router.ReactiveEventFlowFactory.SpecificEventAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author smccarron
 * @created 2021-08-18
 */
class DefaultReactiveEventFlowFactory {

    /**
     * @param <PE> parent event type
     */
    private static interface EventRoutingFunction<PE> {

        Class<PE> getParentEventType();

        List<EventRoutingConditionalAction<PE>> getEventRoutingConditionalActions();

        <NCE> EventRoutingFunction<PE> ifNotRoutedThen(final Class<NCE> nextChildEventType,
                                                       final Predicate<? super NCE> condition,
                                                       final List<TransformerSelector<NCE, PE>> transformerSelectors);
    }

    @FunctionalInterface
    private static interface TransformerSelector<E1, E2> {

        Publisher<? extends E2> apply(E1 event);

    }

    @AllArgsConstructor(staticName = "of")
    private static class ReactiveTransformerSelector<CE, PE> implements TransformerSelector<CE, PE> {

        @NonNull
        private final Function<? super CE, ? extends ReactiveTransformer<? super CE, ? extends PE>> reactiveTransformerSelector;


        @Override
        public Publisher<? extends PE> apply(final CE event) {
            return Mono.justOrEmpty(event).flatMapMany(e -> reactiveTransformerSelector.apply(e).apply(Mono.just(e)));

        }
    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    private static class EventRoutingConditionalAction<E> {

        private final Predicate<? super E> eventCondition;

        private final Function<E, Publisher<? extends E>> eventFlowAction;

    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    private static class EventRoutingFunctionImpl<PE> implements EventRoutingFunction<PE> {

        private final Class<PE> parentEventType;

        private final List<EventRoutingConditionalAction<PE>> eventRoutingConditionalActions;

        @Override
        public <NCE> EventRoutingFunction<PE> ifNotRoutedThen(final Class<NCE> nextChildEventType,
                                                              final Predicate<? super NCE> condition,
                                                              final List<TransformerSelector<NCE, PE>> transformerSelectors) {
            Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType");
            Objects.requireNonNull(condition, () -> "condition");
            Objects.requireNonNull(transformerSelectors, () -> "transformerSelectors");
            final Predicate<? super PE> routingCondition = pe -> TypeOps.toType(pe, nextChildEventType)
                                                                        .filter(condition)
                                                                        .isPresent();
            final Function<PE, Publisher<? extends PE>> routingAction = pe -> {
                return Try.fromOptional(TypeOps.toType(pe, nextChildEventType), t -> {
                    final String message = String.format("unable to convert event [ type: %s ] to event [ type: %s ]",
                                                         pe.getClass().getSimpleName(),
                                                         nextChildEventType.getSimpleName());
                    return new FuncifyReactorException(message);
                }).fold(nce -> {
                    return Flux.merge(transformerSelectors.stream().map(nts -> nts.apply(nce)).collect(Collectors.toList()));
                }, Flux::<PE>error);
            };
            final EventRoutingConditionalAction<PE> conditionalAction = EventRoutingConditionalAction.<PE>of(routingCondition,
                                                                                                             routingAction);
            final List<EventRoutingConditionalAction<PE>> eventRoutingConditionalActions = new ArrayList<>(
                    getEventRoutingConditionalActions());
            eventRoutingConditionalActions.add(conditionalAction);
            return EventRoutingFunctionImpl.of(parentEventType, eventRoutingConditionalActions);
        }


    }


    @AllArgsConstructor(staticName = "of")
    static class FlowStartImpl implements ReactiveEventFlowFactory.FlowStart {

        @Override
        public <PE> EventCondition<PE> beginFlow(final Class<PE> parentEventType) {
            return EventConditionImpl.of(EventRoutingFunctionImpl.of(Objects.requireNonNull(parentEventType,
                                                                                            () -> "parentEventType"),
                                                                     new ArrayList<>()));
        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class EventConditionImpl<PE> implements EventCondition<PE> {

        private final EventRoutingFunction<PE> eventRoutingFunction;

        @Override
        public <CE extends PE> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType) {
            return SpecificEventActionImpl.of(eventRoutingFunction, childEventType, ce -> true);
        }

        @Override
        public <CE extends PE> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                    final Predicate<? super CE> eventCondition) {
            return SpecificEventActionImpl.of(eventRoutingFunction,

                                              Objects.requireNonNull(childEventType, () -> "childEventType"),
                                              Objects.requireNonNull(eventCondition, () -> "eventCondition"));
        }

        @Override
        public <CE extends PE, CI> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                        final CI contextualInput,
                                                                        final BiPredicate<? super CE, ? super CI> contextualInputCondition) {
            Objects.requireNonNull(contextualInputCondition, () -> "contextualInputCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction,

                                              Objects.requireNonNull(childEventType, () -> "childEventType"), ce -> {
                        return Try.attempt(() -> contextualInputCondition.test(ce, contextualInput)).orElse(Boolean.FALSE);
                    });
        }

        @Override
        public <CE extends PE, CI> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                        final CI contextualInput,
                                                                        final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction,

                                              Objects.requireNonNull(childEventType, () -> "childEventType"), ce -> {
                        return Try.attempt(() -> contextCondition.test(contextualInput)).orElse(Boolean.FALSE);
                    });
        }

        @Override
        public AnyEventAction<PE> forAnyEvent() {
            return new AnyEventActionImpl<>(eventRoutingFunction,

                                            pe -> true);
        }

        @Override
        public AnyEventAction<PE> forAnyEvent(final Predicate<? super PE> eventCondition) {
            Objects.requireNonNull(eventCondition, () -> "eventCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction, pe -> {
                return Try.attempt(() -> eventCondition.test(pe)).orElse(Boolean.FALSE);
            });
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final BiPredicate<? super PE, ? super CI> contextualInputCondition) {
            Objects.requireNonNull(contextualInputCondition, () -> "contextualInputCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction, pe -> {
                return Try.attempt(() -> contextualInputCondition.test(pe, contextualInput)).orElse(Boolean.FALSE);
            });
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction, pe -> {
                return Try.attempt(() -> contextCondition.test(contextualInput)).orElse(Boolean.FALSE);
            });
        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class SpecificEventActionImpl<PE, CE> implements SpecificEventAction<PE, CE> {

        private final EventRoutingFunction<PE> eventRoutingFunction;
        private final Class<CE> childEventType;
        private final Predicate<? super CE> eventCondition;


        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, ? extends PE> reactiveTransformer) {
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerSelector.<CE, PE>of(ce -> reactiveTransformer))));
        }

        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, ? extends PE>> reactiveTransformerSelector) {
            Objects.requireNonNull(reactiveTransformerSelector, () -> "reactiveTransformerSelector");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerSelector.<CE, PE>of(
                                                          reactiveTransformerSelector))));
        }
    }

    private static class AnyEventActionImpl<PE> extends SpecificEventActionImpl<PE, PE> implements AnyEventAction<PE> {

        private AnyEventActionImpl(final EventRoutingFunction<PE> eventRoutingFunction,
                                   final Predicate<? super PE> eventCondition) {
            super(eventRoutingFunction, eventRoutingFunction.getParentEventType(), eventCondition);
        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class NextSpecificEventActionImpl<PE, CE> implements NextSpecificEventAction<PE, CE> {

        private final EventRoutingFunction<PE> eventRoutingFunction;
        private final Class<CE> childEventType;
        private final Predicate<? super CE> eventCondition;
        private final List<TransformerSelector<CE, PE>> transformerSelectors;


        @Override
        public NextSpecificEventAction<PE, CE> and(final ReactiveTransformer<? super CE, ? extends PE> reactiveTransformer) {
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            final List<TransformerSelector<CE, PE>> newList = new ArrayList<>(transformerSelectors);
            newList.add(ReactiveTransformerSelector.of(ce -> reactiveTransformer));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends ReactiveTransformer<? super CE, ? extends PE>> reactiveTransformerSelector) {
            Objects.requireNonNull(reactiveTransformerSelector, () -> "reactiveTransformerSelector");
            final List<TransformerSelector<CE, PE>> newList = new ArrayList<>(transformerSelectors);
            newList.add(ReactiveTransformerSelector.of(reactiveTransformerSelector));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType) {
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   transformerSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> true);
        }

        @Override
        public <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                      final Predicate<? super NCE> nextEventCondition) {
            Objects.requireNonNull(nextEventCondition, () -> "nextEventCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   transformerSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> Try.attempt(() -> nextEventCondition.test(ce)).orElse(Boolean.FALSE));
        }

        @Override
        public <NCE extends PE, CI> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                          final CI contextualInput,
                                                                          final BiPredicate<? super NCE, ? super CI> contextualInputCondition) {
            Objects.requireNonNull(contextualInputCondition, () -> "contextualInputCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   transformerSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> Try.attempt(() -> contextualInputCondition.test(ce, contextualInput))
                                                       .orElse(Boolean.FALSE));
        }

        @Override
        public <NCE extends PE, CI> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                          final CI contextualInput,
                                                                          final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   transformerSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> Try.attempt(() -> contextCondition.test(contextualInput))
                                                       .orElse(Boolean.FALSE));
        }

        @Override
        public AnyEventAction<PE> forAnyEvent() {
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 transformerSelectors), pe -> true);
        }

        @Override
        public AnyEventAction<PE> forAnyEvent(final Predicate<? super PE> nextEventCondition) {
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 transformerSelectors),
                                            Objects.requireNonNull(nextEventCondition, () -> "nextEventCondition"));
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final BiPredicate<? super PE, ? super CI> contextualInputCondition) {
            Objects.requireNonNull(contextualInputCondition, () -> "contextualInputCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 transformerSelectors),
                                            pe -> Try.attempt(() -> contextualInputCondition.test(pe, contextualInput))
                                                     .orElse(Boolean.FALSE));
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 transformerSelectors),
                                            pe -> Try.attempt(() -> contextCondition.test(contextualInput))
                                                     .orElse(Boolean.FALSE));
        }

        @Override
        public ReactiveEventFlow<PE> endFlow() {
            return DefaultReactiveEventFlow.<PE>of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                        eventCondition,
                                                                                        transformerSelectors));
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class DefaultReactiveEventFlow<PE> implements ReactiveEventFlow<PE> {

        @NonNull
        private final EventRoutingFunction<PE> eventRoutingFunction;

        @Override
        public Publisher<? extends PE> apply(final Publisher<? extends PE> receivedTypePublisher) {
            Objects.requireNonNull(receivedTypePublisher, () -> "receivedTypePublisher");
            return Flux.from(receivedTypePublisher).flatMap(pe -> {
                return eventRoutingFunction.getEventRoutingConditionalActions()
                                           .stream()
                                           .filter(conditionalAction -> Try.attempt(() -> conditionalAction.getEventCondition()
                                                                                                           .test(pe))
                                                                           .orElse(Boolean.FALSE))
                                           .findFirst()
                                           .map(EventRoutingConditionalAction::getEventFlowAction)
                                           .map(actionFunc -> Try.attempt(() -> actionFunc.apply(pe))
                                                                 .fold(Flux::<PE>from, Flux::<PE>error))
                                           .orElseGet(() -> {
                                               final String message = String.format(
                                                       "did not find matching condition for routing event [ type: %s ] to a destination",
                                                       pe.getClass().getSimpleName());
                                               return Flux.error(new ReactiveEventFlow.UnhandledEventConditionException(message));
                                           });
            });
        }
    }

}

