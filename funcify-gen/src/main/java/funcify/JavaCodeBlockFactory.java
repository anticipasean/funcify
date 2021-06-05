package funcify;

import funcify.tool.SyncList;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.javastatement.JavaStatement;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public interface JavaCodeBlockFactory extends JavaDefinitionFactory<JavaCodeBlock> {

    static JavaCodeBlockFactory getInstance() {
        return new JavaCodeBlockFactory() {
        };
    }

    @Override
    default JavaCodeBlock statements(final JavaCodeBlock definition,
                                     final SyncList<JavaStatement> statements) {

        return definition.withStatements(definition.getStatements()
                                                   .appendAll(statements));
    }

}
