package funcify.template.definition;

import static java.util.Arrays.asList;

import funcify.template.session.TypeGenerationSession;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationTemplate<SWT> extends ExpressionGenerationTemplate<SWT> {

    <TD, MD, CD, SD, ED> SD emptyStatementDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> SD expression(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final SD statementDef,
                                               final ED... expression) {
        return expressions(session,
                           statementDef,
                           asList(expression));
    }

    <TD, MD, CD, SD, ED> SD expressions(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                        final SD statementDef,
                                        final List<ED> expressions);

}
