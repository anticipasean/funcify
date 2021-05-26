package funcify.typedef.javatype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author smccarron
 * @created 2021-05-22
 */
@JsonFormat(shape = Shape.OBJECT)
public enum WildcardJavaTypeBound implements JavaType {

    INSTANCE;

    WildcardJavaTypeBound() {

    }

    public static WildcardJavaTypeBound getInstance() {
        return INSTANCE;
    }

    @JsonProperty("name")
    @Override
    public String getName() {
        return "?";
    }

    @JsonProperty("parameterized")
    @Override
    public boolean isParameterized() {
        return false;
    }

}
