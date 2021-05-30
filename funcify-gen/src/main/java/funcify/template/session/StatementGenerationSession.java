package funcify.template.session;

import static java.util.Arrays.asList;

import funcify.typedef.javatype.JavaType;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationSession<SWT, TD, MD, CD, SD, ED> extends
                                                                     ExpressionGenerationSession<SWT, TD, MD, CD, SD, ED> {

    @SuppressWarnings("unchecked")
    default SD assignmentStatement(final JavaType assigneeType,
                                   final String assigneeName,
                                   final ED... expression) {
        return assignmentStatement(assigneeType,
                                   assigneeName,
                                   asList(expression));
    }

    SD assignmentStatement(final JavaType assigneeType,
                           final String assigneeName,
                           final List<ED> expressions);

    @SuppressWarnings("unchecked")
    default SD returnStatement(final ED... expression) {
        return returnStatement(asList(expression));
    }

    SD returnStatement(final List<ED> expression);

}
