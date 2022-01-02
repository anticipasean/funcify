package funcify.ensemble.trait.traversable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.template.TraitFactoryGenerationTemplate;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class TraversableDisjunctFactoryTypeTemplate<V, R> implements TraitFactoryGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(TraversableDisjunctFactoryTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.DISJUNCT, Trait.TRAVERSABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "trait", "traversable", "disjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "traversable_disjunct_factory_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty().put("types", "TraversableDisjunctEnsembleFactory[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = SyncMap.empty();
            final SyncList<EnsembleKind> ensembleKinds = session.getEnsembleKinds().copy();
            // The max ek must be removed because the func type with the max ek will only support max_ek.num_of_params - 1
            session.getEnsembleKinds()
                   .stream()
                   .max(Comparator.comparing(EnsembleKind::getNumberOfValueParameters))
                   .filter(ek -> ek.getNumberOfValueParameters() > 1)
                   .ifPresent(ensembleKinds::removeValue);
            for (EnsembleKind ek : ensembleKinds) {
                final String className = getTraitNameForEnsembleKind(ek) + "Factory";
                final SyncMap<String, Object> params = SyncMap.of("package",
                                                                  getDestinationTypePackagePathSegments(),
                                                                  "class_name",
                                                                  className,
                                                                  "type_variables",
                                                                  CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                                              .collect(Collectors.toList()))
                                                              .put("implemented_type",
                                                                   getImplementedTypeInstance(ek,
                                                                                              Trait.DISJUNCT,
                                                                                              Trait.WRAPPABLE))
                                                              .put("container_type",
                                                                   getContainerTypeJsonInstanceFor(ek, Trait.DISJUNCT))
                                                              .put("disjunct_to_list_sequences",
                                                                   determineDisjunctToListSequencesForEnsembleKind(ek));
                logger.info("params: {}", params.mkString(o -> Objects.toString(o)));

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
            return session.withDisjunctTraversableEnsembleFactoryTypeResults(results);
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

    private JsonNode determineDisjunctToListSequencesForEnsembleKind(final EnsembleKind ensembleKind) {
        final int numberOfTypeVariables = Optional.ofNullable(ensembleKind)
                                                  .map(EnsembleKind::getNumberOfValueParameters)
                                                  .orElse(-1);
        if (numberOfTypeVariables <= 0) {
            return JsonNodeFactory.instance.arrayNode();
        }
        final ArrayNode typeVariables = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(numberOfTypeVariables)
                                                    .reduce(JsonNodeFactory.instance.arrayNode(),
                                                            ArrayNode::add,
                                                            ArrayNode::addAll);
        return IntStream.range(0, numberOfTypeVariables).mapToObj(i -> {
            final ArrayNode foldMethodParameterNodes = IntStream.range(0, numberOfTypeVariables).mapToObj(j -> {
                return ((ObjectNode) JsonNodeFactory.instance.objectNode()
                                                             .set("type_variable", typeVariables.get(j))).put("empty", i != j);
            }).reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
            return ((ObjectNode) JsonNodeFactory.instance.objectNode()
                                                         .set("fold_method_parameter_nodes", foldMethodParameterNodes));
        }).reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
    }


}
