package funcify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public class FuncifyClassGeneratorTest {

    private GenerationSession buildInitialGenerationSession() {
        return GenerationSession.builder()
                                .ensembleKinds(Arrays.asList(EnsembleKind.values()))
                                .build();
    }

    @Test
    public void generateEnsembleInterfaceTypesTest() {
        final GenerationSession updatedSession = new EnsembleInterfaceTypeAssembler().assembleEnsembleInterfaceTypes(buildInitialGenerationSession());

        final URI uri = URI.create("file:///" + Paths.get("src/main/antlr/funcify/java_type.stg")
                                                     .toAbsolutePath());

        final Path path = Paths.get("src/main/antlr/funcify/funcify.stg")
                               .toAbsolutePath();
        System.out.println("path: " + path);
        System.out.println("exists: " + new File(path.toString()).exists());
        final STGroupFile stGroupFile = new STGroupFile(path.toString(),
                                                        "UTF-8");
        stGroupFile.registerModelAdaptor(JsonNode.class,
                                         new JsonNodeModelAdapter());
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(updatedSession.getBaseEnsembleInterfaceTypeDefinition());
        final JsonNode eNode = objectMapper.valueToTree(updatedSession.getBaseEnsembleInterfaceTypeDefinition());
        ST ensembleBaseTypeStrTemplate = stGroupFile.getInstanceOf("java_type")
                                                    .add("type_definition",
                                                         eNode);
        System.out.println(ensembleBaseTypeStrTemplate.render());
//        System.out.println("ensemble_base_type.json: " + eNode.toPrettyString());

        updatedSession.getEnsembleInterfaceTypeDefinitionsByEnsembleKind()
                      .entrySet()
                      .stream()
                      .sorted(Comparator.comparing(Entry::getKey,
                                                   Comparator.comparing(EnsembleKind::getNumberOfValueParameters)))
                      .forEach(entry -> {
                          try {
                              final JsonNode jsonNode = objectMapper.valueToTree(entry.getValue());
                              ST stringTemplate = stGroupFile.getInstanceOf("java_type")
                                                             .add("type_definition",
                                                                  jsonNode);
                              System.out.println("ensemble: " + entry.getKey()
                                                                     .name());
//                              System.out.println("ensemble.json: " + jsonNode.toPrettyString());
                              System.out.println(stringTemplate.render());
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                      });

    }

}
