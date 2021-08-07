package funcify.tool;

import funcify.tool.DefinitionTreeWalkerImpl.TemplateStepImpl;

/**
 * @author smccarron
 * @created 2021-08-01
 */
public interface DefinitionTreeWalker {

    static TemplateStep builder() {
        return TemplateStepImpl.of();
    }

    static interface TemplateStep {

        <T> SessionStep<T> template(final T template);

    }

    static interface SessionStep<T> {

        <S> DefinitionStep<T, S> session(final S session);

    }

    static interface DefinitionStep<T, S> {

        <D> ChildDefinition1CreateStep<T, S, D> definition(final D definition);

        <D, CD1, CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createDef(final Fn3<? super T, ? super S, ? super I1, ? extends D> defCreator);

        <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createDef(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends D> defCreator);

        <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createDef(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends D> defCreator);

        <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createDef(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends D> defCreator);

        <D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createDef(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends D> defCreator);

    }

    /**
     * Scala REPL Expression
     * <pre>
     *    List.range(1, 8).map(i => s"""
     *
     *                static interface ChildDefinition${i}UpdateStep<T, S, D${List.range(1, i).map(idx => s"CD${idx}").mkString(", ", ", ", "")}> {
     *
     *                   ${if(i == 2) s"ChildDefinition${i}CreateStep<T, S, D${if(i == 1) "" else List.range(1, i).map(idx => s"CD${idx}").mkString(", ", ", ", "")}> updateChildDef${i - 2}WithChildDef${i - 1}(final Fn4<? super T, ? super S, ? super D, ? super CD${i - 1}, ? extends D> definitionUpdater);" else if(i >= 3) s"ChildDefinition${i}CreateStep<T, S, D${if(i == 1) "" else List.range(1, i).map(idx => s"CD${idx}").mkString(", ", ", ", "")}> childDef${i - 2}Updater(final Fn4<? super T, ? super S, ? super CD${i - 2}, ? super CD${i - 1}, ? extends CD${i - 2}> childDef${i - 2}Updater);" else ""}
     *
     *
     *                }
     *
     *                static interface ChildDefinition${i}CreateStep<T, S, D${if(i == 1) "" else List.range(1, i).map(idx => s"CD${idx}").mkString(", ", ", ", "")}> {
     *
     *                    ${if(i < 7) s"${List.range(i, 7).map(idx => s"CD${idx}").mkString("<", ", ", "")}${List.range(1, 7).map(idx => s"I${idx}").mkString(", ", ", ", ">")} ApplyStep<T, S, D, ${List.range(1, 7).map(idx => s"CD${idx}").mkString("", ", ", "")}${List.range(1,6).map(idx => s"I${idx}").mkString(", ", ", ", "")}> childDef${i}(final CD${i} childDef${i});" else ""}
     *
     *                    ${if(i < 7) s"<CD${i}> ChildDefinition${i + 1}UpdateStep<T, S, D, ${List.range(1, i+1).map(idx => s"CD${idx}").mkString("", ", ", "")}> createDefaultChildDef${i}(final Fn2<? super T, ? super S, ? extends CD${i}> childDef${i}Creator);" else ""}
     *
     *                    ${if(i > 1) List.range(2, 7).map(inputArity => s"${if(i < 7) "<" else ""}${List.range(i, 7).map(idx => s"CD${idx}").mkString(", ")}${if(i < 7) ", " else "<"}${List.range(1, inputArity).map(idx => s"I${idx}").mkString(", ")}> Input${inputArity - 1}Step<T, S, D, ${List.range(1, 7).map(idx => s"CD${idx}").mkString(", ")}, ${List.range(1, inputArity).map(idx => s"I${idx}").mkString(", ")}> updateChildDef${i - 1}(final Fn${3 + inputArity - 1}<? super T, ? super S, ? super CD${i - 1},${List.range(1, inputArity).map(idx => s"? super I${idx}").mkString(" ", ", ", "")}, ? extends CD${i - 1}> childDef${i - 1}UpdateInput);").mkString("", "\n\n            ", "\n\n") else ""}
     *
     *                    ${if(i > 1) List.range(2, 7).map(inputArity => s"${if(i < 7) "<" else ""}${List.range(i, 7).map(idx => s"CD${idx}").mkString(", ")}${if(i < 7) ", " else "<"}${List.range(1, inputArity).map(idx => s"I${idx}").mkString(", ")}> Input${inputArity - 1}Step<T, S, D, ${List.range(1, 7).map(idx => s"CD${idx}").mkString(", ")}, ${List.range(1, inputArity).map(idx => s"I${idx}").mkString(", ")}> createChildDef${i - 1}(final Fn${2 + inputArity - 1}<? super T, ? super S,${List.range(1, inputArity).map(idx => s"? super I${idx}").mkString(" ", ", ", "")}, ? extends CD${i - 1}> childDef${i - 1}Input);").mkString("", "\n\n            ", "\n\n") else ""}
     *
     *                }
     *
     *
     *           """).foreach(println)
     * </pre>
     *
     * @param <T>
     * @param <S>
     * @param <D>
     */

