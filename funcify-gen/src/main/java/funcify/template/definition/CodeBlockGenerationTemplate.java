package funcify.template.definition;

import static java.util.Arrays.asList;

import funcify.template.session.TypeGenerationSession;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface CodeBlockGenerationTemplate<SWT> extends StatementGenerationTemplate<SWT> {

    <TD, MD, CD, SD, ED> CD emptyCodeBlockDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session);


    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> CD statement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final CD codeBlockDef,
                                              final SD... statement) {
        return statements(session,
                          codeBlockDef,
                          asList(statement));
    }

    <TD, MD, CD, SD, ED> CD statements(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                       final CD codeBlockDef,
                                       final List<SD> statements);

}
