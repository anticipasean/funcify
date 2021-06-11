package funcify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import funcify.codegen.JsonNodeModelAdapter;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.factory.generation.EnsembleTypeGenerationFactory;
import funcify.ensemble.factory.session.DefaultEnsembleTypeGenerationSession;
import funcify.ensemble.factory.session.EnsembleTypeGenerationSession;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap.Tuple2;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaMethod;
import funcify.typedef.JavaTypeDefinition;
import funcify.typedef.javaexpr.JavaExpression;
import funcify.typedef.javastatement.JavaStatement;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-05-25
 */
public class FuncifyClassGeneratorTest {

    @Test
    public void generateEnsembleInterfaceTypesTest() {
        final EnsembleTypeGenerationSession<JavaTypeDefinition, JavaMethod, JavaCodeBlock, JavaStatement, JavaExpression> session = runEnsembleTypeGenerationSession();
        //        final URI uri = URI.create("file:///" + Paths.get("src/main/antlr/funcify/java_type_definition.stg")
        //                                                     .toAbsolutePath());

        final Path path = Paths.get("src/main/antlr/funcify/funcify.stg")
                               .toAbsolutePath();
        //        System.out.println("path: " + path);
        //        System.out.println("exists: " + new File(path.toString()).exists());
        final STGroupFile stGroupFile = new STGroupFile(path.toString(),
                                                        "UTF-8");
        stGroupFile.registerModelAdaptor(JsonNode.class,
                                         new JsonNodeModelAdapter());
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(session.getBaseEnsembleInterfaceTypeDefinition());
        final JsonNode eNode = objectMapper.valueToTree(session.getBaseEnsembleInterfaceTypeDefinition());
        ST ensembleBaseTypeStrTemplate = stGroupFile.getInstanceOf("java_type")
                                                    .add("type_definition",
                                                         eNode);
        System.out.println(ensembleBaseTypeStrTemplate.render());
        //                System.out.println("ensemble_base_type.json: " + eNode.toPrettyString());

        session.getEnsembleInterfaceTypeDefinitionsByEnsembleKind()
               .stream()
               .sorted(Comparator.comparing(Tuple2::_1,
                                            Comparator.comparing(EnsembleKind::getNumberOfValueParameters)))
               .forEach(entry -> {
                   try {
                       final JsonNode jsonNode = objectMapper.valueToTree(entry._2());
                       ST stringTemplate = stGroupFile.getInstanceOf("java_type")
                                                      .add("type_definition",
                                                           jsonNode);
                       System.out.println("ensemble: " + entry._1()
                                                              .name());
                       System.out.println("ensemble.json: " + jsonNode.toPrettyString());
                       System.out.println(stringTemplate.render());
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });

    }

    private static EnsembleTypeGenerationSession<JavaTypeDefinition, JavaMethod, JavaCodeBlock, JavaStatement, JavaExpression> runEnsembleTypeGenerationSession() {
        return EnsembleTypeGenerationSession.narrowK(EnsembleTypeGenerationFactory.of()
                                                                                  .generateEnsembleTypesInSession(buildInitialEnsembleInterfaceTypeGenerationSession()));
    }

    private static EnsembleTypeGenerationSession<JavaTypeDefinition, JavaMethod, JavaCodeBlock, JavaStatement, JavaExpression> buildInitialEnsembleInterfaceTypeGenerationSession() {
        return DefaultEnsembleTypeGenerationSession.builder()
                                                   .ensembleKinds(SyncList.of(EnsembleKind.values()))
                                                   .build();
    }

}
