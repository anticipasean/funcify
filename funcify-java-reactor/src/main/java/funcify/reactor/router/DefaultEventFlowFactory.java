package funcify.reactor.router;


import funcify.reactor.adt.Try;
import funcify.reactor.error.FuncifyReactorException;
import funcify.reactor.reflect.TypeOps;
import funcify.reactor.router.EventFlow.SubmissionStatus;
import funcify.reactor.router.EventFlowFactory.AnyEventAction;
import funcify.reactor.router.EventFlowFactory.EventCondition;
import funcify.reactor.router.EventFlowFactory.NextSpecificEventAction;
import funcify.reactor.router.EventFlowFactory.SpecificEventAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-08-18
 */
class DefaultEventFlowFactory {

    /**
     * @param <PE> parent event type
     */
    private static interface EventRoutingFunction<PE> {

        Class<PE> getParentEventType();

        List<EventRoutingConditionalAction<PE>> getEventRoutingConditionalActions();

        <NCE> EventRoutingFunction<PE> ifNotRoutedThen(final Class<NCE> nextChildEventType,
                                                       final Predicate<? super NCE> condition,
                                                       final List<SubscriberSelector<NCE>> subscriberSelectors);

    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    private static class EventRoutingConditionalAction<E> {

        private final Predicate<? super E> eventCondition;

        private final Function<E, Try<SubmissionStatus>> eventFlowAction;

    }

    @AllArgsConstructor(staticName = "of")
    @Getter
    private static class EventRoutingFunctionImpl<PE> implements EventRoutingFunction<PE> {

        private final Class<PE> parentEventType;

        private final List<EventRoutingConditionalAction<PE>> eventRoutingConditionalActions;

        @Override
        public <NCE> EventRoutingFunction<PE> ifNotRoutedThen(final Class<NCE> nextChildEventType,
                                                              final Predicate<? super NCE> condition,
                                                              final List<SubscriberSelector<NCE>> subscriberSelectors) {
            Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType");
            Objects.requireNonNull(condition, () -> "condition");
            Objects.requireNonNull(subscriberSelectors, () -> "subscriberSelectors");
            final Predicate<? super PE> routingCondition = pe -> TypeOps.toType(pe, nextChildEventType)
                                                                        .filter(condition)
                                                                        .isPresent();
            final Function<PE, Try<SubmissionStatus>> routingAction = pe -> {
                return Try.fromOptional(TypeOps.toType(pe, nextChildEventType), t -> {
                              final String message = String.format("unable to convert event [ type: %s ] to event [ type: %s ]",
                                                                   pe.getClass().getSimpleName(),
                                                                   nextChildEventType.getSimpleName());
                              return new FuncifyReactorException(message);
                          })
                          .flatMap(nce -> subscriberSelectors.stream()
                                                             .reduce(Try.success(SubmissionStatus.NOT_YET_SUBMITTED),
                                                                     (submissionAttempt, selector) -> {
                                                                         return submissionAttempt.flatMap(ss -> selector.apply(nce));
                                                                     },
                                                                     (t1, t2) -> {
                                                                         // When combining, take error status of either attempt first else mark SUBMITTED
                                                                         return t1.zip(t2,
                                                                                       (ss1, ss2) -> ss1 ==
                                                                                                     SubmissionStatus.ERROR_DURING_SUBMISSION ?
                                                                                                     ss1 : ss2 ==
                                                                                                           SubmissionStatus.ERROR_DURING_SUBMISSION ?
                                                                                                           ss2 :
                                                                                                           SubmissionStatus.SUBMITTED);
                                                                     }));
            };
            final EventRoutingConditionalAction<PE> conditionalAction = EventRoutingConditionalAction.<PE>of(routingCondition,
                                                                                                             routingAction);
            final List<EventRoutingConditionalAction<PE>> eventRoutingConditionalActions = new ArrayList<>(
                    getEventRoutingConditionalActions());
            eventRoutingConditionalActions.add(conditionalAction);
            return EventRoutingFunctionImpl.of(parentEventType, eventRoutingConditionalActions);
        }


    }

    @FunctionalInterface
    private static interface SubscriberSelector<E> {

        Try<SubmissionStatus> apply(E event);

    }

    @AllArgsConstructor(staticName = "of")
    private static class ReactiveTransformerSelector<E, T> implements SubscriberSelector<E> {

        private final Function<? super E, ? extends ReactiveTransformer<E, T>> reactiveTransformerSelector;

        private final Subscriber<? super T> subscriber;


