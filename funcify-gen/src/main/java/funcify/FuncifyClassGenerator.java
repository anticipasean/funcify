package funcify;

import funcify.commandline.PathConverter;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
            fallbackValue = "22")
    private int valueParameterLimit;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FuncifyClassGenerator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public File call() throws Exception {
        final GenerationSession generationSession = buildInitialGenerationSession();
        return null;
    }

    private GenerationSession buildInitialGenerationSession() {
        return GenerationSession.builder()
                                .destinationDirectoryPath(destinationDirectory)
                                .ensembleKinds(Stream.of(EnsembleKind.values())
                                                     .filter(ek -> (valueParameterLimit >= 1
                                                         && ek.getNumberOfValueParameters() <= valueParameterLimit)
                                                         || valueParameterLimit <= 0)
                                                     .collect(Collectors.toList()))
                                .build();
    }
}
