package funcify;

import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import java.util.List;

/**
 * @author smccarron
 * @created 2021-05-27
 */
public interface JavaStatementFactory extends JavaDefinitionFactory<JavaStatement> {


    @Override
    default JavaStatement expressions(final JavaStatement definition,
                                      final List<JavaExpression> expressions) {
        return null;
    }
}
