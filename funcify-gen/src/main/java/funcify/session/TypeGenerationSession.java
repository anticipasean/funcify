package funcify.session;

import funcify.ensemble.EnsembleKind;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import funcify.writer.WriteResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.With;

import java.nio.file.Path;

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
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctEnsembleTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctEnsembleTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctWrappableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctWrappableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> functionTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> errableFunctionTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> consumerFunctionTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> errableConsumerFunctionTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctMappableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctMappableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctFlattenableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctFlattenableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctZippableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctZippableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctTraversableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctTraversableEnsembleFactoryTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctWrappableDesignTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctWrappableDesignTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> conjunctFlattenableDesignTypeResults = SyncMap.empty();

    @Default
    private final SyncMap<EnsembleKind, WriteResult<R>> disjunctFlattenableDesignTypeResults = SyncMap.empty();

}
