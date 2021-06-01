package funcify;

import static java.util.Arrays.asList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import funcify.typedef.JavaImport;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaModifier;
import funcify.typedef.JavaPackage;
import funcify.typedef.JavaParameter;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.JavaTypeKind;
import funcify.typedef.javatype.BoundedJavaTypeVariable;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import funcify.typedef.javatype.VariableParameterJavaType;
import funcify.typedef.javatype.WildcardJavaTypeBound;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public class JavaTypeDefinitionTest {

    @Test
    public void createTypeDefinitionTest() {
        final JavaTypeDefinition javaTypeDefinition = JavaTypeDefinition.builder()
                                                                        .javaPackage(JavaPackage.builder()
                                                                                                .name("funcify")
                                                                                                .build())
                                                                        .javaImports(Collections.emptyList())
                                                                        .annotations(Collections.emptyList())
                                                                        .modifiers(asList(JavaModifier.PUBLIC))
                                                                        .typeKind(JavaTypeKind.CLASS)
                                                                        .name("Solo")
                                                                        .typeVariables(asList(SimpleJavaTypeVariable.of("W"),
                                                                                              SimpleJavaTypeVariable.of("A")))

                                                                        .superType(null)
                                                                        .implementedInterfaceTypes(Collections.emptyList())
                                                                        .fields(Collections.emptyList())
                                                                        .methods(Collections.emptyList())
                                                                        .build();
        Assertions.assertNotNull(javaTypeDefinition);
    }

    @Test
    public void serializeTypeDefinitionIntoJsonTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStrOutput = "";
        try {
            jsonStrOutput = objectMapper.writeValueAsString(JavaTypeDefinition.builder()
                                                                              .javaPackage(JavaPackage.builder()
                                                                                                      .name("funcify")
                                                                                                      .build())
                                                                              .javaImports(Collections.emptyList())
                                                                              .annotations(Collections.emptyList())
                                                                              .modifiers(Stream.of(JavaModifier.PUBLIC)
                                                                                               .collect(Collectors.toList()))
                                                                              .typeKind(JavaTypeKind.CLASS)

                                                                              .name("Solo")
                                                                              .typeVariables(Stream.of(SimpleJavaTypeVariable.of("W"),
                                                                                                       SimpleJavaTypeVariable.of("A"))
                                                                                                   .collect(Collectors.toList()))

                                                                              .superType(null)
                                                                              .implementedInterfaceTypes(Collections.emptyList())
                                                                              .fields(Collections.emptyList())
                                                                              .methods(Collections.emptyList())
                                                                              .build());
        } catch (JsonProcessingException e) {
            Assertions.fail("json_processing_error: " + e.getMessage());
        }

        Assertions.assertEquals("{\"package\":{\"name\":\"funcify\"},\"imports\":[],\"annotations\":[],\"modifiers\":[\"PUBLIC\"],\"kind\":\"CLASS\",\"java_type\":{\"name\":\"Solo\",\"variables\":[{\"name\":\"W\"},{\"name\":\"A\"}]},\"super_type\":null,\"implemented_interface_types\":[],\"fields\":[],\"methods\":[]}",
                                jsonStrOutput);
    }

    @Test
    public void sttest() {

        final URI uri = URI.create("file:///" + Paths.get("src/main/antlr/funcify/java_type_definition.stg")
                                                     .toAbsolutePath());

        final Path path = Paths.get("src/main/antlr/funcify/java_type_definition.stg")
                               .toAbsolutePath();
        System.out.println("path: " + path);
        System.out.println("exists: " + new File(path.toString()).exists());
        final STGroupFile stGroupFile = new STGroupFile(path.toString(),
                                                        "UTF-8");
        stGroupFile.registerModelAdaptor(JsonNode.class,
                                         new JsonNodeModelAdapter());
        ObjectMapper objectMapper = new ObjectMapper();
        final JsonNode jsonNode = objectMapper.valueToTree(JavaTypeDefinition.builder()
                                                                             .javaPackage(JavaPackage.builder()
                                                                                                     .name("funcify")
                                                                                                     .build())
                                                                             .javaImports(Collections.singletonList(JavaImport.builder()
                                                                                                                              .javaPackage(JavaPackage.of("java.util"))
                                                                                                                              .simpleClassName("Function")
                                                                                                                              .build()))
                                                                             .annotations(Collections.emptyList())
                                                                             .modifiers(Stream.of(JavaModifier.PUBLIC)
                                                                                              .collect(Collectors.toList()))
                                                                             .typeKind(JavaTypeKind.INTERFACE)

                                                                             .name("Solo")
                                                                             .typeVariables(asList(SimpleJavaTypeVariable.of("W"),
                                                                                                      SimpleJavaTypeVariable.of("A")))

                                                                             .superType(null)
                                                                             .implementedInterfaceTypes(Collections.singletonList(VariableParameterJavaType.builder()
                                                                                                                                                           .name("Ensemble")
                                                                                                                                                           .typeVariables(Stream.of(SimpleJavaTypeVariable.of("W"))
                                                                                                                                                                                .collect(Collectors.toList()))
                                                                                                                                                           .build()))
                                                                             .fields(Collections.emptyList())
                                                                             .methods(Collections.singletonList(JavaMethod.builder()
                                                                                                                          .modifiers(Collections.singletonList(JavaModifier.PUBLIC))
                                                                                                                          .typeVariables(Collections.singletonList(SimpleJavaTypeVariable.of("R")))
                                                                                                                          .returnType(SimpleJavaTypeVariable.of("R"))
                                                                                                                          .name("convert")
                                                                                                                          .parameters(Collections.singletonList(JavaParameter.builder()
                                                                                                                                                                             .name("converter")
                                                                                                                                                                             .modifiers(Collections.singletonList(JavaModifier.FINAL))
                                                                                                                                                                             .type(VariableParameterJavaType.builder()
                                                                                                                                                                                                            .javaPackage(JavaPackage.builder()
                                                                                                                                                                                                                                    .name("java.util")
                                                                                                                                                                                                                                    .build())
                                                                                                                                                                                                            .name("Function")
                                                                                                                                                                                                            .typeVariables(Stream.of(BoundedJavaTypeVariable.builder()
                                                                                                                                                                                                                                                            .baseType(VariableParameterJavaType.builder()
                                                                                                                                                                                                                                                                                               .name("Solo")
                                                                                                                                                                                                                                                                                               .typeVariables(Stream.of(SimpleJavaTypeVariable.of("W"),
                                                                                                                                                                                                                                                                                                                        SimpleJavaTypeVariable.of("A"))
                                                                                                                                                                                                                                                                                                                    .collect(Collectors.toList()))
                                                                                                                                                                                                                                                                                               .build())
                                                                                                                                                                                                                                                            .lowerBoundTypes(Collections.singletonList(WildcardJavaTypeBound.getInstance()))
                                                                                                                                                                                                                                                            .build(),
                                                                                                                                                                                                                                     BoundedJavaTypeVariable.builder()
                                                                                                                                                                                                                                                            .upperBoundTypes(Collections.singletonList(WildcardJavaTypeBound.getInstance()))
                                                                                                                                                                                                                                                            .baseType(SimpleJavaTypeVariable.of("R"))
                                                                                                                                                                                                                                                            .build())
                                                                                                                                                                                                                                 .collect(Collectors.toList()))
                                                                                                                                                                                                            .build())
                                                                                                                                                                             .build()))
                                                                                                                          .build()))
                                                                             .build());
        System.out.println(jsonNode.toPrettyString());
        ST stringTemplate = stGroupFile.getInstanceOf("java_type")
                                       .add("type_definition",
                                            jsonNode);
        System.out.println(stringTemplate.render());
    }


}
