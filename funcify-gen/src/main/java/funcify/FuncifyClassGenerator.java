package funcify;

import funcify.commandline.PathConverter;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.TypeGenerationSession;
import funcify.ensemble.basetype.template.EnsembleTypesTemplate;
import funcify.ensemble.function.FunctionTypeTemplate;
import funcify.ensemble.trait.mappable.ConjunctMappableTypeTemplate;
import funcify.ensemble.trait.wrappable.ConjunctWrappableTypeTemplate;
import funcify.ensemble.trait.wrappable.DisjunctWrappableTypeTemplate;
import funcify.file.JavaSourceFile;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.container.SyncList;
import funcify.writer.StringTemplateWriter;
import funcify.writer.StringTemplateWriterFactory;
import funcify.writer.WriteResult;
import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public class FuncifyClassGenerator implements Callable<TypeGenerationSession<?, ?>> {

    @Option(names = {"-d", "--destination-dir"},
            description = "directory where the generated funcify packages and classes should be placed",
            defaultValue = ".",
            converter = PathConverter.class)
    private Path destinationDirectory;

    @Option(names = {"-p", "--print-to-console"},
            description = "print classes to console instead of generating java source files for classes")
    private boolean printToConsole;

    @Option(names = {"-l", "--limit"},
            description = "limit for number of value parameters to consider in funcify ensembles and subtypes generated",
            defaultValue = "22")
    private int valueParameterLimit;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FuncifyClassGenerator()).execute(args);
        System.exit(exitCode);
    }

    private static <V, R> SyncList<TypeGenerationTemplate<V, R>> typeGenerationTemplateSequence() {
        return SyncList.of(EnsembleTypesTemplate.of(),
                           DisjunctWrappableTypeTemplate.of(),
                           ConjunctWrappableTypeTemplate.of(),
                           ConjunctMappableTypeTemplate.of(),
                           FunctionTypeTemplate.of());
    }

    private static <V, R> TypeGenerationSession<V, R> applyEachTemplateToSession(final TypeGenerationSession<V, R> session) {
        return FuncifyClassGenerator.<V, R>typeGenerationTemplateSequence()
                                    .foldLeft(session,
                                              (s, template) -> {
                                                  return template.createTypesForSession(s);
                                              });
    }

    @Override
    public TypeGenerationSession<?, ?> call() throws Exception {
        if (printToConsole) {
            final StringTemplateWriter<String, Void> consoleWriter = StringTemplateWriterFactory.createStringTemplateConsoleWriter();
            final TypeGenerationSession<String, Void> session = buildInitialGenerationSession(consoleWriter);
            return applyEachTemplateToSession(session);
        } else {
            final StringTemplateWriter<File, WriteResult<JavaSourceFile>> javaSourceFileWriter = StringTemplateWriterFactory.createStringTemplateJavaSourceFileWriter();
            final TypeGenerationSession<File, WriteResult<JavaSourceFile>> session = buildInitialGenerationSession(javaSourceFileWriter);
            return applyEachTemplateToSession(session);
        }
    }

    private <V, R> TypeGenerationSession<V, R> buildInitialGenerationSession(final StringTemplateWriter<V, R> templateWriter) {

        return TypeGenerationSession.<V, R>builder()
                                    .destinationDirectoryPath(destinationDirectory)
                                    .ensembleKinds(SyncList.of(EnsembleKind.values())
                                                           .filter(ek -> (valueParameterLimit >= 1
                                                               && ek.getNumberOfValueParameters() <= valueParameterLimit)
                                                               || valueParameterLimit <= 0))
                                    .templateWriter(templateWriter)
                                    .build();
    }


}
