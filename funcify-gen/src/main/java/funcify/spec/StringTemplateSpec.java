package funcify.spec;

import funcify.error.FuncifyCodeGenException;
import funcify.tool.LiftOps;
import funcify.tool.container.SyncMap;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * @author smccarron
 * @created 2021-08-29
 */
public interface StringTemplateSpec {

    default Charset getCharacterEncoding() {
        return StandardCharsets.UTF_8;
    }

    List<String> getTypePackagePathSegments();

    String getTypeName();

    String getFileTypeExtension();

    default Optional<Path> getTypePackageAsFilePath() {
        return Optional.ofNullable(getTypePackagePathSegments())
                       .filter(l -> l.size() > 0)
                       .flatMap(l -> {
                           try {
                               return l.size() == 1 ? Optional.of(Paths.get(l.get(0))) : Optional.of(Paths.get(l.get(0),
                                                                                                               l.stream()
                                                                                                                .skip(1)
                                                                                                                .toArray(String[]::new)));
                           } catch (final Throwable t) {
                               return Optional.empty();
                           }
                       });
    }

    default String getStringTemplateGroupFileName() {
        return getStringTemplateGroupFilePath().getFileName()
                                               .toString();
    }

    Path getStringTemplateGroupFilePath();

    default STGroupFile getStringTemplateGroupFile() {
        try {
            return new STGroupFile(getStringTemplateGroupFilePath().toString(),
                                   getCharacterEncoding().displayName());
        } catch (final Throwable t) {
            throw new FuncifyCodeGenException(t.getMessage(),
                                              t);

        }
    }

    Path getDestinationParentDirectoryPath();

    default Path getDestinationPackageDirectoryPath() {
        final Path typePackagePath = getTypePackageAsFilePath().orElseThrow(() -> new IllegalArgumentException(String.format("type_package [ %s ] is empty or invalid and cannot be converted into a path",
                                                                                                                             String.join(".",
                                                                                                                                         getTypePackagePathSegments()))));
        return getDestinationParentDirectoryPath().resolve(typePackagePath);
    }

    default Path getDestinationFilePath() {
        return getDestinationPackageDirectoryPath().resolve(getTypeName() + getFileTypeExtension());
    }

    //    default Path getStringTemplateGroupFilePath() {
    //        return LiftOps.tryCatchLift(() -> URI.create("file://" + System.getProperty("user.dir")))
    //                      .flatMap(LiftOps.<URI, Path>tryCatchLift(Paths::get))
    //                      .flatMap(LiftOps.<Path, Path>tryCatchLift(p -> p.resolve(getStringTemplateGroupFilePath())))
    //                      .flatMap(p -> LiftOps.tryCatchLift(Path::toFile)
    //                                           .andThen(opt -> opt.filter(LiftOps.tryCatchLift(File::exists))
    //                                                              .map(f -> p))
    //                                           .apply(p))
    //                      .orElseThrow(() -> new FuncifyCodeGenException(String.format("unable to find or get string template group file path instance at [ %s ]",
    //                                                                                   LiftOps.tryCatchLift(() -> Paths.get(URI.create(
    //                                                                                       "file://"
    //                                                                                           + System.getProperty("user.dir")))
    //                                                                                                                   .resolve(getStringTemplateGroupFilePath())
    //                                                                                                                   .toString())
    //                                                                                          .orElse("null"))));
    //    }

    default boolean stringTemplateGroupFileExists() {
        return LiftOps.tryCatchLift(this::getStringTemplateGroupFile)
                      .isPresent();
    }

    String getTemplateFunctionName();

    SyncMap<String, Object> getTemplateFunctionParameterInput();

    default ST getStringTemplate() {
        final STGroupFile stGroupFile = getStringTemplateGroupFile();
        return getTemplateFunctionParameterInput().foldLeft(stGroupFile.getInstanceOf(getTemplateFunctionName()),
                                                            (st, entry) -> st.add(entry._1(),
                                                                                  entry._2()));
    }

}
