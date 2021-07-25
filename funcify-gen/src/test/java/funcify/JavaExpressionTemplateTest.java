package funcify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import funcify.st.adapter.model.JsonNodeModelAdapter;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.typedef.JavaCodeBlock;
import funcify.typedef.JavaParameter;
import funcify.typedef.javaexpr.TemplatedExpression;
import funcify.typedef.javaexpr.TextExpression;
import funcify.typedef.javastatement.ReturnStatement;
import funcify.typedef.javatype.ParametricJavaType;
import funcify.typedef.javatype.SimpleJavaTypeVariable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-07-25
 */
public class JavaExpressionTemplateTest {

    private static final Path funcifyStringTemplateGroupFilePath = Paths.get("src/main/antlr/funcify/funcify.stg")
                                                                        .toAbsolutePath();
    private static ObjectMapper objectMapper;

    private static STGroupFile stGroupFile;

    @BeforeAll
    public static void beforeAll() {
        objectMapper = new ObjectMapper();
        Assertions.assertTrue(new File(funcifyStringTemplateGroupFilePath.toString()).exists(),
                              "funcifyStringTemplateGroupFilePath does not exist");
        stGroupFile = new STGroupFile(funcifyStringTemplateGroupFilePath.toString(),
                                      "UTF-8");
        stGroupFile.registerModelAdaptor(JsonNode.class,
                                         new JsonNodeModelAdapter());
    }

    @Test
    public void lambdaSimpleExpressionTest() {
        final TemplatedExpression templatedExpression = TemplatedExpression.builder()
                                                                           .templateFunction("lambda")
                                                                           .input(SyncMap.of("parameters",
                                                                                             SyncList.of(JavaParameter.builder()
                                                                                                                      .name("aList")
                                                                                                                      .type(ParametricJavaType.builder()
                                                                                                                                              .name("List")
                                                                                                                                              .typeVariables(SyncList.of(SimpleJavaTypeVariable.of("A")))
                                                                                                                                              .build())
                                                                                                                      .build()),
                                                                                             "expression",
                                                                                             TextExpression.of("aList")))
                                                                           .build();
        final JsonNode jsonNode = objectMapper.valueToTree(templatedExpression);
        System.out.println(jsonNode.toPrettyString());
        ST stringTemplate = stGroupFile.getInstanceOf("expression")
                                       .add("expression",
                                            jsonNode);
        final String actualOutput = stringTemplate.render();
        System.out.println(actualOutput);
        final String expectedOutput = "(List<A> aList) -> aList";
        Assertions.assertEquals(expectedOutput,
                                actualOutput);

    }

    @Test
    public void lambdaCodeBlockExpressionTest() {
        final TemplatedExpression templatedExpression = TemplatedExpression.builder()
                                                                           .templateFunction("lambda")
                                                                           .input(SyncMap.of("parameters",
                                                                                             SyncList.of(JavaParameter.builder()
                                                                                                                      .name("aList")
                                                                                                                      .type(ParametricJavaType.builder()
                                                                                                                                              .name("List")
                                                                                                                                              .typeVariables(SyncList.of(SimpleJavaTypeVariable.of("A")))
                                                                                                                                              .build())
                                                                                                                      .build()),
                                                                                             "code_block",
                                                                                             JavaCodeBlock.builder()
                                                                                                          .statements(SyncList.of(ReturnStatement.builder()
                                                                                                                                                 .expressions(SyncList.of(TextExpression.of("aList")))
                                                                                                                                                 .build()))
                                                                                                          .build()))
                                                                           .build();
        final JsonNode jsonNode = objectMapper.valueToTree(templatedExpression);
        System.out.println(jsonNode.toPrettyString());
        ST stringTemplate = stGroupFile.getInstanceOf("expression")
                                       .add("expression",
                                            jsonNode);
        final String actualOutput = stringTemplate.render();
        System.out.println(actualOutput);
        final String expectedOutput = "(List<A> aList) -> {\n        return aList;\n    }";
        Assertions.assertEquals(expectedOutput,
                                actualOutput);

    }

}
