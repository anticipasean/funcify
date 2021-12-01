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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator.OfInt;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
        final ZipMethodContext context = ZipMethodContext.builder()
                                                         .containerParams(containerParams)
                                                         .build();
        final ZipMethodContext updatedContext = unwrapContainersRecursively(context);
        System.out.println(updatedContext.getMethodTextBuilder());
        zipMethodContainer.put("body",
                               updatedContext.getMethodTextBuilder()
                                             .toString());
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

    private ZipMethodContext unwrapContainersRecursively(final ZipMethodContext context) {
        Objects.requireNonNull(context, () -> "context");
        if (context.getMethodTextBuilder()
                   .length() == 0
            && context.getContainerParams()
                      .size() > 0) {
            context.getContextStack()
                   .push(context.withContainerIndex(0)
                                .withTypeVariableIndex(0));
        }
        while (!context.getContextStack()
                       .isEmpty()) {
            final ZipMethodContext currentContext = context.getContextStack()
                                                           .pop();
            if (isUnhappyPath(currentContext)) {
                unwrapUnhappyPath(currentContext);
            } else {
                unwrapHappyPath(currentContext);
            }
            // Shift type variable until done
            if (currentContext.getTypeVariableIndex()
                < currentContext.getTypeVariablesForContainer()
                                .size() - 1) {
                context.getContextStack()
                       .push(currentContext.withTypeVariableIndex(currentContext.getTypeVariableIndex() + 1));
            }
        }
        return context;
    }

    private boolean requiresContainerDeclaration(final ZipMethodContext context) {
        return context.getTypeVariableIndex() == 0;
    }


    private void unwrapUnhappyPath(final ZipMethodContext currentContext) {
        if (requiresContainerDeclaration(currentContext)) {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("return container")
                          .append(currentContext.getContainerIndex() + 1)
                          .append(".fold(");
        } else {
            currentContext.getMethodTextBuilder()
                          .append(getNFourSpaceIndent(Math.max(2, currentContext.getContainerIndex())));
        }
        if (currentContext.getContainerIndex()
            < currentContext.getContainerParams()
                            .size() - 1) {
            appendFunctionParameterInputExpressionWithInputIndex(currentContext);
            currentContext.getMethodTextBuilder()
                          .append(" -> ")
                          .append("{\n");
            // shift container_index until done
            currentContext.getContextStack()
                          .push(currentContext.withContainerIndex(currentContext.getContainerIndex() + 1));
            unwrapContainersRecursively(currentContext);
        } else {
            appendFunctionParameterInputExpression(currentContext);
            currentContext.getMethodTextBuilder()
                          .append(" -> ")
                          .append("{\n");
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("return this.wrap")
                          .append(currentContext.getTypeVariableIndex() + 1)
                          .append("(")
                          .append("input")
                          .append(currentContext.getTypeVariableText())
                          .append(currentContext.getContainerIndex() + 1)
                          .append("));\n");
        }
        if (currentContext.getTypeVariableIndex()
            < currentContext.getTypeVariablesForContainer()
                            .size() - 1) {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("},\n");
        } else {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("}\n");
        }
    }

    private boolean isUnhappyPath(final ZipMethodContext currentContext) {
        return currentContext.getTypeVariablesForContainer()
                             .size() > 1 && StreamSupport.stream(currentContext.getContainerParams()
                                                                               .spliterator(), false)
                                                         .map(jn -> jn.get("type_variables"))
                                                         .map(jn -> jn.get(currentContext.getTypeVariableIndex()))
                                                         .map(JsonNode::asText)
                                                         .distinct()
                                                         .count() == 1;
    }

    private void unwrapHappyPath(final ZipMethodContext currentContext) {
        if (requiresContainerDeclaration(currentContext)) {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("return container")
                          .append(currentContext.getContainerIndex() + 1)
                          .append(".fold(");
        } else {
            currentContext.getMethodTextBuilder()
                          .append(getNFourSpaceIndent(Math.max(2, currentContext.getContainerIndex())));
        }
        if (currentContext.getContainerIndex()
            < currentContext.getContainerParams()
                            .size() - 1) {
            appendFunctionParameterInputExpression(currentContext);
            currentContext.getMethodTextBuilder()
                          .append(" -> ")
                          .append("{\n");
            // shift container_index until done
            currentContext.getContextStack()
                          .push(currentContext.withContainerIndex(currentContext.getContainerIndex() + 1));
            unwrapContainersRecursively(currentContext);
        } else {
            appendFunctionParameterInputExpression(currentContext);
            currentContext.getMethodTextBuilder()
                          .append(" -> ")
                          .append("{\n");
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("return this.wrap")
                          .append(currentContext.getTypeVariableIndex() + 1)
                          .append("(")
                          .append("zipper.apply(")
                          .append(StreamSupport.stream(currentContext.getContainerParams()
                                                                     .spliterator(), false)
                                               .map(jn -> jn.get("type_variables"))
                                               .map(jn -> jn.get(currentContext.getTypeVariableIndex())
                                                            .asText())
                                               .map(s -> "input" + s)
                                               .collect(Collectors.joining(", ")))
                          .append("));\n");
        }
        if (currentContext.getTypeVariablesForContainer()
                          .size() == 1) {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("},\n")
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("() -> {\n")
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("return this.empty();\n")
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("}")
                          .append(currentContext.getTypeVariablesForContainer()
                                                .size() == 1 ? ");" : "")
                          .append("\n");
        } else if (currentContext.getTypeVariableIndex()
                   < currentContext.getTypeVariablesForContainer()
                                   .size() - 1) {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("},\n");
        } else {
            currentContext.getMethodTextBuilder()
                          .append(currentContext.getIndent())
                          .append(currentContext.getIndent())
                          .append("}\n");
        }
    }

    private void appendFunctionParameterInputExpression(final ZipMethodContext context) {
        context.getMethodTextBuilder()
               .append("(")
               .append(context.getTypeVariableText())
               .append(" ")
               .append("input")
               .append(context.getTypeVariableText())
               .append(")");
    }

    private void appendFunctionParameterInputExpressionWithInputIndex(final ZipMethodContext context) {
        context.getMethodTextBuilder()
               .append("(")
               .append(context.getTypeVariableText())
               .append(" ")
               .append("input")
               .append(context.getTypeVariableText())
               .append(context.getContainerIndex() + 1)
               .append(")");
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    private static class ZipMethodContext {

        @Default
        private final ArrayNode containerParams = JsonNodeFactory.instance.arrayNode();

        @Default
        private final Deque<ZipMethodContext> contextStack = new LinkedList<>();

        @Default
        private final StringBuilder methodTextBuilder = new StringBuilder();

        @Default
        private final int containerIndex = 0;

        @Default
        private final int typeVariableIndex = 0;

        public JsonNode getContainerParam() {
            return containerParams.get(containerIndex);
        }

        public JsonNode getTypeVariable() {
            return getTypeVariablesForContainer().get(typeVariableIndex);
        }

        public ArrayNode getTypeVariablesForContainer() {
            return Optional.of(getContainerParam().get("type_variables"))
                           .filter(ArrayNode.class::isInstance)
                           .map(ArrayNode.class::cast)
                           .orElseThrow(() -> new IllegalArgumentException("type_variables json_node is not an array_node"));
        }

        public String getTypeVariableText() {
            return getTypeVariable().asText();
        }

        public String getIndent() {
            return getNFourSpaceIndent(containerIndex);
        }

    }

    private static String getNFourSpaceIndent(final int numberOfIndents) {
        return Stream.generate(() -> " ")
                     .limit(numberOfIndents * 4L)
                     .collect(Collectors.joining());
    }

}