    static interface ChildDefinition1CreateStep<T, S, D> {

        <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef1(final CD1 childDef1);

        <CD1> ChildDefinition2UpdateStep<T, S, D, CD1> createDefaultChildDef1(final Fn2<? super T, ? super S, ? extends CD1> childDef1Creator);


        <CD1, CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateDef(final Fn4<? super T, ? super S, ? super D, ? super I1, ? extends D> defUpdate);

        <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateDef(final Fn5<? super T, ? super S, ? super D, ? super I1, ? super I2, ? extends D> defUpdate);

        <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateDef(final Fn6<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? extends D> defUpdate);

        <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateDef(final Fn7<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? super I4, ? extends D> defUpdate);

        <CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateDef(final Fn8<? super T, ? super S, ? super D, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends D> defUpdate);

    }


    static interface ChildDefinition2UpdateStep<T, S, D, CD1> {

        ChildDefinition2CreateStep<T, S, D, CD1> updateDefWithChildDef1(final Fn4<? super T, ? super S, ? super D, ? super CD1, ? extends D> definitionUpdater);


    }

    static interface ChildDefinition2CreateStep<T, S, D, CD1> {

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef2(final CD2 childDef2);

        <CD2> ChildDefinition3UpdateStep<T, S, D, CD1, CD2> createDefaultChildDef2(final Fn2<? super T, ? super S, ? extends CD2> childDef2Creator);

        <CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef1(final Fn4<? super T, ? super S, ? super CD1, ? super I1, ? extends CD1> childDef1UpdateInput);

        <CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef1(final Fn5<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? extends CD1> childDef1UpdateInput);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef1(final Fn6<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? extends CD1> childDef1UpdateInput);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef1(final Fn7<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD1> childDef1UpdateInput);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef1(final Fn8<? super T, ? super S, ? super CD1, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD1> childDef1UpdateInput);


        <CD2, CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef1(final Fn3<? super T, ? super S, ? super I1, ? extends CD1> childDef1Input);

        <CD2, CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef1(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD1> childDef1Input);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef1(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD1> childDef1Input);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef1(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD1> childDef1Input);

        <CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef1(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD1> childDef1Input);


    }


    static interface ChildDefinition3UpdateStep<T, S, D, CD1, CD2> {

        ChildDefinition3CreateStep<T, S, D, CD1, CD2> childDef1Updater(final Fn4<? super T, ? super S, ? super CD1, ? super CD2, ? extends CD1> childDef1Updater);


    }

    static interface ChildDefinition3CreateStep<T, S, D, CD1, CD2> {

        <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef3(final CD3 childDef3);

        <CD3> ChildDefinition4UpdateStep<T, S, D, CD1, CD2, CD3> createDefaultChildDef3(final Fn2<? super T, ? super S, ? extends CD3> childDef3Creator);

        <CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef2(final Fn4<? super T, ? super S, ? super CD2, ? super I1, ? extends CD2> childDef2UpdateInput);

        <CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef2(final Fn5<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? extends CD2> childDef2UpdateInput);

        <CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef2(final Fn6<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? extends CD2> childDef2UpdateInput);

        <CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef2(final Fn7<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD2> childDef2UpdateInput);

        <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef2(final Fn8<? super T, ? super S, ? super CD2, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD2> childDef2UpdateInput);


        <CD3, CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef2(final Fn3<? super T, ? super S, ? super I1, ? extends CD2> childDef2Input);

        <CD3, CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef2(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD2> childDef2Input);

        <CD3, CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef2(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD2> childDef2Input);

        <CD3, CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef2(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD2> childDef2Input);

        <CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef2(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD2> childDef2Input);


    }


    static interface ChildDefinition4UpdateStep<T, S, D, CD1, CD2, CD3> {

        ChildDefinition4CreateStep<T, S, D, CD1, CD2, CD3> childDef2Updater(final Fn4<? super T, ? super S, ? super CD2, ? super CD3, ? extends CD2> childDef2Updater);


    }

    static interface ChildDefinition4CreateStep<T, S, D, CD1, CD2, CD3> {

        <CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef4(final CD4 childDef4);

