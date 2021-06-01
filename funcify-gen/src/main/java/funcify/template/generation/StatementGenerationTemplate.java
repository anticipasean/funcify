package funcify.template.generation;

import static java.util.Arrays.asList;

import funcify.template.session.TypeGenerationSession;
import funcify.typedef.javatype.JavaType;
import java.util.List;

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
                                   asList(expression));
    }

    default <TD, MD, CD, SD, ED> SD assignmentStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                        final JavaType assigneeType,
                                                        final String assigneeName,
                                                        final List<ED> expressions) {
        return session.assignmentStatement(assigneeType,
                                           assigneeName,
                                           expressions);
    }

    default <TD, MD, CD, SD, ED> SD returnStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final List<ED> expressions) {
        return session.returnStatement(expressions);
    }

    @SuppressWarnings("unchecked")
    default <TD, MD, CD, SD, ED> SD returnStatement(final TypeGenerationSession<SWT, TD, MD, CD, SD, ED> session,
                                                    final ED... expression) {
        return returnStatement(session,
                               asList(expression));
    }


}
