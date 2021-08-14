package funcify.tool;

import funcify.tool.DefinitionTreeWalker.ApplyStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition1CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition2CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition2UpdateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition3CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition3UpdateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition4CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition4UpdateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition5CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition5UpdateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition6CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition6UpdateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition7CreateStep;
import funcify.tool.DefinitionTreeWalker.ChildDefinition7UpdateStep;
import funcify.tool.DefinitionTreeWalker.DefinitionStep;
import funcify.tool.DefinitionTreeWalker.Fn2;
import funcify.tool.DefinitionTreeWalker.Fn3;
import funcify.tool.DefinitionTreeWalker.Fn4;
import funcify.tool.DefinitionTreeWalker.Fn5;
import funcify.tool.DefinitionTreeWalker.Fn6;
import funcify.tool.DefinitionTreeWalker.Fn7;
import funcify.tool.DefinitionTreeWalker.Fn8;
import funcify.tool.DefinitionTreeWalker.Input1Step;
import funcify.tool.DefinitionTreeWalker.Input2Step;
import funcify.tool.DefinitionTreeWalker.Input3Step;
import funcify.tool.DefinitionTreeWalker.Input4Step;
import funcify.tool.DefinitionTreeWalker.Input5Step;
import funcify.tool.DefinitionTreeWalker.SessionStep;
import funcify.tool.DefinitionTreeWalker.TemplateStep;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author smccarron
 * @created 2021-08-08
 */
class DefinitionTreeWalkerImpl {


    @AllArgsConstructor(staticName = "of")
    static class TemplateStepImpl implements TemplateStep {

