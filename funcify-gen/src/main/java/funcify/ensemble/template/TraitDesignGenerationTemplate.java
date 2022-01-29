package funcify.ensemble.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import funcify.ensemble.EnsembleKind;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-09-11
 */
public interface TraitDesignGenerationTemplate<V, R> extends TraitGenerationTemplate<V, R> {

    default JsonNode getContainerTypeJsonInstanceFor(final EnsembleKind ek, final Trait... traits) {
        return SyncMap.of("type_name",
                          Trait.generateTraitNameFrom(ek, traits),
                          "type_package",
                          Stream.concat(Stream.of("funcify", "ensemble"),
                                        Stream.of(traits)
                                              .sorted(Trait::relativeTo)
                                              .map(Trait::name)
                                              .map(String::toLowerCase))
                                .collect(Collectors.joining(".")))
                      .foldLeft(JsonNodeFactory.instance.objectNode(), (objNode, tup) -> {
                          return (ObjectNode) objNode.set(tup._1(), JsonNodeFactory.instance.textNode(tup._2()));
                      });
    }

    @Override
    default JsonNode getImplementedTypeInstance(final EnsembleKind ek, final Trait... traits) {
        return SyncMap.of("type_name",
                          Trait.generateTraitNameFrom(ek, traits),
                          "type_package",
                          Stream.concat(Stream.of("funcify", "ensemble"),
                                        Stream.of(traits)
                                              .sorted(Trait::relativeTo)
                                              .map(Trait::name)
                                              .map(String::toLowerCase))
                                .collect(Collectors.joining(".")))
                      .foldLeft(JsonNodeFactory.instance.objectNode(), (objNode, tup) -> {
                          return (ObjectNode) objNode.set(tup._1(), JsonNodeFactory.instance.textNode(tup._2()));
                      });
    }
}
