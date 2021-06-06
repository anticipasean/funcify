package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.Definition;
import funcify.tool.container.SyncList;
import funcify.typedef.javatype.JavaType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class JavaMethod implements Definition<JavaMethod> {

    @JsonProperty("annotations")
    @Default
    private SyncList<JavaAnnotation> annotations = SyncList.empty();

    @JsonProperty("modifiers")
    @Default
    private SyncList<JavaModifier> modifiers = SyncList.empty();

    @JsonProperty("type_variables")
    @Default
    private SyncList<JavaType> typeVariables = SyncList.empty();

    @JsonProperty("return_type")
    private JavaType returnType;

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameters")
    @Default
    private SyncList<JavaParameter> parameters = SyncList.empty();

    @JsonProperty("code_block")
    private JavaCodeBlock codeBlock;

}
