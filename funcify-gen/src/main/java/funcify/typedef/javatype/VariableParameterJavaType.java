package funcify.typedef.javatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.JavaPackage;
import java.util.Collections;
import funcify.tool.SyncList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-05-22
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Getter
@Builder
@ToString
public class VariableParameterJavaType implements JavaType {

    @JsonProperty("parameterized")
    private final boolean parameterized = true;

    @JsonProperty("package")
    private JavaPackage javaPackage;

    @JsonProperty("name")
    private String name;

    @Default
    @JsonProperty("type_variables")
    private SyncList<JavaType> typeVariables = SyncList.empty();

}