        @Override
        public Try<SubmissionStatus> apply(final E event) {
            final ReactiveTransformer<E, T> reactiveTransformer = reactiveTransformerSelector.apply(event);
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            return Try.attempt(() -> Flux.from(reactiveTransformer.apply(Mono.just(event))).subscribe(subscriber))
                      .map(empty -> SubmissionStatus.SUBMITTED)
                      .flatMapFailure(throwable -> Try.attempt(() -> Mono.from(reactiveTransformer.apply(Mono.<E>error(throwable)))
                                                                         .subscribe(subscriber))
                                                      .map(empty -> SubmissionStatus.ERROR_DURING_SUBMISSION));

        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class ReactiveTransformerConsumerSelector<E, T> implements SubscriberSelector<E> {

        private final Function<? super E, ? extends ReactiveTransformer<E, T>> reactiveTransformerSelector;

        private final Function<? super E, ? extends BiConsumer<? super T, Throwable>> consumerSelector;


        @Override
        public Try<SubmissionStatus> apply(final E event) {
            final ReactiveTransformer<E, T> reactiveTransformer = reactiveTransformerSelector.apply(event);
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            final BiConsumer<? super T, Throwable> biConsumer = consumerSelector.apply(event);
            Objects.requireNonNull(biConsumer, () -> "biConsumer");
            return Try.attempt(() -> Flux.from(reactiveTransformer.apply(Mono.just(event)))
                                         .subscribe(t -> biConsumer.accept(t, null), thr -> biConsumer.accept(null, thr)))
                      .map(empty -> SubmissionStatus.SUBMITTED)
                      .flatMapFailure(throwable -> Try.attempt(() -> Mono.from(reactiveTransformer.apply(Mono.<E>error(throwable)))
                                                                         .subscribe(t -> biConsumer.accept(t, null),
                                                                                    thr -> biConsumer.accept(null, thr)))
                                                      .map(empty -> SubmissionStatus.ERROR_DURING_SUBMISSION));

        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class ReactiveStreamsSubscriberSelector<E> implements SubscriberSelector<E> {

        private final Function<? super E, ? extends Subscriber<? super E>> subscriberSelector;

        @Override
        public Try<SubmissionStatus> apply(final E event) {
            final Subscriber<? super E> subscriber = subscriberSelector.apply(event);
            Objects.requireNonNull(subscriber, () -> "subscriber");
            return Try.attempt(() -> Mono.just(event).subscribe(subscriber))
                      .map(empty -> SubmissionStatus.SUBMITTED)
                      .flatMapFailure(throwable -> Try.attempt(() -> Mono.<E>error(throwable).subscribe(subscriber))
                                                      .map(empty -> SubmissionStatus.ERROR_DURING_SUBMISSION));
        }
    }

    @AllArgsConstructor(staticName = "of")
    private static class BiConsumerSelector<E> implements SubscriberSelector<E> {

        private final Function<? super E, ? extends BiConsumer<? super E, Throwable>> consumerSelector;

        @Override
        public Try<SubmissionStatus> apply(final E event) {
            final BiConsumer<? super E, Throwable> biConsumer = consumerSelector.apply(event);
            Objects.requireNonNull(biConsumer, () -> "biConsumer");
            return Try.attempt(() -> biConsumer.accept(event, null))
                      .map(empty -> SubmissionStatus.SUBMITTED)
                      .flatMapFailure(throwable -> Try.attempt(() -> biConsumer.accept(null, throwable))
                                                      .map(empty -> SubmissionStatus.ERROR_DURING_SUBMISSION));
        }
    }


    @AllArgsConstructor(staticName = "of")
    static class FlowStartImpl implements EventFlowFactory.FlowStart {

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
                                              Objects.requireNonNull(childEventType, () -> "childEventType"),
                                              ce -> {
                                                  return Try.attempt(() -> contextualInputCondition.test(ce, contextualInput))
                                                            .orElse(Boolean.FALSE);
                                              });
        }

        @Override
        public <CE extends PE, CI> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                        final CI contextualInput,
                                                                        final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction,
                                              Objects.requireNonNull(childEventType, () -> "childEventType"),
                                              ce -> {
                                                  return Try.attempt(() -> contextCondition.test(contextualInput))
                                                            .orElse(Boolean.FALSE);
                                              });
        }

