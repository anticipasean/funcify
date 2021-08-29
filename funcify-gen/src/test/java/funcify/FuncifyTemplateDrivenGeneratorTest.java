package funcify;

import funcify.ensemble.EnsembleKind;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap.Tuple2;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public class FuncifyTemplateDrivenGeneratorTest {

    @Test
    public void generateEnsembleInterfaceTypesTest() {
        //        final URI uri = URI.create("file:///" + Paths.get("src/main/antlr/funcify/java_type_definition.stg")
        //                                                     .toAbsolutePath());

        final Path path = Paths.get("src/main/antlr/funcify/ensemble_type.stg")
                               .toAbsolutePath();
        //        System.out.println("path: " + path);
        //        System.out.println("exists: " + new File(path.toString()).exists());
        final STGroupFile stGroupFile = new STGroupFile(path.toString(),
                                                        "UTF-8");

        Arrays.stream(EnsembleKind.values())
              .sorted(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
              .forEach(ek -> {
                  try {
                      ST stringTemplate = Stream.of(Tuple2.of("package",
                                                              Arrays.asList("funcify",
                                                                            "ensemble")),
                                                    Tuple2.of("class_name",
                                                              ek.getSimpleClassName()),
                                                    Tuple2.of("is_solo",
                                                              ek == EnsembleKind.SOLO),
                                                    Tuple2.of("witness_type",
                                                              "WT"),
                                                    Tuple2.of("type_variables",
                                                              CharacterOps.firstNUppercaseLettersAsStrings(ek.getNumberOfValueParameters())
                                                                          .collect(Collectors.toList())),
                                                    Tuple2.of("next_type_variable",
                                                              CharacterOps.uppercaseLetterByIndex(ek.getNumberOfValueParameters())
                                                                          .orElse(null)))
                                                .reduce(stGroupFile.getInstanceOf("ensemble_type"),
                                                        (st, entry) -> st.add(entry._1(),
                                                                              entry._2()),
                                                        (st1, st2) -> st2);
                      System.out.println(stringTemplate.render());
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              });

    }


}
