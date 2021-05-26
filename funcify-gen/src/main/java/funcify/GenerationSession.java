package funcify;

import funcify.template.Trait;
import funcify.typedef.JavaTypeDefinition;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.ToString;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-20
 */
@AllArgsConstructor
@Builder
@With
@Getter
@ToString
public class GenerationSession {

    private final Path destinationDirectoryPath;

    @Default
    private final List<EnsembleKind> ensembleKinds = Collections.emptyList();

    private final JavaTypeDefinition baseEnsembleInterfaceTypeDefinition;

    @Default
    private final Map<EnsembleKind, JavaTypeDefinition> ensembleInterfaceTypeDefinitionsByEnsembleKind = new HashMap<>();

    @Default
    private final List<Trait> traits = Collections.emptyList();

}
