package funcify.typedef.javaexpr;

import funcify.Definition;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaExpression extends Definition<JavaExpression> {

    default boolean isLambda() {
        return false;
    }


}
