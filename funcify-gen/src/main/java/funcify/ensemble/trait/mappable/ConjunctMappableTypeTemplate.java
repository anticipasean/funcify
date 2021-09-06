package funcify.ensemble.trait.mappable;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.TypeGenerationSession;
import funcify.error.FuncifyCodeGenException;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class ConjunctMappableTypeTemplate<V, R> implements TypeGenerationTemplate<V, R> {

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "trait",
                             "mappable",
                             "conjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr",
                         "funcify",
                         "conjunct_mappable_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, R> results = session.getDisjunctWrappableEnsembleTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final String className = "ConjunctMappable" + ek.getSimpleClassName();
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  className,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "next_type_variable",
                                                                  CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters())
                                                                              .orElse(null),
                                                                  "ensemble_type_name",
                                                                  "ConjunctWrappable" + ek.getSimpleClassName())
                                                              .put("ensemble_type_package",
                                                                   Arrays.asList("funcify",
                                                                                 "trait",
                                                                                 "wrappable",
                                                                                 "conjunct"))
                                                              .put("next_type_variable_sequences",
                                                                   nextTypeVariableSequences(ek.getNumberOfValueParameters()));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .templateFunctionName("conjunct_mappable_type")
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFilePath(getStringTemplateGroupFilePath())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final R writeResult = templateWriter.write(spec);
                results.put(ek,
                            writeResult);
            }
            return session.withDisjunctWrappableEnsembleTypeResults(results);
        } catch (final Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(),
                                                  t);
            }
        }
    }

    private List<List<String>> nextTypeVariableSequences(final int numberOfValueParameters) {
        final String nextTypeVariable = CharacterOps.uppercaseLetterByIndexWithNumericExtension(numberOfValueParameters)
                                                    .orElse("");
        final String[] array = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(numberOfValueParameters)
                                           .toArray(String[]::new);
        return IntStream.range(0,
                               numberOfValueParameters)
                        .mapToObj(i -> {
                            return Stream.concat(Stream.concat(Arrays.stream(array,
                                                                             0,
                                                                             i),
                                                               Stream.of(nextTypeVariable)),
                                                 Arrays.stream(array,
                                                               i + 1,
                                                               numberOfValueParameters))
                                         .collect(Collectors.toList());
                        })
                        .collect(Collectors.toList());

    }
}
