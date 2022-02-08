package funcify.reactor.router;

import funcify.reactor.router.DefaultReactiveEventFlowFactory.FlowStartImpl;

import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author smccarron
 * @created 2/6/22
 */
public interface ReactiveEventFlowFactory {

    static FlowStart defaultBuilder() {
        return FlowStartImpl.of();
    }


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

        NextSpecificEventAction<PE, CE> routeTo(final ReactiveTransformer<? super CE, ? extends PE> reactiveTransformer);

        NextSpecificEventAction<PE, CE> routeTo(final Function<? super CE, ? extends ReactiveTransformer<? super CE, ? extends PE>> reactiveTransformerSelector);

    }

    interface AnyEventAction<PE> extends SpecificEventAction<PE, PE> {

    }

    interface NextSpecificEventAction<PE, CE> {

        NextSpecificEventAction<PE, CE> and(final ReactiveTransformer<? super CE, ? extends PE> reactiveTransformer);

        NextSpecificEventAction<PE, CE> and(final Function<? super CE, ? extends ReactiveTransformer<? super CE, ? extends PE>> reactiveTransformerSelector);

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

        ReactiveEventFlow<PE> endFlow();

    }

    interface NextAnyEventAction<PE> extends NextSpecificEventAction<PE, PE> {

    }

}