        @Override
        public <T> SessionStep<T> template(final T template) {
            return SessionStepImpl.of(template);
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class SessionStepImpl<T> implements SessionStep<T> {

        private final T template;

        @Override
        public <S> DefinitionStep<T, S> session(final S session) {
            return DefinitionStepImpl.of(template,
                                         session);
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class DefinitionStepImpl<T, S> implements DefinitionStep<T, S> {

        private final T template;
        private final S session;


        @Override
        public <D> ChildDefinition1CreateStep<T, S, D> definition(final D definition) {
            return ChildDefinition1CreateStepImpl.of(template,
                                                     session,
                                                     definition);
        }

        @Override
        public <D, CD1, CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createDef(final Fn3<? super T, ? super S, ? super I1, ? extends D> defCreator) {
            return Input1StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1>builder()
                                 .template(template)
                                 .session(session)
                                 .defCreator(defCreator)
                                 .build();
        }

        @Override
        public <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createDef(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends D> defCreator) {

            return Input2StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2>builder()
                                 .template(template)
                                 .session(session)
                                 .defCreator(defCreator)
                                 .build();
        }

        @Override
        public <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createDef(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends D> defCreator) {
            return Input3StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3>builder()
                                 .template(template)
                                 .session(session)
                                 .defCreator(defCreator)
                                 .build();
        }

        @Override
        public <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createDef(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends D> defCreator) {
            return Input4StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4>builder()
                                 .template(template)
                                 .session(session)
                                 .defCreator(defCreator)
                                 .build();

        }

        @Override
        public <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createDef(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends D> defCreator) {
            return Input5StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                 .template(template)
                                 .session(session)
                                 .defCreator(defCreator)
                                 .build();
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition1CreateStepImpl<T, S, D> implements ChildDefinition1CreateStep<T, S, D> {


        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef1(final CD1 childDef1) {
            return ApplyStepImpl.of(template,
                                    session,
                                    definition,
                                    childDef1,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null);
        }

        @Override
        public <CD1> ChildDefinition2UpdateStep<T, S, D, CD1> createDefaultChildDef1(final Fn2<? super T, ? super S, ? extends CD1> childDef1Creator) {
            return ChildDefinition2UpdateStepImpl.of(template,
                                                     session,
                                                     definition,
                                                     Objects.requireNonNull(childDef1Creator,
                                                                            () -> "childDef1Creator")
                                                            .apply(template,
                                                                   session));
        }

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateDef(final Fn4<? super T, ? super S, ? super D, ? super I1, ? extends D> defUpdate) {
            return Input1StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1>of(template,
                                                                                session,
                                                                                definition,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null);
        }

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateDef(final Fn5<? super T, ? super S, ? super D, ? super I1, ? super I2, ? extends D> defUpdate) {
            return Input2StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2>of(template,
                                                                                    session,
                                                                                    definition,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null);

        }

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateDef(final Fn6<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? extends D> defUpdate) {

            return Input3StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3>of(template,
                                                                                        session,
                                                                                        definition,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null);

        }

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateDef(final Fn7<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? super I4, ? extends D> defUpdate) {

            return Input4StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4>of(template,
                                                                                            session,
                                                                                            definition,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null);

        }

        @Override
        public <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateDef(final Fn8<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends D> defUpdate) {
            return Input5StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>of(template,
                                                                                                session,
                                                                                                definition,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null);

        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition2UpdateStepImpl<T, S, D, CD1> implements ChildDefinition2UpdateStep<T, S, D, CD1> {


        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;

        @Override
        public ChildDefinition2CreateStep<T, S, D, CD1> updateDefWithChildDef1(final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> definitionUpdater) {
            return ChildDefinition2CreateStepImpl.of(template,
                                                     session,
                                                     definition,
                                                     childDef1,
                                                     definitionUpdater);
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition2CreateStepImpl<T, S, D, CD1> implements ChildDefinition2CreateStep<T, S, D, CD1> {


        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> definitionUpdater;

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef2(final CD2 childDef2) {
            return null;
        }

        @Override
        public <CD2> ChildDefinition3UpdateStep<T, S, D, CD1, CD2> createDefaultChildDef2(final Fn2<? super T, ? super S, ? extends CD2> childDef2Creator) {
            return ChildDefinition3UpdateStepImpl.of(template,
                                                     session,
                                                     definition,
                                                     childDef1,
                                                     Objects.requireNonNull(childDef2Creator,
                                                                            () -> "childDef2Creator")
                                                            .apply(template,
                                                                   session),
                                                     definitionUpdater);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef1(final Fn4<? super T, ? super S, ? super CD1, ? super I1, ? extends CD1> childDef1UpdateInput) {

            return Input1StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1>of(template,
                                                                                session,
                                                                                definition,
                                                                                childDef1,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef1(final Fn5<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? extends CD1> childDef1UpdateInput) {
            return Input2StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2>of(template,
                                                                                    session,
                                                                                    definition,
                                                                                    childDef1,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef1(final Fn6<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? extends CD1> childDef1UpdateInput) {

            return Input3StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3>of(template,
                                                                                        session,
                                                                                        definition,
                                                                                        childDef1,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null);

        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef1(final Fn7<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD1> childDef1UpdateInput) {

            return Input4StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4>of(template,
                                                                                            session,
                                                                                            definition,
                                                                                            childDef1,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null);

        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef1(final Fn8<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD1> childDef1UpdateInput) {

            return Input5StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>of(template,
                                                                                                session,
                                                                                                definition,
                                                                                                childDef1,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef1(final Fn3<? super T, ? super S, ? super I1, ? extends CD1> childDef1Input) {
            return Input1StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1>of(template,
                                                                                session,
                                                                                definition,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                childDef1Input,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null);

        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef1(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD1> childDef1Input) {
            return Input2StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2>of(template,
                                                                                    session,
                                                                                    definition,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    childDef1Input,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef1(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD1> childDef1Input) {

            return Input3StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3>of(template,
                                                                                        session,
                                                                                        definition,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        childDef1Input,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef1(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD1> childDef1Input) {
            return Input4StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4>of(template,
                                                                                            session,
                                                                                            definition,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            childDef1Input,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null);
        }

        @Override
        public <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef1(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD1> childDef1Input) {
            return Input5StepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>of(template,
                                                                                                session,
                                                                                                definition,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                childDef1Input,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null,
                                                                                                null);
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition3UpdateStepImpl<T, S, D, CD1, CD2> implements ChildDefinition3UpdateStep<T, S, D, CD1, CD2> {


        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> definitionUpdater;

        @Override
        public ChildDefinition3CreateStep<T, S, D, CD1, CD2> childDef1Updater(final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition3CreateStepImpl<T, S, D, CD1, CD2> implements ChildDefinition3CreateStep<T, S, D, CD1, CD2> {


        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef3(final CD3 childDef3) {
            return null;
        }

        @Override
        public <CD3> ChildDefinition4UpdateStep<T, S, D, CD1, CD2, CD3> createDefaultChildDef3(final Fn2<? super T, ? super S, ? extends CD3> childDef3Creator) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef2(final Fn4<? super T, ? super S, ? super CD2, ? super I1, ? extends CD2> childDef2UpdateInput) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef2(final Fn5<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? extends CD2> childDef2UpdateInput) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef2(final Fn6<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? extends CD2> childDef2UpdateInput) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef2(final Fn7<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD2> childDef2UpdateInput) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef2(final Fn8<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD2> childDef2UpdateInput) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef2(final Fn3<? super T, ? super S, ? super I1, ? extends CD2> childDef2Input) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef2(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD2> childDef2Input) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef2(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD2> childDef2Input) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef2(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD2> childDef2Input) {
            return null;
        }

        @Override
        public <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef2(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD2> childDef2Input) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition4UpdateStepImpl<T, S, D, CD1, CD2, CD3> implements
                                                                        ChildDefinition4UpdateStep<T, S, D, CD1, CD2, CD3> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public ChildDefinition4CreateStep<T, S, D, CD1, CD2, CD3> childDef2Updater(final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition4CreateStepImpl<T, S, D, CD1, CD2, CD3> implements
                                                                        ChildDefinition4CreateStep<T, S, D, CD1, CD2, CD3> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef4(final CD4 childDef4) {
            return null;
        }

        @Override
        public <CD4> ChildDefinition5UpdateStep<T, S, D, CD1, CD2, CD3, CD4> createDefaultChildDef4(final Fn2<? super T, ? super S, ? extends CD4> childDef4Creator) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef3(final Fn4<? super T, ? super S, ? super CD3, ? super I1, ? extends CD3> childDef3UpdateInput) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef3(final Fn5<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? extends CD3> childDef3UpdateInput) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef3(final Fn6<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? extends CD3> childDef3UpdateInput) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef3(final Fn7<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD3> childDef3UpdateInput) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef3(final Fn8<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD3> childDef3UpdateInput) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef3(final Fn3<? super T, ? super S, ? super I1, ? extends CD3> childDef3Input) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef3(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD3> childDef3Input) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef3(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD3> childDef3Input) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef3(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD3> childDef3Input) {
            return null;
        }

        @Override
        public <CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef3(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD3> childDef3Input) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition5UpdateStepImpl<T, S, D, CD1, CD2, CD3, CD4> implements
                                                                             ChildDefinition5UpdateStep<T, S, D, CD1, CD2, CD3, CD4> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public ChildDefinition5CreateStep<T, S, D, CD1, CD2, CD3, CD4> childDef3Updater(final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition5CreateStepImpl<T, S, D, CD1, CD2, CD3, CD4> implements
                                                                             ChildDefinition5CreateStep<T, S, D, CD1, CD2, CD3, CD4> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef5(final CD5 childDef5) {
            return null;
        }

        @Override
        public <CD5> ChildDefinition6UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> createDefaultChildDef5(final Fn2<? super T, ? super S, ? extends CD5> childDef5Creator) {
            return null;
        }

        @Override
        public <CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef4(final Fn4<? super T, ? super S, ? super CD4, ? super I1, ? extends CD4> childDef4UpdateInput) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef4(final Fn5<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? extends CD4> childDef4UpdateInput) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef4(final Fn6<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? extends CD4> childDef4UpdateInput) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef4(final Fn7<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD4> childDef4UpdateInput) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef4(final Fn8<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD4> childDef4UpdateInput) {
            return null;
        }

        @Override
        public <CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef4(final Fn3<? super T, ? super S, ? super I1, ? extends CD4> childDef4Input) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef4(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD4> childDef4Input) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef4(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD4> childDef4Input) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef4(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD4> childDef4Input) {
            return null;
        }

        @Override
        public <CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef4(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD4> childDef4Input) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition6UpdateStepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5> implements
                                                                                  ChildDefinition6UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public ChildDefinition6CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> childDef4Updater(final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition6CreateStepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5> implements
                                                                                  ChildDefinition6CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef6(final CD6 childDef6) {
            return null;
        }

        @Override
        public <CD6> ChildDefinition7UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> createDefaultChildDef6(final Fn2<? super T, ? super S, ? extends CD6> childDef6Creator) {
            return null;
        }

        @Override
        public <CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef5(final Fn4<? super T, ? super S, ? super CD5, ? super I1, ? extends CD5> childDef5UpdateInput) {
            return null;
        }

        @Override
        public <CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef5(final Fn5<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? extends CD5> childDef5UpdateInput) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef5(final Fn6<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? extends CD5> childDef5UpdateInput) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef5(final Fn7<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD5> childDef5UpdateInput) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef5(final Fn8<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD5> childDef5UpdateInput) {
            return null;
        }

        @Override
        public <CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef5(final Fn3<? super T, ? super S, ? super I1, ? extends CD5> childDef5Input) {
            return null;
        }

        @Override
        public <CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef5(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD5> childDef5Input) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef5(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD5> childDef5Input) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef5(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD5> childDef5Input) {
            return null;
        }

        @Override
        public <CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef5(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD5> childDef5Input) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition7UpdateStepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> implements
                                                                                       ChildDefinition7UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public ChildDefinition7CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> childDef5Updater(final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    static class ChildDefinition7CreateStepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> implements
                                                                                       ChildDefinition7CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> {

        private final T template;
        private final S session;
        private final D definition;

        @Override
        public <I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef6(final Fn4<? super T, ? super S, ? super CD6, ? super I1, ? extends CD6> childDef6UpdateInput) {
            return null;
        }

        @Override
        public <I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef6(final Fn5<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? extends CD6> childDef6UpdateInput) {
            return null;
        }

        @Override
        public <I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef6(final Fn6<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? extends CD6> childDef6UpdateInput) {
            return null;
        }

        @Override
        public <I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef6(final Fn7<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD6> childDef6UpdateInput) {
            return null;
        }

        @Override
        public <I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef6(final Fn8<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD6> childDef6UpdateInput) {
            return null;
        }

        @Override
        public <I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef6(final Fn3<? super T, ? super S, ? super I1, ? extends CD6> childDef6Input) {
            return null;
        }

        @Override
        public <I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef6(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD6> childDef6Input) {
            return null;
        }

        @Override
        public <I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef6(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD6> childDef6Input) {
            return null;
        }

        @Override
        public <I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef6(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD6> childDef6Input) {
            return null;
        }

        @Override
        public <I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef6(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD6> childDef6Input) {
            return null;
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class Input1StepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> implements
                                                                           Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> defUpdater;
        private final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater;
        private final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater;
        private final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater;
        private final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater;
        private final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater;
        private final Fn3<? super T, ? super S, ? super I1, ? extends D> defCreator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD1> childDef1Creator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD2> childDef2Creator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD3> childDef3Creator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD4> childDef4Creator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD5> childDef5Creator;
        private final Fn3<? super T, ? super S, ? super I1, ? extends CD6> childDef6Creator;

        /**
         * <pre>
         *     var arity: Int = 3
         * List.range(0, 7).map(i => s"""
         * else if(${if(i == 0) "definition" else s"childDef${i}"} == null && ${if(i == 0) "defCreator" else s"childDef${i}Creator"} != null && ${List.range(1, arity + 1).map(idx => s"input${idx} != null").mkString(" && ")}){
         *             return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
         *             .template(template)
         *             .session(session)
         *             ${if(i == 0) s".definition(defCreator.apply(template, session, ${List.range(1, arity + 1).map(idx => s"input${idx}").mkString(", ")}))" else ".definition(definition)"}
         *             ${if(i == 0) "" else s".childDef${i}(childDef${i}Creator.apply(template, session, ${List.range(1, arity + 1).map(idx => s"input${idx}").mkString(", ")}))"}
         *             .${List.range(1, 7).filterNot(_ == i).map(idx => s"childDef${idx}(childDef${idx})").mkString(".")}
         *             .build();
         * } else if(${if(i == 0) "definition" else s"childDef${i}"} != null && ${if(i == 0) "def" else s"childDef${i}"}Updater != null){
         *             return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
         *             .template(template)
         *             .session(session)
         *             ${if(i == 0) s".definition(defUpdater.apply(template, session, definition, ${List.range(1, arity + 1).map(idx => s"input${idx}").mkString(", ")}))" else ".definition(definition)"}
         *             ${if(i == 0) "" else s".childDef${i}(childDef${i}Updater.apply(template, session, childDef${i}, ${List.range(1, arity + 1).map(idx => s"input${idx}").mkString(", ")}))"}
         *             .${List.range(1, 7).filterNot(_ == i).map(idx => s"childDef${idx}(childDef${idx})").mkString(".")}
         *             .build();
         * }
         * """).foreach(println)
         * </pre>
         */

        @Override
        public <I2, I3, I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1) {
            if (definition == null && defCreator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defCreator.apply(template,
                                                                 session,
                                                                 input1))
                                    .build();
            } else if (definition != null && defUpdater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defUpdater.apply(template,
                                                                 session,
                                                                 definition,
                                                                 childDef1))
                                    .build();
            } else if (childDef1 == null && childDef1Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 != null && childDef1Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Updater.apply(template,
                                                                      session,
                                                                      childDef1,
                                                                      childDef2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 == null && childDef2Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 != null && childDef2Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Updater.apply(template,
                                                                      session,
                                                                      childDef2,
                                                                      childDef3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 == null && childDef3Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 != null && childDef3Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Updater.apply(template,
                                                                      session,
                                                                      childDef3,
                                                                      childDef4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 == null && childDef4Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 != null && childDef4Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Updater.apply(template,
                                                                      session,
                                                                      childDef4,
                                                                      childDef5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 == null && childDef5Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 != null && childDef5Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Updater.apply(template,
                                                                      session,
                                                                      childDef5,
                                                                      childDef6))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef6 == null && childDef6Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef6(childDef6Creator.apply(template,
                                                                      session,
                                                                      input1))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .build();
            } else {
                throw new IllegalArgumentException("none of the definition or child definition input functions are non-null");
            }

        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class Input2StepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> implements
                                                                               Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> defUpdater;
        private final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater;
        private final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater;
        private final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater;
        private final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater;
        private final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends D> defCreator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD1> childDef1Creator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD2> childDef2Creator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD3> childDef3Creator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD4> childDef4Creator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD5> childDef5Creator;
        private final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD6> childDef6Creator;

        @Override
        public <I3, I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                                       final I2 input2) {
            if (definition == null && defCreator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defCreator.apply(template,
                                                                 session,
                                                                 input1,
                                                                 input2))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (definition != null && defUpdater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defUpdater.apply(template,
                                                                 session,
                                                                 definition,
                                                                 childDef1))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 == null && childDef1Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 != null && childDef1Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Updater.apply(template,
                                                                      session,
                                                                      childDef1,
                                                                      childDef2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 == null && childDef2Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 != null && childDef2Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Updater.apply(template,
                                                                      session,
                                                                      childDef2,
                                                                      childDef3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 == null && childDef3Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 != null && childDef3Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Updater.apply(template,
                                                                      session,
                                                                      childDef3,
                                                                      childDef4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 == null && childDef4Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 != null && childDef4Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Updater.apply(template,
                                                                      session,
                                                                      childDef4,
                                                                      childDef5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 == null && childDef5Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 != null && childDef5Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Updater.apply(template,
                                                                      session,
                                                                      childDef5,
                                                                      childDef6))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef6 == null && childDef6Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef6(childDef6Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .build();
            } else {

                throw new IllegalArgumentException("none of the definition or child definition input functions are non-null");
            }
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class Input3StepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> implements
                                                                                   Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> defUpdater;
        private final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater;
        private final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater;
        private final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater;
        private final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater;
        private final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends D> defCreator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD1> childDef1Creator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD2> childDef2Creator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD3> childDef3Creator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD4> childDef4Creator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD5> childDef5Creator;
        private final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD6> childDef6Creator;

        @Override
        public <I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                                   final I2 input2,
                                                                                                   final I3 input3) {
            if (definition == null && defCreator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defCreator.apply(template,
                                                                 session,
                                                                 input1,
                                                                 input2,
                                                                 input3))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (definition != null && defUpdater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defUpdater.apply(template,
                                                                 session,
                                                                 definition,
                                                                 childDef1))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 == null && childDef1Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 != null && childDef1Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Updater.apply(template,
                                                                      session,
                                                                      childDef1,
                                                                      childDef2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 == null && childDef2Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 != null && childDef2Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Updater.apply(template,
                                                                      session,
                                                                      childDef2,
                                                                      childDef3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 == null && childDef3Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 != null && childDef3Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Updater.apply(template,
                                                                      session,
                                                                      childDef3,
                                                                      childDef4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 == null && childDef4Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 != null && childDef4Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Updater.apply(template,
                                                                      session,
                                                                      childDef4,
                                                                      childDef5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 == null && childDef5Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 != null && childDef5Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Updater.apply(template,
                                                                      session,
                                                                      childDef5,
                                                                      childDef6))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef6 == null && childDef6Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef6(childDef6Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .build();
            } else {
                throw new IllegalArgumentException("none of the definition or child definition input functions are non-null");
            }
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class Input4StepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> implements
                                                                                       Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> defUpdater;
        private final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater;
        private final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater;
        private final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater;
        private final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater;
        private final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends D> defCreator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD1> childDef1Creator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD2> childDef2Creator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD3> childDef3Creator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD4> childDef4Creator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD5> childDef5Creator;
        private final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD6> childDef6Creator;

        @Override
        public <I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                               final I2 input2,
                                                                                               final I3 input3,
                                                                                               final I4 input4) {
            if (definition == null && defCreator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defCreator.apply(template,
                                                                 session,
                                                                 input1,
                                                                 input2,
                                                                 input3,
                                                                 input4))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (definition != null && defUpdater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defUpdater.apply(template,
                                                                 session,
                                                                 definition,
                                                                 childDef1))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 == null && childDef1Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 != null && childDef1Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Updater.apply(template,
                                                                      session,
                                                                      childDef1,
                                                                      childDef2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 == null && childDef2Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 != null && childDef2Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Updater.apply(template,
                                                                      session,
                                                                      childDef2,
                                                                      childDef3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 == null && childDef3Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 != null && childDef3Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Updater.apply(template,
                                                                      session,
                                                                      childDef3,
                                                                      childDef4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 == null && childDef4Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 != null && childDef4Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Updater.apply(template,
                                                                      session,
                                                                      childDef4,
                                                                      childDef5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 == null && childDef5Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 != null && childDef5Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Updater.apply(template,
                                                                      session,
                                                                      childDef5,
                                                                      childDef6))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef6 == null && childDef6Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef6(childDef6Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .build();
            } else {
                throw new IllegalArgumentException("none of the definition or child definition input functions are non-null");
            }
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class Input5StepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> implements
                                                                                           Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;
        private final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> defUpdater;
        private final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater;
        private final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater;
        private final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater;
        private final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater;
        private final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends D> defCreator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD1> childDef1Creator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD2> childDef2Creator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD3> childDef3Creator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD4> childDef4Creator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD5> childDef5Creator;
        private final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD6> childDef6Creator;

        @Override
        public ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                          final I2 input2,
                                                                                          final I3 input3,
                                                                                          final I4 input4,
                                                                                          final I5 input5) {
            if (definition == null && defCreator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defCreator.apply(template,
                                                                 session,
                                                                 input1,
                                                                 input2,
                                                                 input3,
                                                                 input4,
                                                                 input5))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (definition != null && defUpdater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(defUpdater.apply(template,
                                                                 session,
                                                                 definition,
                                                                 childDef1))

                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 == null && childDef1Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef1 != null && childDef1Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef1(childDef1Updater.apply(template,
                                                                      session,
                                                                      childDef1,
                                                                      childDef2))
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 == null && childDef2Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef2 != null && childDef2Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef2(childDef2Updater.apply(template,
                                                                      session,
                                                                      childDef2,
                                                                      childDef3))
                                    .childDef1(childDef1)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 == null && childDef3Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef3 != null && childDef3Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef3(childDef3Updater.apply(template,
                                                                      session,
                                                                      childDef3,
                                                                      childDef4))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 == null && childDef4Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef4 != null && childDef4Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef4(childDef4Updater.apply(template,
                                                                      session,
                                                                      childDef4,
                                                                      childDef5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef5(childDef5)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 == null && childDef5Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef5 != null && childDef5Updater != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef5(childDef5Updater.apply(template,
                                                                      session,
                                                                      childDef5,
                                                                      childDef6))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef6(childDef6)
                                    .build();
            } else if (childDef6 == null && childDef6Creator != null) {
                return ApplyStepImpl.<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5>builder()
                                    .template(template)
                                    .session(session)
                                    .definition(definition)
                                    .childDef6(childDef6Creator.apply(template,
                                                                      session,
                                                                      input1,
                                                                      input2,
                                                                      input3,
                                                                      input4,
                                                                      input5))
                                    .childDef1(childDef1)
                                    .childDef2(childDef2)
                                    .childDef3(childDef3)
                                    .childDef4(childDef4)
                                    .childDef5(childDef5)
                                    .build();
            } else {
                throw new IllegalArgumentException("none of the definition or child definition input functions are non-null");
            }
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    static class ApplyStepImpl<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> implements
                                                                                          ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> {

        private final T template;
        private final S session;
        private final D definition;
        private final CD1 childDef1;
        private final CD2 childDef2;
        private final CD3 childDef3;
        private final CD4 childDef4;
        private final CD5 childDef5;
        private final CD6 childDef6;


        @Override
        public D getDefinition() {
            return null;
        }

        @Override
        public S getSession() {
            return null;
        }

        @Override
        public T getTemplate() {
            return null;
        }
    }

}
