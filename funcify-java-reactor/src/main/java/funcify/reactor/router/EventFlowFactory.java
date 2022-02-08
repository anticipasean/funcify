package funcify.reactor.router;


import org.reactivestreams.Subscriber;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2021-08-18
 */
public interface EventFlowFactory {

    static FlowStart defaultBuilder() {
        return DefaultEventFlowFactory.FlowStartImpl.of();
    }

    //    static FlowStart defaultBuilder(final Executor asyncExecutor) {
    //        return FlowStartImpl.of(asyncExecutor);
    //    }


    interface FlowStart {

        <PE> EventCondition<PE> beginFlow(final Class<PE> parentEventType);

    }

    interface EventCondition<PE> {

        <CE extends PE> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType);

        <CE extends PE> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                             final Predicate<? super CE> eventCondition);

        <CE extends PE, CI> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                 final CI contextualInput,
                                                                 final BiPredicate<? super CE, ? super CI> contextualInputCondition);

        <CE extends PE, CI> SpecificEventAction<PE, CE> forEvent(final Class<CE> childEventType,
                                                                 final CI contextualInput,
                                                                 final Predicate<? super CI> contextCondition);

        AnyEventAction<PE> forAnyEvent();

        AnyEventAction<PE> forAnyEvent(final Predicate<? super PE> eventCondition);

        <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                            final BiPredicate<? super PE, ? super CI> contextualInputCondition);

        <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput, final Predicate<? super CI> contextCondition);

    }

    interface SpecificEventAction<PE, CE> {

        <T> NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                    final Subscriber<? super T> subscriber);

        <T> NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                    final Subscriber<? super T> subscriber);

        <T> NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                    final Consumer<? super T> consumer,
                                                    final Consumer<? super Throwable> errorConsumer);

        <T> NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                    final Consumer<? super T> consumer,
                                                    final Consumer<? super Throwable> errorConsumer);

        NextSpecificEventAction<PE, CE> routeTo(final Subscriber<? super CE> subscriber);

        NextSpecificEventAction<PE, CE> routeTo(final Consumer<? super CE> consumer,
                                                final Consumer<? super Throwable> errorConsumer);


        NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends Subscriber<? super CE>> subscriberSelector);

        NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends Consumer<? super CE>> consumerSelector,
                                                final Function<? super CE, ? extends Consumer<? super Throwable>> errorConsumerSelector);

    }

    interface AnyEventAction<PE> extends SpecificEventAction<PE, PE> {

    }

    interface NextSpecificEventAction<PE, CE> {

        <T> NextSpecificEventAction<PE, CE> and(final ReactiveTransformer<? super CE, T> reactiveTransformer,
                                                final Subscriber<? super T> subscriber);

        <T> NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends ReactiveTransformer<? super CE, T>> reactiveTransformerSelector,
                                                final Subscriber<? super T> subscriber);

        NextSpecificEventAction<PE, CE> and(final Subscriber<? super CE> subscriber);

        NextSpecificEventAction<PE, CE> and(final Consumer<? super CE> consumer, final Consumer<? super Throwable> errorConsumer);

        NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends Subscriber<? super CE>> subscriberSelector);

        NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends Consumer<? super CE>> consumerSelector,
                                            final Function<? super CE, ? extends Consumer<? super Throwable>> errorConsumerSelector);


        <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType);

        <NCE extends PE> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                               final Predicate<? super NCE> nextEventCondition);

        <NCE extends PE, CI> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                   final CI contextualInput,
                                                                   final BiPredicate<? super NCE, ? super CI> contextualInputCondition);

        <NCE extends PE, CI> SpecificEventAction<PE, NCE> forEvent(final Class<NCE> nextChildEventType,
                                                                   final CI contextualInput,
                                                                   final Predicate<? super CI> contextCondition);

        AnyEventAction<PE> forAnyEvent();

        AnyEventAction<PE> forAnyEvent(final Predicate<? super PE> nextEventCondition);

        <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput,
                                            final BiPredicate<? super PE, ? super CI> contextualInputCondition);

        <CI> AnyEventAction<PE> forAnyEvent(final CI contextualInput, final Predicate<? super CI> contextCondition);

        EventFlow<PE> endFlow();

    }

    interface NextAnyEventAction<PE> extends NextSpecificEventAction<PE, PE> {

    }

}
