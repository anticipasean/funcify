package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.Definition;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.VariableParameterJavaType;
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
@Builder
@Getter
@With
public class JavaTypeDefinition implements Definition<JavaTypeDefinition> {

    @JsonProperty("package")
    private JavaPackage javaPackage;

    @JsonProperty("imports")
    @Default
    private List<JavaImport> imports = new ArrayList<>();

    @JsonProperty("annotations")
    @Default
    private List<JavaAnnotation> annotations = new ArrayList<>();

    @JsonProperty("modifiers")
    @Default
    private List<JavaModifier> modifiers = new ArrayList<>();

    @JsonProperty("type_kind")
    private JavaTypeKind typeKind;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_variables")
    @Default
    private List<JavaType> typeVariables = new ArrayList<>();

    @JsonProperty("super_type")
    private JavaType superType;

    @JsonProperty("implemented_interface_types")
    @Default
    private List<JavaType> implementedInterfaceTypes = new ArrayList<>();

    @JsonProperty("fields")
    @Default
    private List<JavaField> fields = new ArrayList<>();

    @JsonProperty("methods")
    @Default
    private List<JavaMethod> methods = new ArrayList<>();

    @JsonProperty("sub_type_definitions")
    @Default
    private List<JavaTypeDefinition> subTypeDefinitions = new ArrayList<>();

    public JavaType getJavaType() {
        return VariableParameterJavaType.builder()
                                        .javaPackage(javaPackage)
                                        .name(name)
                                        .typeVariables(typeVariables)
                                        .build();
    }

}
