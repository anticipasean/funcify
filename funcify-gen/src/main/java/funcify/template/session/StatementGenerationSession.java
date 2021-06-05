package funcify.template.session;

import funcify.tool.SyncList;
import funcify.typedef.javatype.JavaType;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationSession<SWT, TD, MD, CD, SD, ED> extends
                                                                     ExpressionGenerationSession<SWT, TD, MD, CD, SD, ED> {

    SyncList<SD> getStatementsForCodeBlock(final CD codeBlockDef);

    default Optional<SD> getFirstStatementForCodeBlock(final CD codeBlockDef) {
        return getStatementsForCodeBlock(codeBlockDef).stream()
                                                      .findFirst();
    }

    default Optional<SD> getLastStatementForCodeBlock(final CD codeBlockDef) {
        final SyncList<SD> statementsForCodeBlock = getStatementsForCodeBlock(codeBlockDef);
        return statementsForCodeBlock.size() > 0 ? statementsForCodeBlock.get(statementsForCodeBlock.size() - 1)
            : Optional.empty();
    }

    SD assignmentStatement(final JavaType assigneeType,
                           final String assigneeName,
                           final SyncList<ED> expressions);


    SD returnStatement(final SyncList<ED> expression);

}
