package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface CodeBlockGenerationTemplate<SWT> extends StatementGenerationTemplate<SWT> {

    default <TD, MD, CD, SD, ED> CD emptyCodeBlockDefinition(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session) {
        return session.emptyCodeBlockDefinition();
    }

    @SuppressWarnings({"unchecked", "varargs"})
    default <TD, MD, CD, SD, ED> CD statement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                              final CD codeBlockDef,
                                              final SD... statement) {
        return statements(session,
                          codeBlockDef,
                          SyncList.of(statement));
    }

    default <TD, MD, CD, SD, ED> CD statements(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                               final CD codeBlockDef,
                                               final SyncList<SD> statements) {
        return session.statements(codeBlockDef,
                                  statements);
    }

}
