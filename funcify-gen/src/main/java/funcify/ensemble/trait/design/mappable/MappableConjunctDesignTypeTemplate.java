package funcify.ensemble.trait.design.mappable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.template.TraitDesignGenerationTemplate;
import funcify.error.FuncifyCodeGenException;
import funcify.session.TypeGenerationSession;
import funcify.spec.DefaultStringTemplateSpec;
import funcify.spec.StringTemplateSpec;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class MappableConjunctDesignTypeTemplate<V, R> implements TraitDesignGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(MappableConjunctDesignTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.CONJUNCT, Trait.MAPPABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "trait", "design", "mappable", "conjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "mappable_conjunct_design_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]", SyncMap.empty().put("types", "ConjunctMappableEnsembleDesign[1..n]"));
        try {
            final SyncList<EnsembleKind> ensembleKindsToUse = session.getEnsembleKinds().copy();
            session.getEnsembleKinds()
                   .stream()
                   .max(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
                   .ifPresent(ensembleKindsToUse::removeValue);
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getConjunctMappableDesignTypeResults();
            for (EnsembleKind ek : ensembleKindsToUse) {
                final String className = getTraitNameForEnsembleKind(ek) + "Design";
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  className,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()),
                                                                  "next_type_variable",
                                                                  CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters()))
                                                              .put("implemented_type",
                                                                   getImplementedTypeInstance(ek, Trait.CONJUNCT))
                                                              .put("container_type",
                                                                   getContainerTypeJsonInstanceFor(ek, Trait.CONJUNCT))
                                                              .put("next_type_variable_sequences",
                                                                   nextTypeVariableSequences(ek.getNumberOfValueParameters()))
                                                              .put("map_all_type_variables",
                                                                   mapAllMethodTypeVariablePairs(ek.getNumberOfValueParameters()));
                final StringTemplateSpec spec = DefaultStringTemplateSpec.builder()
                                                                         .typeName(className)
                                                                         .typePackagePathSegments(
                                                                                 getDestinationTypePackagePathSegments())
                                                                         .templateFunctionName(getStringTemplateGroupFileName())
                                                                         .fileTypeExtension(".java")
                                                                         .stringTemplateGroupFilePath(
                                                                                 getStringTemplateGroupFilePath())
                                                                         .destinationParentDirectoryPath(session.getDestinationDirectoryPath())
                                                                         .templateFunctionParameterInput(params)
                                                                         .build();
                final WriteResult<R> writeResult = templateWriter.write(spec);
                if (writeResult.isFailure()) {
                    throw writeResult.getFailureValue().orElseThrow(() -> new FuncifyCodeGenException("failure value missing"));
                }
                results.put(ek, writeResult);
            }
            return session.withConjunctMappableDesignTypeResults(results);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass().getSimpleName(),
                         t.getMessage());
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(), t);
            }
        }
    }

    private List<List<String>> nextTypeVariableSequences(final int numberOfValueParameters) {
        final String nextTypeVariable = CharacterOps.uppercaseLetterByIndexWithNumericExtension(numberOfValueParameters);
        final String[] array = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(numberOfValueParameters)
                                           .toArray(String[]::new);
        return IntStream.range(0, numberOfValueParameters).mapToObj(i -> {
            return Stream.concat(Stream.concat(Arrays.stream(array, 0, i), Stream.of(nextTypeVariable)),
                                 Arrays.stream(array, i + 1, numberOfValueParameters)).collect(Collectors.toList());
        }).collect(Collectors.toList());

    }

    private JsonNode mapAllMethodTypeVariablePairs(final int numberOfValueParameters) {
        final Spliterator<String> nextTypeVariables = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(
                                                                          numberOfValueParameters * 2)
                                                                  //Need to make a list first rather than directly using the
                                                                  //stream's spliterator so the result spliterator is SIZED
                                                                  .collect(Collectors.toList()).spliterator();
        final Spliterator<String> givenTypeVariables = nextTypeVariables.trySplit();
        final String[] typeVariablePairHolder = new String[2];
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        while (givenTypeVariables.tryAdvance(s -> typeVariablePairHolder[0] = s) &&
               nextTypeVariables.tryAdvance(s -> typeVariablePairHolder[1] = s)) {
            final ObjectNode objNode = JsonNodeFactory.instance.objectNode()
                                                               .put("given", typeVariablePairHolder[0])
                                                               .put("next", typeVariablePairHolder[1]);
            arrayNode = arrayNode.add(objNode);
        }
        return arrayNode;
    }
}
