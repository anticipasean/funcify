package funcify.ensemble.trait.mappable;

import funcify.ensemble.EnsembleKind;
import funcify.ensemble.template.TraitGenerationTemplate;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class DisjunctMappableTypeTemplate<V, R> implements TraitGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(DisjunctMappableTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.DISJUNCT,
                          Trait.MAPPABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "trait",
                             "mappable",
                             "disjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr",
                         "funcify",
                         "disjunct_mappable_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty()
                            .put("types",
                                 "disjunctMappableEnsemble[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getDisjunctMappableEnsembleTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                final String className = "DisjunctMappable" + ek.getSimpleClassName();
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
                                                                  "DisjunctWrappable" + ek.getSimpleClassName())
                                                              .put("ensemble_type_package",
                                                                   Arrays.asList("funcify",
                                                                                 "trait",
                                                                                 "wrappable",
                                                                                 "disjunct"))
                                                              .put("next_type_variable_sequences",
                                                                   nextTypeVariableSequences(ek.getNumberOfValueParameters()));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .typePackagePathSegments(getDestinationTypePackagePathSegments())
                                                                         .templateFunctionName("disjunct_mappable_type")
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFilePath(getStringTemplateGroupFilePath())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final WriteResult<R> writeResult = templateWriter.write(spec);
                results.put(ek,
                            writeResult);
            }
            return session.withConjunctMappableEnsembleTypeResults(results);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
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
