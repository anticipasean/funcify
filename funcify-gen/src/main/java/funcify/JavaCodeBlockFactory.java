package funcify;

import funcify.typedef.JavaCodeBlock;
import funcify.typedef.javastatement.JavaStatement;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public interface JavaCodeBlockFactory extends JavaDefinitionFactory<JavaCodeBlock> {

    public static JavaCodeBlockFactory getInstance() {
        return new JavaCodeBlockFactory() {
        };
    }

    @Override
    default JavaCodeBlock statements(final JavaCodeBlock definition,
                                     final List<JavaStatement> statements) {
        final boolean added = definition.getStatements()
                                        .addAll(statements);
        return definition.withStatements(definition.getStatements());
    }

}