        <CD4> ChildDefinition5UpdateStep<T, S, D, CD1, CD2, CD3, CD4> createDefaultChildDef4(final Fn2<? super T, ? super S, ? extends CD4> childDef4Creator);

        <CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef3(final Fn4<? super T, ? super S, ? super CD3, ? super I1, ? extends CD3> childDef3UpdateInput);

        <CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef3(final Fn5<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? extends CD3> childDef3UpdateInput);

        <CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef3(final Fn6<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? extends CD3> childDef3UpdateInput);

        <CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef3(final Fn7<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD3> childDef3UpdateInput);

        <CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef3(final Fn8<? super T, ? super S, ? super CD3, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD3> childDef3UpdateInput);


        <CD4, CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef3(final Fn3<? super T, ? super S, ? super I1, ? extends CD3> childDef3Input);

        <CD4, CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef3(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD3> childDef3Input);

        <CD4, CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef3(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD3> childDef3Input);

        <CD4, CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef3(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD3> childDef3Input);

        <CD4, CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef3(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD3> childDef3Input);


    }


    static interface ChildDefinition5UpdateStep<T, S, D, CD1, CD2, CD3, CD4> {

        ChildDefinition5CreateStep<T, S, D, CD1, CD2, CD3, CD4> childDef3Updater(final Fn4<? super T, ? super S, ? super CD3, ? super CD4, ? extends CD3> childDef3Updater);


    }

    static interface ChildDefinition5CreateStep<T, S, D, CD1, CD2, CD3, CD4> {

        <CD5, CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef5(final CD5 childDef5);

        <CD5> ChildDefinition6UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> createDefaultChildDef5(final Fn2<? super T, ? super S, ? extends CD5> childDef5Creator);

        <CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef4(final Fn4<? super T, ? super S, ? super CD4, ? super I1, ? extends CD4> childDef4UpdateInput);

        <CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef4(final Fn5<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? extends CD4> childDef4UpdateInput);

        <CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef4(final Fn6<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? extends CD4> childDef4UpdateInput);

        <CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef4(final Fn7<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD4> childDef4UpdateInput);

        <CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef4(final Fn8<? super T, ? super S, ? super CD4, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD4> childDef4UpdateInput);


        <CD5, CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef4(final Fn3<? super T, ? super S, ? super I1, ? extends CD4> childDef4Input);

        <CD5, CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef4(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD4> childDef4Input);

        <CD5, CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef4(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD4> childDef4Input);

        <CD5, CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef4(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD4> childDef4Input);

        <CD5, CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef4(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD4> childDef4Input);


    }


    static interface ChildDefinition6UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> {

        ChildDefinition6CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> childDef4Updater(final Fn4<? super T, ? super S, ? super CD4, ? super CD5, ? extends CD4> childDef4Updater);


    }

    static interface ChildDefinition6CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5> {

        <CD6, I1, I2, I3, I4, I5, I6> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> childDef6(final CD6 childDef6);

        <CD6> ChildDefinition7UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> createDefaultChildDef6(final Fn2<? super T, ? super S, ? extends CD6> childDef6Creator);

        <CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef5(final Fn4<? super T, ? super S, ? super CD5, ? super I1, ? extends CD5> childDef5UpdateInput);

        <CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef5(final Fn5<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? extends CD5> childDef5UpdateInput);

        <CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef5(final Fn6<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? extends CD5> childDef5UpdateInput);

        <CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef5(final Fn7<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD5> childDef5UpdateInput);

        <CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef5(final Fn8<? super T, ? super S, ? super CD5, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD5> childDef5UpdateInput);


        <CD6, I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef5(final Fn3<? super T, ? super S, ? super I1, ? extends CD5> childDef5Input);

        <CD6, I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef5(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD5> childDef5Input);

        <CD6, I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef5(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD5> childDef5Input);

        <CD6, I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef5(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD5> childDef5Input);

        <CD6, I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef5(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD5> childDef5Input);


    }


    static interface ChildDefinition7UpdateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> {

        ChildDefinition7CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> childDef5Updater(final Fn4<? super T, ? super S, ? super CD5, ? super CD6, ? extends CD5> childDef5Updater);


    }

    static interface ChildDefinition7CreateStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6> {


        <I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> updateChildDef6(final Fn4<? super T, ? super S, ? super CD6, ? super I1, ? extends CD6> childDef6UpdateInput);

        <I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> updateChildDef6(final Fn5<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? extends CD6> childDef6UpdateInput);

        <I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> updateChildDef6(final Fn6<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? extends CD6> childDef6UpdateInput);

