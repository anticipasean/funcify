package funcify.template.generation;

import funcify.template.session.TypeGenerationSession;
import funcify.tool.SyncList;
import funcify.typedef.javatype.JavaType;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationTemplate<SWT> extends ExpressionGenerationTemplate<SWT> {

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> SD assignmentStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                        final JavaType assigneeType,
                                                        final String assigneeName,
                                                        final ED... expression) {
        return assignmentStatement(session,
                                   assigneeType,
                                   assigneeName,
                                   SyncList.of(expression));
    }

    default <TD, MD, CD, SD, ED> SD assignmentStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                        final JavaType assigneeType,
                                                        final String assigneeName,
                                                        final SyncList<ED> expressions) {
        return session.assignmentStatement(assigneeType,
                                           assigneeName,
                                           expressions);
    }

    default <TD, MD, CD, SD, ED> SD returnStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final SyncList<ED> expressions) {
        return session.returnStatement(expressions);
    }

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> SD returnStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final ED... expression) {
        return returnStatement(session,
                               SyncList.of(expression));
    }


}
