package funcify.ensemble.basetype.session;

import funcify.ensemble.EnsembleKind;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-31
 */
@AllArgsConstructor
@Builder
@Getter
@With
public class TypeGenerationSession<V, R> {

    private final Path destinationDirectoryPath;

    private final StringTemplateWriter<V, R> templateWriter;

    @Default
    private final SyncList<EnsembleKind> ensembleKinds = SyncList.empty();

    private final WriteResult<R> baseEnsembleTypeResult;

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> ensembleTypeResultsByEnsembleKind = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctWrappableEnsembleTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctWrappableEnsembleTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> functionTypeResults = SyncMap.empty();

}
