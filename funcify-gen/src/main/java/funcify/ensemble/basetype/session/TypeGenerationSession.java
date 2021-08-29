package funcify.ensemble.basetype.session;

import funcify.ensemble.EnsembleKind;
import funcify.file.JavaSourceFile;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import java.nio.file.Path;

/**
 * @author smccarron
 * @created 2021-05-29
 */
public interface TypeGenerationSession {

    Path getDestinationDirectoryPath();

    SyncList<EnsembleKind> getEnsembleKinds();

    JavaSourceFile getBaseEnsembleTypeSourceFile();

    SyncMap<EnsembleKind, JavaSourceFile> getEnsembleTypeSourceFilesByEnsembleKind();
}
