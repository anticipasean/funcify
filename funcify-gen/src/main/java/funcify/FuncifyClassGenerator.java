package funcify;

import funcify.commandline.PathConverter;
import funcify.ensemble.EnsembleKind;
import funcify.tool.container.SyncList;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public class FuncifyClassGenerator implements Callable<File> {

    @Option(names = {"-d", "--destination-dir"},
            description = "directory where the generated funcify packages and classes should be placed", defaultValue = ".",
            converter = PathConverter.class)
    private Path destinationDirectory;

    @Option(names = {"-l", "--limit"},
            description = "limit for number of value parameters to consider in funcify ensembles and subtypes generated",
            defaultValue = "22")
    private int valueParameterLimit;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FuncifyClassGenerator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public File call() throws Exception {
        final DefaultGenerationSession generationSession = buildInitialGenerationSession();
        return null;
    }

    private DefaultGenerationSession buildInitialGenerationSession() {
        return DefaultGenerationSession.builder()
                                       .destinationDirectoryPath(destinationDirectory)
                                       .ensembleKinds(SyncList.of(EnsembleKind.values())
                                                              .filter(ek -> (valueParameterLimit >= 1
                                                         && ek.getNumberOfValueParameters() <= valueParameterLimit)
                                                         || valueParameterLimit <= 0))
                                       .build();
    }
}
