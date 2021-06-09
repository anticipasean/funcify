package funcify.tool;

import static java.util.Objects.requireNonNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-06-06
 */
public interface TypeGenerationExecutor<T, S, D> {

    static <T, S, D> TypeGenerationExecutor<T, S, D> of(final T template,
                                                        final S session,
                                                        final D definition) {
        return DefaultTypeGenerationExecutor.of(template,
                                                session,
                                                definition);
    }

    T getTemplate();

    S getSession();

    D getDefinition();


    default <I> TypeGenerationExecutor<T, S, D> updateDefinition(final Fn3<? super T, ? super S, ? super D, ? extends D> definitionUpdater) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdater,
                                                               () -> "definitionUpdater").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition()));
    }

    default <I> TypeGenerationExecutor<T, S, D> updateDefinition(final Fn4<? super T, ? super S, ? super D, ? super I, ? extends D> definitionUpdater,
                                                                 final I input) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdater,
                                                               () -> "definitionUpdater").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition(),
                                                                                                    input));
    }
    //    default <I> TemplateExecutor<T, S, D> definitionUpdate(final Fn4<? super T, ? super S, ? super D, ? super SyncList<I>, ? extends D> definitionUpdaterFunc,
    //                                                           final SyncList<I> inputs) {
    //        return DefaultTemplateExecutor.of(getTemplate(),
    //                                          getSession(),
    //                                          requireNonNull(definitionUpdaterFunc,
    //                                                         () -> "definitionUpdaterFunc").apply(getTemplate(),
    //                                                                                              getSession(),
    //                                                                                              getDefinition(),
    //                                                                                              requireNonNull(inputs,
    //                                                                                                             () -> "inputs")));
    //    }

    default <I1, I2> TypeGenerationExecutor<T, S, D> updateDefinition(final Fn5<? super T, ? super S, ? super D, ? super I1, ? super I2, ? extends D> definitionUpdater,
                                                                      final I1 input1,
                                                                      final I2 input2) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdater,
                                                               () -> "definitionUpdater").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition(),
                                                                                                    input1,
                                                                                                    input2));
    }

    default <I1, I2, I3> TypeGenerationExecutor<T, S, D> updateDefinition(final Fn6<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? extends D> definitionUpdater,
                                                                          final I1 input1,
                                                                          final I2 input2,
                                                                          final I3 input3) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdater,
                                                               () -> "definitionUpdater").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition(),
                                                                                                    input1,
                                                                                                    input2,
                                                                                                    input3));
    }

    default <CD, I> TypeGenerationExecutor<T, S, D> addChildDefinition(final Fn4<? super T, ? super S, ? super D, ? super CD, ? extends D> definitionUpdaterFunc,
                                                                       final Fn3<? super T, ? super S, ? super I, ? extends CD> childDefGenerator,
                                                                       final I input) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdaterFunc,
                                                               () -> "definitionUpdaterFunc").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition(),
                                                                                                    requireNonNull(childDefGenerator,
                                                                                                                   () -> "childDefGenerator").apply(getTemplate(),
                                                                                                                                                    getSession(),
                                                                                                                                                    input)));
    }

    default <CD1, CD2, I> TypeGenerationExecutor<T, S, D> addChildDefinition(final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> definitionUpdaterFunc,
                                                                             final Fn2<? super T, ? super S, ? extends CD1> emptyChildDefGenerator,
                                                                             final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDefUpdater,
                                                                             final Fn3<? super T, ? super S, ? super I, ? extends CD2> subChildDefGenerator,
                                                                             final I input) {
        final CD1 emptyChildDef = requireNonNull(emptyChildDefGenerator,
                                                 () -> "emptyChildDefGenerator").apply(getTemplate(),
                                                                                       getSession());
        final CD2 subChildDef = requireNonNull(subChildDefGenerator,
                                               () -> "subChildDefGenerator").apply(getTemplate(),
                                                                                   getSession(),
                                                                                   input);
        final CD1 updatedChildDef = requireNonNull(childDefUpdater,
                                                   () -> "childDefUpdater").apply(getTemplate(),
                                                                                  getSession(),
                                                                                  emptyChildDef,
                                                                                  subChildDef);
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                getSession(),
                                                requireNonNull(definitionUpdaterFunc,
                                                               () -> "definitionUpdaterFunc").apply(getTemplate(),
                                                                                                    getSession(),
                                                                                                    getDefinition(),
                                                                                                    updatedChildDef));
    }

    default TypeGenerationExecutor<T, S, D> updateSession(final Fn3<? super T, ? super S, ? super D, ? extends S> sessionUpdater) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                requireNonNull(sessionUpdater).apply(getTemplate(),
                                                                                     getSession(),
                                                                                     getDefinition()),
                                                getDefinition());
    }

    default <I> TypeGenerationExecutor<T, S, D> updateSession(final Fn4<? super T, ? super S, ? super D, ? super I, ? extends S> sessionUpdater,
                                                              final I input) {
        return DefaultTypeGenerationExecutor.of(getTemplate(),
                                                requireNonNull(sessionUpdater).apply(getTemplate(),
                                                                                     getSession(),
                                                                                     getDefinition(),
                                                                                     input),
                                                getDefinition());
    }


    @AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
    @Getter
    static class DefaultTypeGenerationExecutor<T, S, D> implements TypeGenerationExecutor<T, S, D> {

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

    @FunctionalInterface
    static interface Fn6<A, B, C, D, E, F, G> {

        G apply(A a,
                B b,
                C c,
                D d,
                E e,
                F f);

    }

}
