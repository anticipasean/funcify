package funcify.template.session;

import static java.util.Arrays.asList;

import funcify.typedef.javatype.JavaType;
import java.util.List;
import java.util.Optional;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface StatementGenerationSession<SWT, TD, MD, CD, SD, ED> extends
                                                                     ExpressionGenerationSession<SWT, TD, MD, CD, SD, ED> {

    List<SD> getStatementsForCodeBlock(final CD codeBlockDef);

    default Optional<SD> getFirstStatementForCodeBlock(final CD codeBlockDef) {
        return getStatementsForCodeBlock(codeBlockDef).stream()
                                                      .findFirst();
    }

    default Optional<SD> getLastStatementForCodeBlock(final CD codeBlockDef) {
        final List<SD> statementsForCodeBlock = getStatementsForCodeBlock(codeBlockDef);
        return statementsForCodeBlock.size() > 0 ? Optional.ofNullable(statementsForCodeBlock.get(
            statementsForCodeBlock.size() - 1)) : Optional.empty();
    }

    SD assignmentStatement(final JavaType assigneeType,
                           final String assigneeName,
                           final List<ED> expressions);


    SD returnStatement(final List<ED> expression);

}
