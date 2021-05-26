package funcify.typedef.javastatement;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaExpression {

    default boolean isLambda() {
        return false;
    }


}
