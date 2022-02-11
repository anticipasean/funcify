package funcify.ensemble.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import funcify.ensemble.EnsembleKind;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.container.SyncMap;
import funcify.trait.Trait;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public interface TraitGenerationTemplate<V, R> extends TypeGenerationTemplate<V, R> {

    Set<Trait> getTraits();

    default String getTraitNameForEnsembleKind(final EnsembleKind ek) {
        return Trait.generateTraitNameFrom(ek, getTraits());
    }

    default JsonNode getImplementedTypeInstance(final EnsembleKind ek, final Trait... traits) {
        return SyncMap.of("type_name",
                          Trait.generateTraitNameFrom(ek, traits),
                          "type_package",
                          Stream.concat(Stream.of("funcify", "trait"),
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
