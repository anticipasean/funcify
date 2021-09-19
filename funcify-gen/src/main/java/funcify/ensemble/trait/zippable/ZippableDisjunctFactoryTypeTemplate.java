package funcify.ensemble.trait.zippable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
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

    private JsonNode createZipImplementationSequences(final int zipMethodIndex, final int numberOfTypeParameters) {
        if (zipMethodIndex <= 0) {
            return JsonNodeFactory.instance.objectNode();
        }
        final Spliterator<String> givenTypeVars = CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(
            numberOfTypeParameters)
                                                              .spliterator();
        final Spliterator<String> otherTypeVars = IntStream.range(numberOfTypeParameters,
                                                                  (numberOfTypeParameters * (zipMethodIndex + 1)))
                                                           .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                                           .flatMap(opt -> opt.map(Stream::of)
                                                                              .orElseGet(Stream::empty))
                                                           .spliterator();
        final Spliterator<String> nextTypeVars = IntStream.range(numberOfTypeParameters * (zipMethodIndex + 1),
                                                                 (numberOfTypeParameters * (zipMethodIndex + 1))
                                                                 + numberOfTypeParameters)
                                                          .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                                          .flatMap(opt -> opt.map(Stream::of)
                                                                             .orElseGet(Stream::empty))
                                                          .spliterator();
        final ArrayNode givenTypeVarsNode = JsonNodeFactory.instance.arrayNode();
        final ArrayNode nextTypeVarsNode = JsonNodeFactory.instance.arrayNode();
        final ArrayNode otherTypeVarsNode = JsonNodeFactory.instance.arrayNode();
        while (givenTypeVars.tryAdvance(s -> givenTypeVarsNode.add(s)) && nextTypeVars.tryAdvance(s -> nextTypeVarsNode.add(s))) {

        }
        for (int i = 0; i < zipMethodIndex; i++) {
            final ArrayNode otherNode = JsonNodeFactory.instance.arrayNode();
            for (int j = 0; j < numberOfTypeParameters; j++) {
                otherTypeVars.tryAdvance(s -> otherNode.add(s));
            }
            otherTypeVarsNode.add(otherNode);
        }
        final ArrayNode functionTypeVarsNode = IntStream.range(0, numberOfTypeParameters)
                                                        .mapToObj(i -> {
                                                            final Spliterator<JsonNode> spliter = otherTypeVarsNode.spliterator();
                                                            final ArrayNode accumNode = JsonNodeFactory.instance.arrayNode()
                                                                                                                .add(
                                                                                                                    givenTypeVarsNode.get(
                                                                                                                        i));
                                                            while (spliter.tryAdvance(jn -> accumNode.add(jn.get(i)))) {
                                                            }
                                                            return accumNode.add(nextTypeVarsNode.get(i));
                                                        })
                                                        .reduce(JsonNodeFactory.instance.arrayNode(),
                                                                ArrayNode::add,
                                                                ArrayNode::addAll);
        return SyncMap.of("given", givenTypeVarsNode)
                      .put("other", otherTypeVarsNode)
                      .put("next", nextTypeVarsNode)
                      .put("function", functionTypeVarsNode)
                      .foldLeft(JsonNodeFactory.instance.objectNode(), (objNode, tup) -> objNode.set(tup._1(), tup._2()));
    }


}
