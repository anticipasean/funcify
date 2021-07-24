package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.javastatement.JavaStatement;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface CodeBlockGenerationSession<SWT> extends StatementGenerationSession<SWT> {

    JavaCodeBlock emptyCodeBlockDefinition();

    Optional<JavaCodeBlock> getCodeBlockForMethodDefinition(final JavaMethod methodDef);

    JavaCodeBlock statements(final JavaCodeBlock codeBlockDef,
                             final SyncList<JavaStatement> statements);
}
