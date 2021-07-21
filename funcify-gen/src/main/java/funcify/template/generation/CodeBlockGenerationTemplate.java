package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.javastatement.JavaStatement;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface CodeBlockGenerationTemplate<SWT> extends StatementGenerationTemplate<SWT> {

    default JavaCodeBlock emptyCodeBlockDefinition(final TypeGenerationSession<SWT> session) {
        return session.emptyCodeBlockDefinition();
    }

    default JavaCodeBlock statement(final TypeGenerationSession<SWT> session,
                                    final JavaCodeBlock codeBlockDef,
                                    final JavaStatement statement) {
        return statements(session,
                          codeBlockDef,
                          SyncList.of(statement));
    }

    default JavaCodeBlock statements(final TypeGenerationSession<SWT> session,
                                     final JavaCodeBlock codeBlockDef,
                                     final SyncList<JavaStatement> statements) {
        return session.statements(codeBlockDef,
                                  statements);
    }

}
