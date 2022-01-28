package funcify.ensemble.trait.zippable;

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
import funcify.tool.LiftOps;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;
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
            final SyncMap<EnsembleKind, WriteResult<R>> results = SyncMap.empty();
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
                                                              .put("zip_impl_sequences",
                                                                   LiftOps.tryCatchLift(() -> createZipImplementationSequences(ek.getNumberOfValueParameters()))
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
        final ZipMethodBodyContextVisitor visitor = new ZipMethodBodyContextVisitor();
        final MethodContext context = MethodContext.builder()
                                                   .containerParams(containerParams)
                                                   .returnTypeVariables(Optional.of(zipMethodContainer)
                                                                                .map(on -> on.path("return_type_variables"))
                                                                                .filter(jn -> !jn.isMissingNode())
                                                                                .filter(JsonNode::isArray)
                                                                                .map(ArrayNode.class::cast)
                                                                                .map(an -> an.spliterator())
                                                                                .map(spl -> StreamSupport.stream(spl, false))
                                                                                .orElseGet(Stream::empty)
                                                                                .map(JsonNode::asText)
                                                                                .collect(Collectors.toList()))
                                                   .build();

        try {
            final ContainerRef topContainerRef = ContainerRef.builder()
                                                             .containerSuffix(String.valueOf(1))
                                                             .build();
            context.getComponentStack()
                   .push(topContainerRef);
            visitor.visit(context, topContainerRef);
        } catch (final Throwable t) {
            logger.error("add_paths_node_to_container: [ status: failed ] with error [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
        }
        zipMethodContainer.put("body",
                               context.getMethodTextBuilder()
                                      .toString());
    }

    private static interface MethodBodyComponent {

        void accept(final MethodContext context,
                    final MethodContextVisitor visitor);
    }

    private static interface MethodContextVisitor {

        default void visit(final MethodContext context,
                           final ContainerRef containerRef) {
        }

        default void visit(final MethodContext context,
                           final FoldMethodCall foldMethodCall) {
        }

        default void visit(final MethodContext context,
                           final FunctionParameterValue functionParameterValue) {
        }

        default void visit(final MethodContext context,
                           final ZipperFunctionCallReturnValue zipperFunctionCallReturnValue) {
        }

        default void visit(final MethodContext context,
                           final FunctionBody functionBody) {
        }

        default void visit(final MethodContext context,
                           final EmptyCallReturnValue emptyCallReturnValue) {
        }

        default void visit(final MethodContext context,
                           final WrapCallReturnValue wrapCallReturnValue) {
        }
    }


    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    private static class MethodContext {

        @Default
        private final ArrayNode containerParams = JsonNodeFactory.instance.arrayNode();

        @Default
        private final Deque<MethodBodyComponent> componentStack = new LinkedList<>();

        @Default
        private final StringBuilder methodTextBuilder = new StringBuilder();

        @Default
        private final List<String> returnTypeVariables = new ArrayList<>();

        public int getTotalNumberOfContainers() {
            return getContainerParams().size();
        }

        public int getTotalNumberOfTypeVariables() {
            return getContainerParams().size() == 0 ? 0 : getContainerParams().get(0)
                                                                              .get("type_variables")
                                                                              .size();
        }

    }


    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class ContainerRef implements MethodBodyComponent {

        @Default
        private final String containerName = "container";

        @Default
        private final String containerSuffix = "";

        @Default
        private final int containerIndex = 0;

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class FoldMethodCall implements MethodBodyComponent {

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }


    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class FunctionParameterValue implements MethodBodyComponent {

        @Default
        private final int containerIndex = 0;

        @Default
        private final int typeVariableIndex = 0;

        @Default
        private final String inputParameterName = "input";

        @Default
        private final String inputParameterSuffix = "";

        @Default
        private final boolean isUnhappyPath = true;

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class FunctionBody implements MethodBodyComponent {

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }


    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class ZipperFunctionCallReturnValue implements MethodBodyComponent {

        @Default
        private final String wrapFunctionName = "wrap";

        @Default
        private final String wrapFunctionSuffix = "";

        @Default
        private final String zipperFunctionName = "zipper";

        @Default
        private final String functionNameSuffix = "";

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class WrapCallReturnValue implements MethodBodyComponent {

        @Default
        private final String wrapMethodName = "wrap";

        @Default
        private final String methodSuffix = "";

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }

    @AllArgsConstructor(staticName = "of")
    @Builder
    @Getter
    @With
    @ToString
    private static class EmptyCallReturnValue implements MethodBodyComponent {

        @Default
        private final String emptyFunctionName = "empty";

        @Override
        public void accept(final MethodContext context,
                           final MethodContextVisitor visitor) {
            visitor.visit(context, this);
        }
    }


    private static class ZipMethodBodyContextVisitor implements MethodContextVisitor {

        @Override
        public void visit(final MethodContext context,
                          final ContainerRef containerRef) {

            final FoldMethodCall foldMethodCall = FoldMethodCall.of();
            context.getMethodTextBuilder()
                   .append(getNFourSpaceIndent(containerRef.getContainerIndex()))
                   .append("return ")
                   .append(String.join("", containerRef.getContainerName(), containerRef.getContainerSuffix()));
            visit(context, foldMethodCall);
            context.getMethodTextBuilder()
                   .append(";\n");


        }

        @Override
        public void visit(final MethodContext context,
                          final FoldMethodCall foldMethodCall) {

            final MethodBodyComponent currentContainerRef = context.getComponentStack()
                                                                   .pollFirst();
            if (currentContainerRef == null) {
                throw new IllegalStateException("current container ref expected but not present on stack");
            }
            final int containerIndex = Optional.of(currentContainerRef)
                                               .filter(ContainerRef.class::isInstance)
                                               .map(ContainerRef.class::cast)
                                               .map(ContainerRef::getContainerIndex)
                                               .orElse(-1);
            context.getComponentStack()
                   .push(currentContainerRef);
            context.getMethodTextBuilder()
                   .append(".fold(");
            context.getComponentStack()
                   .push(foldMethodCall);
            final int totalNumberOfTypeVariables = context.getTotalNumberOfTypeVariables();
            for (int i = 0; i < totalNumberOfTypeVariables; i++) {
                final int typeVariableIndex = i;
                final String typeVariable = getTypeVariableForContainerAndTypeVariableIndex(context.getContainerParams(),
                                                                                            containerIndex,
                                                                                            typeVariableIndex);
                final boolean isUnhappyPath = Optional.of(context.getContainerParams())
                                                      .map(Iterable::spliterator)
                                                      .map(spl -> StreamSupport.stream(spl, false))
                                                      .orElseGet(Stream::empty)
                                                      .map(jn -> jn.path("type_variables"))
                                                      .filter(jn -> !jn.isMissingNode())
                                                      .map(jn -> jn.path(typeVariableIndex))
                                                      .filter(jn -> !jn.isMissingNode())
                                                      .map(JsonNode::asText)
                                                      .distinct()
                                                      .count() == 1;
                final FunctionParameterValue functionParameterValue = FunctionParameterValue.builder()
                                                                                            .inputParameterSuffix(
                                                                                                isUnhappyPath ? String.join("",
                                                                                                                            typeVariable,
                                                                                                                            String.valueOf(
                                                                                                                                containerIndex
                                                                                                                                + 1))
                                                                                                    : typeVariable)
                                                                                            .containerIndex(containerIndex)
                                                                                            .typeVariableIndex(typeVariableIndex)
                                                                                            .isUnhappyPath(isUnhappyPath)
                                                                                            .build();
                visit(context, functionParameterValue);
                if (i < totalNumberOfTypeVariables - 1) {
                    context.getMethodTextBuilder()
                           .append(",\n");
                }
                if (totalNumberOfTypeVariables == 1) {
                    visit(context,
                          EmptyCallReturnValue.builder()
                                              .build());
                }
            }
            context.getMethodTextBuilder()
                   .append(")");
            if (context.getComponentStack()
                       .peekFirst() instanceof FoldMethodCall) {
                context.getComponentStack()
                       .pop();
            }

        }

        @Override
        public void visit(final MethodContext context,
                          final FunctionParameterValue functionParameterValue) {
            context.getComponentStack()
                   .push(functionParameterValue);
            if (functionParameterValue.getTypeVariableIndex() != 0) {
                context.getMethodTextBuilder()
                       .append(getNFourSpaceIndent(functionParameterValue.getContainerIndex()));
            }
            context.getMethodTextBuilder()
                   .append("(")
                   .append(getTypeVariableForContainerAndTypeVariableIndex(context.getContainerParams(),
                                                                           functionParameterValue.getContainerIndex(),
                                                                           functionParameterValue.getTypeVariableIndex()))
                   .append(" ")
                   .append(functionParameterValue.getInputParameterName())
                   .append(functionParameterValue.getInputParameterSuffix())
                   .append(") -> {\n");
            final FunctionBody functionBody = FunctionBody.of();
            visit(context, functionBody);
            context.getMethodTextBuilder()
                   .append(getNFourSpaceIndent(functionParameterValue.getContainerIndex()))
                   .append("}");
            if (context.getComponentStack()
                       .peekFirst() instanceof FunctionParameterValue) {
                context.getComponentStack()
                       .pop();
            }

        }

        @Override
        public void visit(final MethodContext context,
                          final FunctionBody functionBody) {

            context.getComponentStack()
                   .push(functionBody);
            final Optional<FunctionParameterValue> functionParameterValueForBody = Optional.ofNullable(context.getComponentStack())
                                                                                           .map(Deque::stream)
                                                                                           .orElseGet(Stream::empty)
                                                                                           .filter(FunctionParameterValue.class::isInstance)
                                                                                           .map(FunctionParameterValue.class::cast)
                                                                                           .findFirst();

            if (functionParameterValueForBody.filter(FunctionParameterValue::isUnhappyPath)
                                             .isPresent()
                && functionParameterValueForBody.map(FunctionParameterValue::getTypeVariableIndex)
                                                .orElse(-1) >= 0) {
                final int typeVariableIndex = functionParameterValueForBody.map(FunctionParameterValue::getTypeVariableIndex)
                                                                           .orElse(-1);
                visit(context,
                      WrapCallReturnValue.builder()
                                         .methodSuffix(String.valueOf(typeVariableIndex + 1))
                                         .build());
            } else {
                final long numOfContainersTraversed = Optional.ofNullable(context.getComponentStack())
                                                              .map(Deque::stream)
                                                              .orElseGet(Stream::empty)
                                                              .filter(ContainerRef.class::isInstance)
                                                              .count();
                if (numOfContainersTraversed <= context.getTotalNumberOfContainers() - 1) {
                    final Optional<ContainerRef> parentContainerRefOpt = Optional.ofNullable(context.getComponentStack())
                                                                                 .map(Deque::stream)
                                                                                 .orElseGet(Stream::empty)
                                                                                 .filter(ContainerRef.class::isInstance)
                                                                                 .map(ContainerRef.class::cast)
                                                                                 .findFirst();
                    final int nextContainerIndex = parentContainerRefOpt.map(ContainerRef::getContainerIndex)
                                                                        .map(i -> i + 1)
                                                                        .filter(i -> i < context.getTotalNumberOfContainers())
                                                                        .orElse(-1);
                    if (nextContainerIndex >= 0) {
                        final ContainerRef containerRef = ContainerRef.builder()
                                                                      .containerIndex(nextContainerIndex)
                                                                      .containerSuffix(String.valueOf(nextContainerIndex + 1))
                                                                      .build();
                        context.getComponentStack()
                               .push(containerRef);
                        visit(context, containerRef);
                        if (context.getComponentStack()
                                   .peekFirst() instanceof ContainerRef) {
                            context.getComponentStack()
                                   .pop();
                        }
                    }
                } else {
                    final int closestFuncParamValTypeVariableIndex = Optional.of(context.getComponentStack())
                                                                             .map(Deque::stream)
                                                                             .orElseGet(Stream::empty)
                                                                             .filter(FunctionParameterValue.class::isInstance)
                                                                             .map(FunctionParameterValue.class::cast)
                                                                             .findFirst()
                                                                             .map(FunctionParameterValue::getTypeVariableIndex)
                                                                             .orElse(-1);
                    final ZipperFunctionCallReturnValue zipperFunctionCallReturnValue = ZipperFunctionCallReturnValue.builder()
                                                                                                                     .wrapFunctionSuffix(
                                                                                                                         String.valueOf(
                                                                                                                             closestFuncParamValTypeVariableIndex
                                                                                                                             + 1))
                                                                                                                     .build();
                    visit(context, zipperFunctionCallReturnValue);
                }
            }
            if (context.getComponentStack()
                       .peekFirst() instanceof FunctionBody) {
                context.getComponentStack()
                       .pop();
            }

        }

        @Override
        public void visit(final MethodContext context,
                          final ZipperFunctionCallReturnValue zipperFunctionCallReturnValue) {
            final String stringListOfInputParams = context.getComponentStack()
                                                          .stream()
                                                          .filter(FunctionParameterValue.class::isInstance)
                                                          .map(FunctionParameterValue.class::cast)
                                                          .map(fpv -> String.join("",
                                                                                  fpv.getInputParameterName(),
                                                                                  fpv.getInputParameterSuffix()))
                                                          .reduce(new LinkedList<String>(), (strList, str) -> {
                                                              strList.push(str);
                                                              return strList;
                                                          }, (l1, l2) -> {
                                                              l1.addAll(l2);
                                                              return l1;
                                                          })
                                                          .stream()
                                                          .collect(Collectors.joining(", "));
            final int totalNumberOfContainers = context.getTotalNumberOfContainers();
            context.getMethodTextBuilder()
                   .append(getNFourSpaceIndent(totalNumberOfContainers))
                   .append("return this.")
                   .append(new StringJoiner("", "<", ">").add(context.getReturnTypeVariables()
                                                                     .stream()
                                                                     .collect(Collectors.joining(", ")))
                                                         .toString())
                   .append(zipperFunctionCallReturnValue.getWrapFunctionName())
                   .append(zipperFunctionCallReturnValue.getWrapFunctionSuffix())
                   .append("(")
                   .append(zipperFunctionCallReturnValue.getZipperFunctionName())
                   .append(".apply(")
                   .append(stringListOfInputParams)
                   .append("));\n");
        }

        @Override
        public void visit(final MethodContext context,
                          final EmptyCallReturnValue emptyCallReturnValue) {

            final int closestContainerIndex = context.getComponentStack()
                                                     .stream()
                                                     .filter(ContainerRef.class::isInstance)
                                                     .map(ContainerRef.class::cast)
                                                     .map(ContainerRef::getContainerIndex)
                                                     .findFirst()
                                                     .orElse(-1);
            context.getMethodTextBuilder()
                   .append(",\n")
                   .append(getNFourSpaceIndent(closestContainerIndex))
                   .append("() -> {\n")
                   .append(getNFourSpaceIndent(closestContainerIndex + 1))
                   .append("return this.empty();\n")
                   .append(getNFourSpaceIndent(closestContainerIndex))
                   .append("}");
        }

        @Override
        public void visit(final MethodContext context,
                          final WrapCallReturnValue wrapCallReturnValue) {
            final int closestContainerIndex = context.getComponentStack()
                                                     .stream()
                                                     .filter(ContainerRef.class::isInstance)
                                                     .map(ContainerRef.class::cast)
                                                     .map(ContainerRef::getContainerIndex)
                                                     .findFirst()
                                                     .orElse(-1);
            final Optional<FunctionParameterValue> closestFunctionalParameterVal = Optional.ofNullable(context.getComponentStack())
                                                                                           .map(Deque::stream)
                                                                                           .orElseGet(Stream::empty)
                                                                                           .filter(FunctionParameterValue.class::isInstance)
                                                                                           .map(FunctionParameterValue.class::cast)
                                                                                           .findFirst();
            closestFunctionalParameterVal.ifPresent(fvp -> {
                context.getMethodTextBuilder()
                       .append(getNFourSpaceIndent(closestContainerIndex + 1))
                       .append("return this.")
                       .append(new StringJoiner("", "<", ">").add(context.getReturnTypeVariables()
                                                                         .stream()
                                                                         .collect(Collectors.joining(", ")))
                                                             .toString())
                       .append(wrapCallReturnValue.getWrapMethodName())
                       .append(wrapCallReturnValue.getMethodSuffix())
                       .append("(")
                       .append(fvp.getInputParameterName())
                       .append(fvp.getInputParameterSuffix())
                       .append(");\n");
            });
        }
    }

    private static String getTypeVariableForContainerAndTypeVariableIndex(final ArrayNode containerParams,
                                                                          final int containerIndex,
                                                                          final int typeVariableIndex) {
        return Optional.ofNullable(containerParams)
                       .map(jn -> jn.get(containerIndex))
                       .map(jn -> jn.get("type_variables"))
                       .filter(ArrayNode.class::isInstance)
                       .map(ArrayNode.class::cast)
                       .map(an -> an.get(typeVariableIndex))
                       .map(jn -> jn.asText(""))
                       .orElseThrow(() -> new IllegalArgumentException("type_variables json_node is not an array_node"));
    }

    private static String getNFourSpaceIndent(final int numberOfIndents) {
        return Stream.generate(() -> " ")
                     .limit(numberOfIndents * 4L)
                     .collect(Collectors.joining());
    }

}
