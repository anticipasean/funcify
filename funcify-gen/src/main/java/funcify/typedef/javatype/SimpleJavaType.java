package funcify.typedef.javatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.JavaPackage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Getter
@Builder
@ToString
public class SimpleJavaType implements JavaType {

    @JsonProperty("package")
    private JavaPackage javaPackage;

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameterized")
    private final boolean parameterized = false;

}
