package funcify;

import funcify.commandline.PathConverter;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.ConjunctEnsembleTypeTemplate;
import funcify.ensemble.basetype.DisjunctEnsembleTypeTemplate;
import funcify.ensemble.basetype.EnsembleTypesTemplate;
import funcify.ensemble.function.ConsumerFunctionTypeTemplate;
import funcify.ensemble.function.ErrableConsumerFunctionTypeTemplate;
import funcify.ensemble.function.ErrableFunctionTypeTemplate;
import funcify.ensemble.function.FunctionTypeTemplate;
import funcify.ensemble.trait.design.flattenable.FlattenableConjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.flattenable.FlattenableDisjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.mappable.MappableConjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.mappable.MappableDisjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.traversable.TraversableConjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.traversable.TraversableDisjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.zippable.ZippableConjunctDesignTypeTemplate;
import funcify.ensemble.trait.design.zippable.ZippableDisjunctDesignTypeTemplate;
import funcify.ensemble.trait.factory.flattenable.FlattenableConjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.flattenable.FlattenableDisjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.mappable.MappableConjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.mappable.MappableDisjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.traversable.TraversableConjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.traversable.TraversableDisjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.wrappable.WrappableConjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.wrappable.WrappableDisjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.zippable.ZippableConjunctFactoryTypeTemplate;
import funcify.ensemble.trait.factory.zippable.ZippableDisjunctFactoryTypeTemplate;
import funcify.file.JavaSourceFile;
import funcify.session.TypeGenerationSession;
import funcify.template.TypeGenerationTemplate;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.writer.StringTemplateWriter;
import funcify.writer.StringTemplateWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author smccarron
 * @created 2021-05-19
 */
public class FuncifyClassGenerator implements Callable<TypeGenerationSession<?, ?>> {

    private static final Logger logger = LoggerFactory.getLogger(FuncifyClassGenerator.class);

    @Option(names = {"-d",
                     "--destination-dir"}, description = "directory where the generated funcify packages and classes should be placed", defaultValue = ".", converter = PathConverter.class)
    private Path destinationDirectory;

    @Option(names = {"-p",
                     "--print-to-console"}, description = "print classes to console instead of generating java source files for classes")
    private boolean printToConsole;

    @Option(names = {"-l",
                     "--limit"}, description = "limit for number of value parameters to consider in funcify ensembles and subtypes generated", defaultValue = "20")
    private int valueParameterLimit;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FuncifyClassGenerator()).execute(args);
        System.exit(exitCode);
    }

    private static <V, R> SyncList<TypeGenerationTemplate<V, R>> typeGenerationTemplateSequence() {
        return SyncList.of(EnsembleTypesTemplate.of(),
                           ConjunctEnsembleTypeTemplate.of(),
                           DisjunctEnsembleTypeTemplate.of(),
                           WrappableDisjunctFactoryTypeTemplate.of(),
                           WrappableConjunctFactoryTypeTemplate.of(),
                           MappableConjunctFactoryTypeTemplate.of(),
                           MappableDisjunctFactoryTypeTemplate.of(),
                           FlattenableConjunctFactoryTypeTemplate.of(),
                           FlattenableDisjunctFactoryTypeTemplate.of(),
                           FunctionTypeTemplate.of(),
                           ErrableFunctionTypeTemplate.of(),
                           ConsumerFunctionTypeTemplate.of(),
                           ErrableConsumerFunctionTypeTemplate.of(),
                           ZippableDisjunctFactoryTypeTemplate.of(),
                           ZippableConjunctFactoryTypeTemplate.of(),
                           TraversableConjunctFactoryTypeTemplate.of(),
                           TraversableDisjunctFactoryTypeTemplate.of(),
                           FlattenableConjunctDesignTypeTemplate.of(),
                           FlattenableDisjunctDesignTypeTemplate.of(),
                           MappableConjunctDesignTypeTemplate.of(),
                           MappableDisjunctDesignTypeTemplate.of(),
                           TraversableConjunctDesignTypeTemplate.of(),
                           TraversableDisjunctDesignTypeTemplate.of(),
                           ZippableConjunctDesignTypeTemplate.of(),
                           ZippableDisjunctDesignTypeTemplate.of());
    }

    private static <V, R> TypeGenerationSession<V, R> applyEachTemplateToSession(final TypeGenerationSession<V, R> session) {
        return FuncifyClassGenerator.<V, R>typeGenerationTemplateSequence().foldLeft(session, (s, template) -> {
            return template.createTypesForSession(s);
        });
    }

    @Override
    public TypeGenerationSession<?, ?> call() throws Exception {
        logger.info("call: [ {} ]",
                    SyncMap.empty()
                           .put("destinationDirectory", destinationDirectory)
                           .put("printToConsole", printToConsole)
                           .put("valueParameterLimit", valueParameterLimit)
                           .mkString());
        if (printToConsole) {
            final StringTemplateWriter<String, Void> consoleWriter = StringTemplateWriterFactory.createStringTemplateConsoleWriter();
            final TypeGenerationSession<String, Void> session = buildInitialGenerationSession(consoleWriter);
            return applyEachTemplateToSession(session);
        } else {
            final StringTemplateWriter<File, JavaSourceFile> javaSourceFileWriter = StringTemplateWriterFactory.createStringTemplateJavaSourceFileWriter();
            final TypeGenerationSession<File, JavaSourceFile> session = buildInitialGenerationSession(javaSourceFileWriter);
            return applyEachTemplateToSession(session);
        }
    }

    private <V, R> TypeGenerationSession<V, R> buildInitialGenerationSession(final StringTemplateWriter<V, R> templateWriter) {

        return TypeGenerationSession.<V, R>builder()
                                    .destinationDirectoryPath(destinationDirectory)
                                    .ensembleKinds(SyncList.of(EnsembleKind.values())
                                                           .filter(ek -> (valueParameterLimit >= 1 &&
                                                                          ek.getNumberOfValueParameters() <=
                                                                          valueParameterLimit) || valueParameterLimit <= 0))
                                    .templateWriter(templateWriter)
                                    .build();
    }


}