        <I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> updateChildDef6(final Fn7<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD6> childDef6UpdateInput);

        <I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> updateChildDef6(final Fn8<? super T, ? super S, ? super CD6, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD6> childDef6UpdateInput);


        <I1> Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> createChildDef6(final Fn3<? super T, ? super S, ? super I1, ? extends CD6> childDef6Input);

        <I1, I2> Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> createChildDef6(final Fn4<? super T, ? super S, ? super I1, ? super I2, ? extends CD6> childDef6Input);

        <I1, I2, I3> Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> createChildDef6(final Fn5<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? extends CD6> childDef6Input);

        <I1, I2, I3, I4> Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> createChildDef6(final Fn6<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? extends CD6> childDef6Input);

        <I1, I2, I3, I4, I5> Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> createChildDef6(final Fn7<? super T, ? super S, ? super I1, ? super I2, ? super I3, ? super I4, ? super I5, ? extends CD6> childDef6Input);


    }


    /**
     * <pre>
     *     scala> List.range(1, 6).map(i => s"""
     *      |
     *      | static interface Input${i}Step<T, S, D, ${List.range(1, 7).map(idx => s"CD${idx}").mkString(", ")}, ${List.range(1, i + 1).map(idx => s"I${idx}").mkString(", ")}> {
     *      |
     *      |     ${if(i >= 5) "" else List.range(i + 1, 6).map(idx => s"I${idx}").mkString("<",", ", ">")} ApplyStep<T, S, D, ${List.range(1, 7).map(idx => s"CD${idx}").mkString(", ")}, ${List.range(1, 6).map(idx => s"I${idx}").mkString(", ")}> input(${List.range(1, i + 1).map(idx => s"final I${idx} input${idx}").mkString(", ")});
     *      |
     *      | }
     *      |
     *      | """).foreach(println)
     * </pre>
     */
    static interface Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1> {

        <I2, I3, I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1);

    }


    static interface Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2> {

        <I3, I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                                final I2 input2);

    }


    static interface Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3> {

        <I4, I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                            final I2 input2,
                                                                                            final I3 input3);

    }


    static interface Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4> {

        <I5> ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                        final I2 input2,
                                                                                        final I3 input3,
                                                                                        final I4 input4);

    }


    static interface Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> input(final I1 input1,
                                                                                   final I2 input2,
                                                                                   final I3 input3,
                                                                                   final I4 input4,
                                                                                   final I5 input5);

    }

    static interface ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5> {

        D getDefinition();

        S getSession();

        T getTemplate();

    }

    /**
     * Scala REPL Expression
     * <pre>
     *     scala> List.range(1, 11).map(i => s"""
     *      | @FunctionalInterface
     *      | static interface Fn${i}<${List.range(1, i + 1).map(idx => s"I${idx}").mkString(", ")}, O> {
     *      |
     *      |     O apply(${List.range(1, i + 1).map(idx => s"I${idx} input${idx}").mkString(",\n            ")});
     *      |
     *      | }
     *      | """).foreach(println)
     * </pre>
     *
     * @param <I1>
     * @param <O>
     */

    @FunctionalInterface
    static interface Fn1<I1, O> {

        O apply(I1 input1);

    }


    @FunctionalInterface
    static interface Fn2<I1, I2, O> {

        O apply(I1 input1,
                I2 input2);

    }


    @FunctionalInterface
    static interface Fn3<I1, I2, I3, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3);

    }


    @FunctionalInterface
    static interface Fn4<I1, I2, I3, I4, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4);

    }


    @FunctionalInterface
    static interface Fn5<I1, I2, I3, I4, I5, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5);

    }


    @FunctionalInterface
    static interface Fn6<I1, I2, I3, I4, I5, I6, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5,
                I6 input6);

    }


    @FunctionalInterface
    static interface Fn7<I1, I2, I3, I4, I5, I6, I7, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5,
                I6 input6,
                I7 input7);

    }


    @FunctionalInterface
    static interface Fn8<I1, I2, I3, I4, I5, I6, I7, I8, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5,
                I6 input6,
                I7 input7,
                I8 input8);

    }


    @FunctionalInterface
    static interface Fn9<I1, I2, I3, I4, I5, I6, I7, I8, I9, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5,
                I6 input6,
                I7 input7,
                I8 input8,
                I9 input9);

    }


    @FunctionalInterface
    static interface Fn10<I1, I2, I3, I4, I5, I6, I7, I8, I9, I10, O> {

        O apply(I1 input1,
                I2 input2,
                I3 input3,
                I4 input4,
                I5 input5,
                I6 input6,
                I7 input7,
                I8 input8,
                I9 input9,
                I10 input10);

    }


}