        @Override
        public AnyEventAction<PE> forAnyEvent() {
            return new AnyEventActionImpl<>(eventRoutingFunction, pe -> true);
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
        public <T> NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                           final Subscriber<? super T> subscriber) {
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            Objects.requireNonNull(subscriber, () -> "subscriber");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(
                                                          reactiveTransformer), subscriber))));
        }

        @Override
        public <T> NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                           final Consumer<? super T> consumer,
                                                           final Consumer<? super Throwable> errorConsumer) {
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            Objects.requireNonNull(consumer, () -> "consumer");
            Objects.requireNonNull(errorConsumer, () -> "errorConsumer");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerConsumerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(
                                                          reactiveTransformer), ce -> {
                                                      return (t, throwable) -> {
                                                          if (t != null) {
                                                              consumer.accept(t);
                                                          } else {
                                                              errorConsumer.accept(throwable);
                                                          }
                                                      };
                                                  }))));
        }

        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final Subscriber<? super CE> subscriber) {
            Objects.requireNonNull(subscriber, () -> "subscriber");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveStreamsSubscriberSelector.of(ce -> subscriber))));
        }

        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final Consumer<? super CE> consumer,
                                                       final Consumer<? super Throwable> errorConsumer) {
            Objects.requireNonNull(consumer, () -> "consumer");
            Objects.requireNonNull(errorConsumer, () -> "errorConsumer");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(BiConsumerSelector.of(ce -> {
                                                      return (e, throwable) -> {
                                                          if (e != null) {
                                                              consumer.accept(e);
                                                          } else {
                                                              errorConsumer.accept(throwable);
                                                          }
                                                      };
                                                  }))));
        }

        @Override
        public <T> NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                           final Subscriber<? super T> subscriber) {
            Objects.requireNonNull(reactiveTransformerSelector, () -> "reactiveTransformerSelector");
            Objects.requireNonNull(subscriber, () -> "subscriber");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(
                                                          reactiveTransformerSelector.apply(ce)), subscriber))));
        }

        @Override
        public <T> NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                           final Consumer<? super T> consumer,
                                                           final Consumer<? super Throwable> errorConsumer) {
            Objects.requireNonNull(reactiveTransformerSelector, () -> "reactiveTransformerSelector");
            Objects.requireNonNull(consumer, () -> "consumer");
            Objects.requireNonNull(errorConsumer, () -> "errorConsumer");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveTransformerConsumerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(
                                                          reactiveTransformerSelector.apply(ce)), ce -> {
                                                      return (t, throwable) -> {
                                                          if (t != null) {
                                                              consumer.accept(t);
                                                          } else {
                                                              errorConsumer.accept(throwable);
                                                          }
                                                      };
                                                  }))));
        }

        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends Subscriber<? super CE>> subscriberSelector) {
            Objects.requireNonNull(subscriberSelector, () -> "subscriberSelector");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(ReactiveStreamsSubscriberSelector.of(
                                                          subscriberSelector))));
        }

        @Override
        public NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends Consumer<? super CE>> consumerSelector,
                                                       final Function<? super CE, ? extends Consumer<? super Throwable>> errorConsumerSelector) {
            Objects.requireNonNull(consumerSelector, () -> "consumerSelector");
            Objects.requireNonNull(errorConsumerSelector, () -> "errorConsumerSelector");
            return NextSpecificEventActionImpl.of(eventRoutingFunction,
                                                  childEventType,
                                                  eventCondition,
                                                  new ArrayList<>(Arrays.asList(BiConsumerSelector.of(ce -> {
                                                      final Consumer<? super CE> consumer = Objects.requireNonNull(
                                                              consumerSelector.apply(ce),
                                                              "consumer");
                                                      final Consumer<? super Throwable> errorConsumer = Objects.requireNonNull(
                                                              errorConsumerSelector.apply(ce),
                                                              "errorConsumer");
                                                      return (e, throwable) -> {
                                                          if (e != null) {
                                                              consumer.accept(e);
                                                          } else {
                                                              errorConsumer.accept(throwable);
                                                          }
                                                      };
                                                  }))));
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
        private final List<SubscriberSelector<CE>> subscriberSelectors;

        @Override
        public <T> NextSpecificEventAction<PE, CE> and(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                       final Subscriber<? super T> subscriber) {
            Objects.requireNonNull(reactiveTransformer, () -> "reactiveTransformer");
            Objects.requireNonNull(subscriber, () -> "subscriber");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(ReactiveTransformerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(reactiveTransformer), subscriber));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public <T> NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                       final Subscriber<? super T> subscriber) {
            Objects.requireNonNull(reactiveTransformerSelector, () -> "reactiveTransformerSelector");
            Objects.requireNonNull(subscriber, () -> "subscriber");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(ReactiveTransformerSelector.<CE, T>of(ce -> ReactiveTransformer.narrow(reactiveTransformerSelector.apply(
                    ce)), subscriber));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public NextSpecificEventAction<PE, CE> and(final Subscriber<? super CE> subscriber) {
            Objects.requireNonNull(subscriber, () -> "subscriber");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(ReactiveStreamsSubscriberSelector.of(ce -> subscriber));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public NextSpecificEventAction<PE, CE> and(final Consumer<? super CE> consumer,
                                                   final Consumer<? super Throwable> errorConsumer) {
            Objects.requireNonNull(consumer, () -> "consumer");
            Objects.requireNonNull(errorConsumer, () -> "errorConsumer");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(BiConsumerSelector.of(ce -> {
                return (e, throwable) -> {
                    if (e != null) {
                        consumer.accept(e);
                    } else {
                        errorConsumer.accept(throwable);
                    }
                };
            }));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends Subscriber<? super CE>> subscriberSelector) {
            Objects.requireNonNull(subscriberSelector, () -> "subscriberSelector");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(ReactiveStreamsSubscriberSelector.of(subscriberSelector));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends Consumer<? super CE>> consumerSelector,
                                                   final Function<? super CE, ? extends Consumer<? super Throwable>> errorConsumerSelector) {
            Objects.requireNonNull(consumerSelector, () -> "consumerSelector");
            Objects.requireNonNull(errorConsumerSelector, () -> "errorConsumerSelector");
            final List<SubscriberSelector<CE>> newList = new LinkedList<>(subscriberSelectors);
            newList.add(BiConsumerSelector.of(ce -> {
                final Consumer<? super CE> nextConsumer = Objects.requireNonNull(consumerSelector.apply(ce), () -> "consumer");
                final Consumer<? super Throwable> errorConsumer = Objects.requireNonNull(errorConsumerSelector.apply(ce),
                                                                                         () -> "errorConsumer");
                return (childEvent, throwable) -> {
                    if (childEvent != null) {
                        nextConsumer.accept(childEvent);
                    } else {
                        errorConsumer.accept(throwable);
                    }
                };
            }));
            return NextSpecificEventActionImpl.of(eventRoutingFunction, childEventType, eventCondition, newList);
        }

        @Override
        public <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType) {
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   subscriberSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> true);
        }

        @Override
        public <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                      final Predicate<? super NCE> nextEventCondition) {
            Objects.requireNonNull(nextEventCondition, () -> "nextEventCondition");
            return SpecificEventActionImpl.of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                   eventCondition,
                                                                                   subscriberSelectors),
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
                                                                                   subscriberSelectors),
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
                                                                                   subscriberSelectors),
                                              Objects.requireNonNull(nextChildEventType, () -> "nextChildEventType"),
                                              ce -> Try.attempt(() -> contextCondition.test(contextualInput))
                                                       .orElse(Boolean.FALSE));
        }

        @Override
        public AnyEventAction<PE> forAnyEvent() {
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 subscriberSelectors), pe -> true);
        }

        @Override
        public AnyEventAction<PE> forAnyEvent(final Predicate<? super PE> nextEventCondition) {
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 subscriberSelectors),
                                            Objects.requireNonNull(nextEventCondition, () -> "nextEventCondition"));
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final BiPredicate<? super PE, ? super CI> contextualInputCondition) {
            Objects.requireNonNull(contextualInputCondition, () -> "contextualInputCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 subscriberSelectors),
                                            pe -> Try.attempt(() -> contextualInputCondition.test(pe, contextualInput))
                                                     .orElse(Boolean.FALSE));
        }

        @Override
        public <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                                   final Predicate<? super CI> contextCondition) {
            Objects.requireNonNull(contextCondition, () -> "contextCondition");
            return new AnyEventActionImpl<>(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                 eventCondition,
                                                                                 subscriberSelectors),
                                            pe -> Try.attempt(() -> contextCondition.test(contextualInput))
                                                     .orElse(Boolean.FALSE));
        }

        @Override
        public EventFlow<PE> endFlow() {
            return DefaultEventFlow.<PE>of(eventRoutingFunction.ifNotRoutedThen(childEventType,
                                                                                eventCondition,
                                                                                subscriberSelectors));
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class DefaultEventFlow<PE> implements EventFlow<PE> {

        private final EventRoutingFunction<PE> eventRoutingFunction;

        @Override
        public Try<SubmissionStatus> apply(final PE event) {
            return eventRoutingFunction.getEventRoutingConditionalActions()
                                       .stream()
                                       .filter(eventRoutingConditionalAction -> eventRoutingConditionalAction.getEventCondition()
                                                                                                             .test(event))
                                       .findFirst()
                                       .map(EventRoutingConditionalAction::getEventFlowAction)
                                       .map(routingFunction -> routingFunction.apply(event))
                                       .orElseGet(() -> {
                                           final String message = String.format(
                                                   "event [ sub_type: %s ] was not handled by any condition specified for routing",
                                                   Optional.ofNullable(event)
                                                           .map(Object::getClass)
                                                           .map(Class::getTypeName)
                                                           .orElse("null"));
                                           return Try.failure(new UnhandledEventConditionException(message));
                                       });
        }
    }

}

