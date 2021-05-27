package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.Definition;
import funcify.typedef.javatype.JavaType;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Getter
@With
@Builder
public class JavaMethod implements Definition<JavaMethod> {

    @JsonProperty("annotations")
    @Default
    private List<JavaAnnotation> annotations = new ArrayList<>();

    @JsonProperty("modifiers")
    @Default
    private List<JavaModifier> modifiers = new ArrayList<>();

    @JsonProperty("type_variables")
    @Default
    private List<JavaType> typeVariables = new ArrayList<>();

    @JsonProperty("return_type")
    private JavaType returnType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameters")
    @Default
    private List<JavaParameter> parameters = new ArrayList<>();

    @JsonProperty("code_block")
    private JavaCodeBlock codeBlock;

}
