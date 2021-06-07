package funcify.tool;

import static java.util.Objects.requireNonNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-06-06
 */
public interface TemplateExecutor<T, S, D> {

    static <T, S, D> TemplateExecutor<T, S, D> of(final T template,
                                                  final S session,
                                                  final D definition) {
        return DefaultTemplateExecutor.of(template,
                                          session,
                                          definition);
    }

    T getTemplate();

    S getSession();

    D getDefinition();

    default <I> TemplateExecutor<T, S, D> definitionUpdate(final Fn4<? super T, ? super S, ? super D, ? super I, ? extends D> definitionUpdaterFunc,
                                                           final I input) {
        return DefaultTemplateExecutor.of(getTemplate(),
                                          getSession(),
                                          requireNonNull(definitionUpdaterFunc,
                                                         () -> "definitionUpdaterFunc").apply(getTemplate(),
                                                                                              getSession(),
                                                                                              getDefinition(),
                                                                                              input));
    }

//    default <I> TemplateExecutor<T, S, D> definitionUpdate(final Fn4<? super T, ? super S, ? super D, ? super I, ? extends D> definitionUpdaterFunc,
//                                                           final Fn2<? super T, ? super S, ? extends I> inputFunction) {
//        return DefaultTemplateExecutor.of(getTemplate(),
//                                          getSession(),
//                                          requireNonNull(definitionUpdaterFunc,
//                                                         () -> "definitionUpdaterFunc").apply(getTemplate(),
//                                                                                              getSession(),
//                                                                                              getDefinition(),
//                                                                                              requireNonNull(inputFunction,
//                                                                                                             () -> "inputFunction").apply(getTemplate(),
//                                                                                                                                          getSession())));
//    }

    default <I1, I2> TemplateExecutor<T, S, D> definitionUpdate(final Fn5<? super T, ? super S, ? super D, ? super I1, ? super I2, ? extends D> definitionUpdaterFunc,
                                                                final I1 input1,
                                                                final I2 input2) {
        return DefaultTemplateExecutor.of(getTemplate(),
                                          getSession(),
                                          requireNonNull(definitionUpdaterFunc,
                                                         () -> "definitionUpdaterFunc").apply(getTemplate(),
                                                                                              getSession(),
                                                                                              getDefinition(),
                                                                                              input1,
                                                                                              input2));
    }

    default TemplateExecutor<T, S, D> sessionUpdate(final Fn3<? super T, ? super S, ? super D, ? extends S> sessionUpdater) {
        return DefaultTemplateExecutor.of(getTemplate(),
                                          requireNonNull(sessionUpdater).apply(getTemplate(),
                                                                               getSession(),
                                                                               getDefinition()),
                                          getDefinition());
    }


    @AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
    @Getter
    static class DefaultTemplateExecutor<T, S, D> implements TemplateExecutor<T, S, D> {

        private final T template;
        private final S session;
        private final D definition;

    }


    @FunctionalInterface
    static interface Fn2<A, B, C> {

        C apply(A a,
                B b);

    }

    @FunctionalInterface
    static interface Fn3<A, B, C, D> {

        D apply(A a,
                B b,
                C c);

    }

    @FunctionalInterface
    static interface Fn4<A, B, C, D, E> {

        E apply(A a,
                B b,
                C c,
                D d);

    }

    @FunctionalInterface
    static interface Fn5<A, B, C, D, E, F> {

        F apply(A a,
                B b,
                C c,
                D d,
                E e);

    }

}
