package funcify.ensemble.basetype.template;

import funcify.TypeTemplate;
import funcify.ensemble.EnsembleKind;
import funcify.ensemble.basetype.session.DefaultTypeGenerationSession;
import funcify.ensemble.basetype.session.TypeGenerationSession;
import funcify.error.FuncifyCodeGenException;
import funcify.file.JavaSourceFile;
import funcify.tool.CharacterOps;
import funcify.tool.container.SyncMap;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.STMessage;

/**
 * @author smccarron
 * @created 2021-08-28
 */
public class EnsembleTypesTemplate implements TypeTemplate {

    private static STErrorListener getStringTemplateErrorListener(final AtomicReference<STMessage> messageHolder) {
        return new STErrorListener() {
            @Override
            public void compileTimeError(final STMessage msg) {
                messageHolder.compareAndSet(null,
                                            msg);
            }

            @Override
            public void runTimeError(final STMessage msg) {
                messageHolder.compareAndSet(null,
                                            msg);
            }

            @Override
            public void IOError(final STMessage msg) {
                messageHolder.compareAndSet(null,
                                            msg);
            }

            @Override
            public void internalError(final STMessage msg) {
                messageHolder.compareAndSet(null,
                                            msg);
            }
        };
    }

    @Override
    public List<String> getDestinationTypePackagePathSegments() {
        return Arrays.asList("funcify",
                             "ensemble");
    }

    @Override
    public String getStringTemplateGroupFileName() {
        return "ensemble.stg";
    }

    @Override
    public String getStringTemplateGroupFilePathString() {
        return "src/main/antlr/funcify/ensemble.stg";
    }

    @Override
    public TypeGenerationSession createTypesForSession(final TypeGenerationSession session) {
        final AtomicReference<STMessage> messageHolder = new AtomicReference<>();
        final STErrorListener stErrorListener = getStringTemplateErrorListener(messageHolder);
        final SyncMap<EnsembleKind, JavaSourceFile> sourceFileByEnsembleKind = SyncMap.empty();
        try {
            final Path destinationDirectoryPath = session.getDestinationDirectoryPath();
            final Optional<Path> destinationTypePackageAsPath = getDestinationTypePackageAsPath();
            final Path resolvedDestinationTypePackagePath = destinationDirectoryPath.resolve(destinationTypePackageAsPath.orElseThrow(() -> new IllegalArgumentException("path is empty or invalid")));
            final STGroupFile stringTemplateGroupFile = getStringTemplateGroupFile();
            final Path ensembleBaseTypePath = resolvedDestinationTypePackagePath.resolve("Ensemble.java");
            final File baseEnsembleTypeSourceFile = Files.createFile(ensembleBaseTypePath)
                                                         .toFile();
            stringTemplateGroupFile.getInstanceOf("ensemble_base_type")
                                   .write(baseEnsembleTypeSourceFile,
                                          stErrorListener,
                                          getCharacterEncoding().displayName());
            final TypeGenerationSession updatedSession = DefaultTypeGenerationSession.builder()
                                                                                     .ensembleKinds(session.getEnsembleKinds())
                                                                                     .ensembleTypeSourceFilesByEnsembleKind(sourceFileByEnsembleKind)
                                                                                     .baseEnsembleTypeSourceFile(JavaSourceFile.of(getDestinationTypePackagePathSegments(),
                                                                                                                                   baseEnsembleTypeSourceFile))
                                                                                     .destinationDirectoryPath(session.getDestinationDirectoryPath())
                                                                                     .build();
            for (EnsembleKind ek : session.getEnsembleKinds()) {
                ST stringTemplate = SyncMap.of("package",
                                               getDestinationTypePackagePathSegments(),
                                               "class_name",
                                               ek.getSimpleClassName(),
                                               "is_solo",
                                               ek == EnsembleKind.SOLO,
                                               "type_variables",
                                               CharacterOps.firstNUppercaseLettersWithNumericIndexExtension(ek.getNumberOfValueParameters())
                                                           .collect(Collectors.toList()),
                                               "next_type_variable",
                                               CharacterOps.uppercaseLetterByIndexWithNumericExtension(ek.getNumberOfValueParameters())
                                                           .orElse(null))
                                           .foldLeft(stringTemplateGroupFile.getInstanceOf("ensemble_type"),
                                                     (st, entry) -> st.add(entry._1(),
                                                                           entry._2()));
                final Path resolvedTypePath = resolvedDestinationTypePackagePath.resolve(ek.getSimpleClassName() + ".java");
                final File ensembleTypeSourceFile = Files.createFile(resolvedTypePath)
                                                         .toFile();
                stringTemplate.write(ensembleTypeSourceFile,
                                     stErrorListener,
                                     getCharacterEncoding().displayName());
                sourceFileByEnsembleKind.put(ek,
                                             JavaSourceFile.of(getDestinationTypePackagePathSegments(),
                                                               ensembleTypeSourceFile));
            }
            return updatedSession;
        } catch (final Throwable t) {
            if (messageHolder.get() != null) {
                throw new FuncifyCodeGenException(new StringBuilder().append("message: ")
                                                                     .append(messageHolder.get().error.message)
                                                                     .append(", ")
                                                                     .append("cause: ")
                                                                     .append(messageHolder.get().cause)
                                                                     .toString(),
                                                  t);
            } else {
                throw new FuncifyCodeGenException(t.getMessage(),
                                                  t);
            }
        }
    }
}
