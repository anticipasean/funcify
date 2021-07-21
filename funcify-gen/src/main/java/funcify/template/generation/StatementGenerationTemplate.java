package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationTemplate<SWT> extends ExpressionGenerationTemplate<SWT> {


    default JavaStatement assignmentStatement(final TypeGenerationSession<SWT> session,
                                              final JavaType assigneeType,
                                              final String assigneeName,
                                              final JavaExpression expression) {
        return assignmentStatement(session,
                                   assigneeType,
                                   assigneeName,
                                   SyncList.of(expression));
    }

    default JavaStatement assignmentStatement(final TypeGenerationSession<SWT> session,
                                              final JavaType assigneeType,
                                              final String assigneeName,
                                              final SyncList<JavaExpression> expressions) {
        return session.assignmentStatement(assigneeType,
                                           assigneeName,
                                           expressions);
    }

    default JavaStatement returnStatement(final TypeGenerationSession<SWT> session,
                                          final SyncList<JavaExpression> expressions) {
        return session.returnStatement(expressions);
    }


    default JavaStatement returnStatement(final TypeGenerationSession<SWT> session,
                                          final JavaExpression... expression) {
        return returnStatement(session,
                               SyncList.of(expression));
    }


}
