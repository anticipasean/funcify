package funcify.typedef.javastatement;

/**
 * @author smccarron
 * @created 2021-05-22
 */
public interface JavaStatement {

    default boolean isReturnStatement() {
        return false;
    }

    default boolean isAssignment() {
        return false;
    }

}
