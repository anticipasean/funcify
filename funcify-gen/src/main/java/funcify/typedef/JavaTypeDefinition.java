package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.Definition;
import funcify.tool.container.SyncList;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.VariableParameterJavaType;
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
@Builder
@Getter
@With
@ToString
public class JavaTypeDefinition implements Definition<JavaTypeDefinition> {

    @JsonProperty("package")
    private JavaPackage javaPackage;

    @JsonProperty("imports")
    @Default
    private SyncList<JavaImport> javaImports = SyncList.empty();

    @JsonProperty("annotations")
    @Default
    private SyncList<JavaAnnotation> annotations = SyncList.empty();

    @JsonProperty("modifiers")
    @Default
    private SyncList<JavaModifier> modifiers = SyncList.empty();

    @JsonProperty("type_kind")
    private JavaTypeKind typeKind;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type_variables")
    @Default
    private SyncList<JavaType> typeVariables = SyncList.empty();

    @JsonProperty("super_type")
    private JavaType superType;

    @JsonProperty("implemented_interface_types")
    @Default
    private SyncList<JavaType> implementedInterfaceTypes = SyncList.empty();

    @JsonProperty("fields")
    @Default
    private SyncList<JavaField> fields = SyncList.empty();

    @JsonProperty("methods")
    @Default
    private SyncList<JavaMethod> methods = SyncList.empty();

    @JsonProperty("sub_type_definitions")
    @Default
    private SyncList<JavaTypeDefinition> subTypeDefinitions = SyncList.empty();

    public JavaType getJavaType() {
        return VariableParameterJavaType.builder()
                                        .javaPackage(javaPackage)
                                        .name(name)
                                        .typeVariables(typeVariables)
                                        .build();
    }

}
