package funcify.base.session;

import funcify.tool.container.SyncList;
import funcify.typedef.JavaAnnotation;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javatype.JavaType;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationSession<SWT> extends AnnotationGenerationSession<SWT> {

    SyncList<JavaStatement> getStatementsForCodeBlock(final JavaCodeBlock codeBlockDef);

    default Optional<JavaStatement> getFirstStatementForCodeBlock(final JavaCodeBlock codeBlockDef) {
        return getStatementsForCodeBlock(codeBlockDef).first();
    }

    default Optional<JavaStatement> getLastStatementForCodeBlock(final JavaCodeBlock codeBlockDef) {
        return getStatementsForCodeBlock(codeBlockDef).last();
    }

    JavaStatement assignmentStatement(final SyncList<JavaAnnotation> annotations,
                                      final JavaType assigneeType,
                                      final String assigneeName,
                                      final SyncList<JavaExpression> expressions);


    JavaStatement returnStatement(final SyncList<JavaExpression> expression);

}
