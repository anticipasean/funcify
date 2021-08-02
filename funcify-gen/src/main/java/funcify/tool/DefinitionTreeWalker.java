package funcify.tool;

/**
 * @author smccarron
 * @created 2021-08-01
 */
public interface DefinitionTreeWalker {


    static interface StartStep {


    }

    static interface TemplateStep {

        <T> SessionStep<T> template(final T template);

    }

    static interface SessionStep<T> {

        <S> DefinitionStep<T, S> session(final S session);

    }

    static interface DefinitionStep<T, S> {

        <D> ChildDefinition1Step<T, S, D> definition(final D definition);

    }

    static interface ChildDefinition1Step<T, S, D> {

        <CD1> ChildDefinition2Step<T, S, D, CD1> firstChildDefinition(final CD1 childDef1);

    }

    static interface ChildDefinition2Step<T, S, D, CD1> {


    }

    static interface Input1Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1);
    }

    static interface Input2Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1,
                                                                                       final I2 input2);
    }

    static interface Input3Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1,
                                                                                       final I2 input2,
                                                                                       final I3 input3);
    }

    static interface Input4Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1,
                                                                                       final I2 input2,
                                                                                       final I3 input3,
                                                                                       final I4 input4);
    }

    static interface Input5Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1,
                                                                                       final I2 input2,
                                                                                       final I3 input3,
                                                                                       final I4 input4,
                                                                                       final I5 input5);
    }

    static interface Input6Step<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> input(final I1 input1,
                                                                                       final I2 input2,
                                                                                       final I3 input3,
                                                                                       final I4 input4,
                                                                                       final I5 input5,
                                                                                       final I6 input6);
    }

    static interface ApplyStep<T, S, D, CD1, CD2, CD3, CD4, CD5, CD6, I1, I2, I3, I4, I5, I6> {

        D apply();

    }

}
