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
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(staticName = "of")
public class ZippableConjunctFactoryTypeTemplate<V, R> implements TraitFactoryGenerationTemplate<V, R> {

    private static final Logger logger = LoggerFactory.getLogger(ZippableConjunctFactoryTypeTemplate.class);

    @Override
    public Set<Trait> getTraits() {
        return EnumSet.of(Trait.CONJUNCT, Trait.ZIPPABLE);
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify", "trait", "zippable", "conjunct");
    }

    @Override
    public Path getStringTemplateGroupFilePath() {
        return Paths.get("antlr", "funcify", "zippable_conjunct_factory_type.stg");
    }

    @Override
    public TypeGenerationSession<V, R> createTypesForSession(final TypeGenerationSession<V, R> session) {
        logger.debug("create_types_for_session: [ {} ]",
                     SyncMap.empty()
                            .put("types", "ZippableConjunctEnsembleFactory[1..n]"));
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
                                                                                              Trait.CONJUNCT,
                                                                                              Trait.WRAPPABLE))
                                                              .put("container_type",
                                                                   getContainerTypeJsonInstanceFor(ek, Trait.CONJUNCT))
                                                              .put("zip_impl_sequences",
                                                                   createZipImplementationSequences(ek.getNumberOfValueParameters()));
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
            return session.withConjunctZippableEnsembleFactoryTypeResults(results);
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
        return SyncMap.<String, JsonNode>of("method_name", JsonNodeFactory.instance.textNode("zip" + zipMethodIndex))
                      .put("container_params", determineContainerParametersForZipMethod(zipMethodIndex, numberOfTypeParameters))
                      .put("new_type_variables", determineNewTypeVariablesForZipMethod(zipMethodIndex, numberOfTypeParameters))
                      .put("return_type_variables",
                           determineReturnTypeVariablesForZipMethod(zipMethodIndex, numberOfTypeParameters))
                      .put("zipper_function_params",
                           determineFunctionTypeParametersForZipperMethodParameter(zipMethodIndex, numberOfTypeParameters))
                      .foldLeft(JsonNodeFactory.instance.objectNode(), (on, tup) -> on.set(tup._1(), tup._2()));
    }

    private JsonNode determineFunctionTypeParametersForZipperMethodParameter(final int zipMethodIndex,
                                                                             final int numberOfTypeParameters) {

        final List<Iterator<String>> iterators = IntStream.range(0, zipMethodIndex + 2)
                                                          .mapToObj(zi -> {
                                                              return IntStream.range(numberOfTypeParameters * zi,
                                                                                     numberOfTypeParameters * (zi + 1))
                                                                              .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                                                              .iterator();
                                                          })
                                                          .collect(Collectors.toList());
        return IntStream.range(0, numberOfTypeParameters)
                        .mapToObj(i -> {
                            final ArrayNode functionParamSet = JsonNodeFactory.instance.arrayNode(numberOfTypeParameters + 1);
                            for (Iterator<String> iter : iterators) {
                                if (iter.hasNext()) {
                                    functionParamSet.add(iter.next());
                                }
                            }
                            return functionParamSet;
                        })
                        .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);

    }

    private JsonNode determineReturnTypeVariablesForZipMethod(final int zipMethodIndex,
                                                              final int numberOfTypeParameters) {
        return IntStream.range((numberOfTypeParameters * (1 + zipMethodIndex)), (numberOfTypeParameters * (2 + zipMethodIndex)))
                        .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                        .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
    }

    private JsonNode determineNewTypeVariablesForZipMethod(final int zipMethodIndex,
                                                           final int numberOfTypeParameters) {
        return IntStream.range(numberOfTypeParameters, numberOfTypeParameters * (zipMethodIndex + Math.max(2, numberOfTypeParameters)))
                        .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                        .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
    }

    private ArrayNode determineContainerParametersForZipMethod(final int zipMethodIndex,
                                                               final int numberOfTypeParameters) {
        return IntStream.range(1, Math.max(2, zipMethodIndex + 2))
                        .mapToObj(zi -> {
                            final ObjectNode containerParam = JsonNodeFactory.instance.objectNode();
                            containerParam.put("index", zi);
                            containerParam.set("type_variables", IntStream.range((numberOfTypeParameters * (zi - 1)),
                                                                                 (numberOfTypeParameters * (zi - 1))
                                                                                 + numberOfTypeParameters)
                                                                          .mapToObj(CharacterOps::uppercaseLetterByIndexWithNumericExtension)
                                                                          .reduce(JsonNodeFactory.instance.arrayNode(),
                                                                                  ArrayNode::add,
                                                                                  ArrayNode::addAll));
                            return containerParam;
                        })
                        .reduce(JsonNodeFactory.instance.arrayNode(), ArrayNode::add, ArrayNode::addAll);
    }

}
