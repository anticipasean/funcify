package funcify.template.session;

import funcify.tool.SyncList;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface CodeBlockGenerationSession<SWT, TD, MD, CD, SD, ED> extends StatementGenerationSession<SWT, TD, MD, CD, SD, ED> {

    CD emptyCodeBlockDefinition();

    Optional<CD> getCodeBlockForMethodDefinition(final MD methodDef);

    CD statements(final CD codeBlockDef,
                  final SyncList<SD> statements);
}
