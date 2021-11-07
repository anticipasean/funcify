package funcify.ensemble.trait.zippable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import funcify.tool.LiftOps;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Spliterator.OfInt;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class ZippableDisjunctFactoryTypeTemplate<V, R> implements TraitFactoryGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(ZippableDisjunctFactoryTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.DISJUNCT, Trait.ZIPPABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "trait", "zippable", "disjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "zippable_disjunct_factory_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty()
                            .put("types", "ZippableDisjunctEnsembleFactory[1..n]"));
        try {
            final StringTemplateWriter<V, R> templateWriter = session.getTemplateWriter();
            final SyncMap<EnsembleKind, WriteResult<R>> results = session.getDisjunctMappableEnsembleFactoryTypeResults();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
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
                                                              .put("zip_impl_sequences", LiftOps.tryCatchLift(() -> {
                                                                  final JsonNode zipImplementationSequences = createZipImplementationSequences(
                                                                      ek.getNumberOfValueParameters());
                                                                  logger.debug("full_model: {}",
                                                                               new ObjectMapper().writerWithDefaultPrettyPrinter()
                                                                                                 .writeValueAsString(
                                                                                                     zipImplementationSequences));
                                                                  return zipImplementationSequences;
                                                              })
                                                                                                .orElse(JsonNodeFactory.instance.nullNode()))
                                                              .put("container_type",
                                                                   getContainerTypeJsonInstanceFor(ek, Trait.DISJUNCT));
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
                    throw writeResult.getFailureValue()
                                     .orElseThrow(() -> new FuncifyCodeGenException("failure value missing"));
                }
                results.put(ek, writeResult);
            }
            return session.withDisjunctZippableEnsembleFactoryTypeResults(results);
        } catch (final Throwable t) {
            logger.debug("create_types_for_session: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new FuncifyCodeGenException(t.getMessage(), t);
            }
        }
    }

    private JsonNode createZipImplementationSequences(final int numberOfTypeParameters) {
        return IntStream.range(1, 4)
                        .mapToObj(i -> createZipImplementationSequences(i, numberOfTypeParameters))
                        .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
    }

    private JsonNode createZipImplementationSequences(final int zipMethodIndex,
                                                      final int numberOfTypeParameters) {
        if (zipMethodIndex <= 0) {
            return JsonNodeFactory.instance.objectNode();
        }
        final ObjectNode containerNodes = JsonNodeFactory.instance.objectNode();
        containerNodes.set("other", IntStream.range(numberOfTypeParameters,
                                                    (zipMethodIndex * numberOfTypeParameters) + numberOfTypeParameters)
                                             .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                             .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll));
        containerNodes.set("next", IntStream.range((numberOfTypeParameters * zipMethodIndex) + numberOfTypeParameters,
                                                   (zipMethodIndex * numberOfTypeParameters) + (numberOfTypeParameters * 2))
                                            .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                            .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll));
        containerNodes.set("given", IntStream.range(0, numberOfTypeParameters)
                                             .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                             .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll));
        final ArrayNode containerNodeArr = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < numberOfTypeParameters; i++) {
            final ObjectNode zipMethodContainer = JsonNodeFactory.instance.objectNode();
            zipMethodContainer.put("method_name", "zip" + zipMethodIndex + "on" + (i + 1));
            zipMethodContainer.set("new_type_variables", StreamSupport.stream(containerNodes.get("other")
                                                                                            .spliterator(), false)
                                                                      .limit(zipMethodIndex)
                                                                      .reduce(JsonNodeFactory.instance.arrayNode(),
                                                                              ArrayNode::add,
                                                                              ArrayNode::addAll)
                                                                      .add(containerNodes.get("next")
                                                                                         .get(0)));
            final ArrayNode returnTypeVariables = JsonNodeFactory.instance.arrayNode();
            for (int ri = 0; ri < numberOfTypeParameters; ri++) {
                if (ri == i) {
                    returnTypeVariables.add(containerNodes.get("next")
                                                          .get(0));
                } else {
                    returnTypeVariables.add(containerNodes.get("given")
                                                          .get(ri));
                }
            }
            zipMethodContainer.set("return_type_variables", returnTypeVariables);
            final ArrayNode containerParams = JsonNodeFactory.instance.arrayNode();
            final ArrayNode functionParams = JsonNodeFactory.instance.arrayNode();
            containerParams.add(JsonNodeFactory.instance.objectNode()
                                                        .put("index", 1)
                                                        .set("type_variables", containerNodes.get("given")));
            functionParams.add(containerNodes.get("given")
                                             .get(i));
            for (int z = 1; z < zipMethodIndex + 1; z++) {
                final ObjectNode containerNode = JsonNodeFactory.instance.objectNode();
                containerNode.put("index", z + 1);
                final ArrayNode typeVariables = JsonNodeFactory.instance.arrayNode();
                for (int j = 0; j < numberOfTypeParameters; j++) {
                    if (j == i) {
                        typeVariables.add(containerNodes.get("other")
                                                        .get(z - 1));
                        functionParams.add(containerNodes.get("other")
                                                         .get(z - 1));
                    } else {
                        typeVariables.add(containerNodes.get("given")
                                                        .get(j));
                    }
                }
                containerNode.set("type_variables", typeVariables);
                containerParams.add(containerNode);
            }
            zipMethodContainer.set("container_params", containerParams);
            functionParams.add(containerNodes.get("next")
                                             .get(0));
            zipMethodContainer.set("function_params", functionParams);
            addPathsNodeToContainer(zipMethodContainer);
            containerNodeArr.add(zipMethodContainer);
        }

        return containerNodes.set("containers", containerNodeArr);
    }

    private void addPathsNodeToContainer(final ObjectNode zipMethodContainer) {
        final JsonNode containerParamsNode = zipMethodContainer.get("container_params");
        if (!containerParamsNode.isArray()) {
            return;
        }
        final ArrayNode containerParams = (ArrayNode) containerParamsNode;
        if (containerParams.size() == 0) {
            return;
        }
        final OfInt containerIndicesIter = createPeakingIndicesSpliterator(containerParams.size());
        final ZipMethodContext context = ZipMethodContext.of(new StringBuilder(), containerParams, 0, 0, "");
        final StringBuilder builder = unwrapContainersRecursively(context);
        System.out.println(builder);
        zipMethodContainer.put("body", builder.toString());
    }

    private static OfInt createPeakingIndicesSpliterator(final int peak) {
        if (peak < 0) {
            return Spliterators.emptyIntSpliterator();
        }
        return IntStream.concat(IntStream.range(0, peak),
                                IntStream.iterate(peak - 1, i -> i - 1)
                                         .limit(peak))
                        .spliterator();
    }

    private StringBuilder unwrapContainersRecursively(final ZipMethodContext context) {
        String outerIndent = createSpaceBasedIndentUsingContainerIndex(context.getContainerIndex());
        if (context.getContainerIndex() == 0) {
            context.getMethodTextBuilder()
                   .append("return container")
                   .append(context.getContainerIndex() + 1)
                   .append(".fold(");
        }
        if (context.getContainerIndex()
            >= context.getContainerParams()
                      .size() - 1) {
            return appendReturnExpression(context);
        }
        for (int i = 0; i < context.getContainerParams()
                                   .get(context.getContainerIndex())
                                   .get("type_variables")
                                   .size(); i++) {
            final String innerIndent = createSpaceBasedIndentUsingContainerIndex(context.getContainerIndex() + 1);
            appendFunctionParameterInputExpression(context);
            context.getMethodTextBuilder()
                   .append(" -> {")
                   .append("\n")
                   .append(innerIndent)
                   .append("return container")
                   .append(context.getContainerIndex() + 2)
                   .append(".fold( ");
            unwrapContainersRecursively(context.withContainerIndex(context.getContainerIndex() + 1)
                                               .withTypeVariableIndex(i));
            context.getMethodTextBuilder()
                   .append(innerIndent)
                   .append("}")
                   .append(disjunctSoloEmptyCaseAppender(context));
        }
        return context.getMethodTextBuilder();

    }

    private String createSpaceBasedIndentUsingContainerIndex(final int containerIndex) {
        return Stream.generate(() -> " ")
                     .limit(containerIndex * 4L)
                     .collect(Collectors.joining());
    }

    private StringBuilder appendFunctionParameterInputExpression(final ZipMethodContext context) {
        return context.getMethodTextBuilder()
                      .append("(")
                      .append(context.getContainerParams()
                                     .get(context.getContainerIndex())
                                     .get("type_variables")
                                     .get(context.getTypeVariableIndex())
                                     .asText())
                      .append(" ")
                      .append("input")
                      .append(context.getContainerParams()
                                     .get(context.getContainerIndex())
                                     .get("type_variables")
                                     .get(context.getTypeVariableIndex())
                                     .asText())
                      .append(")");
    }

    private StringBuilder appendReturnExpression(final ZipMethodContext context) {
        return context.getMethodTextBuilder()
                      .append("(")
                      .append(context.getContainerParams()
                                     .get(context.getContainerIndex())
                                     .get("type_variables")
                                     .get(context.getTypeVariableIndex())
                                     .asText())
                      .append(" ")
                      .append("input")
                      .append(context.getContainerParams()
                                     .get(context.getContainerIndex())
                                     .get("type_variables")
                                     .get(context.getTypeVariableIndex())
                                     .asText())
                      .append(") -> {")
                      .append("\n")
                      .append(context.getIndent())
                      .append(context.getIndent())
                      .append("return this.wrap")
                      .append(context.getContainerIndex() + 1)
                      .append("(")
                      .append("zipper.apply(")
                      .append(IntStream.range(0,
                                              context.getContainerParams()
                                                     .size())
                                       .mapToObj(i -> "input" + context.getContainerParams()
                                                                       .get(i)
                                                                       .get("type_variables")
                                                                       .get(context.getTypeVariableIndex())
                                                                       .asText())
                                       .collect(Collectors.joining(", ")))
                      .append("));")
                      .append("\n")
                      .append(context.getIndent())
                      .append("}")
                      .append(disjunctSoloEmptyCaseAppender(context));
    }

    private StringBuilder disjunctSoloEmptyCaseAppender(final ZipMethodContext context) {
        if (context.getContainerParams()
                   .get(context.getContainerIndex())
                   .get("type_variables")
                   .size() != 1) {
            return new StringBuilder("\n");
        }
        return new StringBuilder().append(",")
                                  .append("\n")
                                  .append(context.getIndent())
                                  .append("() -> {")
                                  .append("\n")
                                  .append(context.getIndent())
                                  .append("    return this.empty();")
                                  .append("\n")
                                  .append(context.getIndent())
                                  .append("});")
                                  .append("\n");
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    private static class ZipMethodContext {

        //        private final Deque<FunctionBlock> funcBlockDeque;

        private final StringBuilder methodTextBuilder;

        private final ArrayNode containerParams;

        private final int containerIndex;

        private final int typeVariableIndex;

        private final String indent;

    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    private static class FunctionBlock {

        private final int parentContainerIndex;

        private final int parentTypeVariableIndex;

        private final int childContainerIndex;

        private final int childTypeVariableIndex;
    }

}
