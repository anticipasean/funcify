package funcify.file;

import funcify.tool.LiftOps;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A java source file with a name ending in extension ".java"
 *
 * @author smccarron
 * @created 2021-08-28
 */
public interface JavaSourceFile {

    static JavaSourceFile of(final List<String> packagePathSegments,
                             final String className) {
        Objects.requireNonNull(packagePathSegments,
                               () -> "packagePathSegments");
        if (packagePathSegments.size() == 0) {
            throw new IllegalArgumentException("must specify the parts of the package name");
        }
        Objects.requireNonNull(className,
                               () -> "className");
        return JavaSourceFileImpl.of(packagePathSegments,
                                     className,
                                     Optional.<File>empty());
    }

    static JavaSourceFile of(final List<String> packagePathSegments,
                             final File sourceFile) {
        Objects.requireNonNull(packagePathSegments,
                               () -> "packagePathSegments");
        if (packagePathSegments.size() == 0) {
            throw new IllegalArgumentException("must specify the parts of the package name");
        }
        Objects.requireNonNull(sourceFile,
                               () -> "sourceFile");
        if (!sourceFile.getName()
                       .endsWith(".java")) {
            throw new IllegalArgumentException("not a java source file; does not end in extension .java");
        }
        return JavaSourceFileImpl.of(packagePathSegments,
                                     sourceFile.getName()
                                               .replaceAll("\\.java$",
                                                           ""),
                                     Optional.of(sourceFile));
    }

    static JavaSourceFile updateWithFile(final JavaSourceFile javaSourceFile,
                                         final File sourceFile) {
        Objects.requireNonNull(javaSourceFile,
                               () -> "javaSourceFile");
        Objects.requireNonNull(sourceFile,
                               () -> "sourceFile");
        if (sourceFile.getName()
                      .replaceAll("\\.java$",
                                  "")
                      .equals(javaSourceFile.getClassName())) {
            throw new IllegalArgumentException(String.format("java_source_file.class_name does not match source file name: [ expected: %s, actual: %s ]",
                                                             sourceFile.getName()
                                                                       .replaceAll("\\.java$",
                                                                                   ""),
                                                             javaSourceFile.getClassName()));
        }
        return of(javaSourceFile.getPackageNameAsPathSegments(),
                  sourceFile);
    }

    default String getExtension() {
        return ".java";
    }

    default String getPackageName() {
        return String.join(".",
                           getPackageNameAsPathSegments());
    }

    List<String> getPackageNameAsPathSegments();

    default Optional<Path> getPackageNameAsPath() {
        return Optional.ofNullable(getPackageNameAsPathSegments())
                       .filter(l -> l.size() > 0)
                       .flatMap(l -> {
                           return Optional.of(l)
                                          .filter(ps -> ps.size() > 1)
                                          .map(LiftOps.<List<String>, Path>tryCatchLift(ps -> Paths.get(ps.get(0),
                                                                                                        ps.stream()
                                                                                                          .skip(1)
                                                                                                          .toArray(String[]::new))))
                                          .orElseGet(() -> Optional.of(l)
                                                                   .flatMap(LiftOps.<List<String>, Path>tryCatchLift(ps -> Paths.get(ps.get(0)))));
                       });
    }

    default Optional<Path> resolvePackagePathWithDirectoryPath(final Path directoryPath) {
        Objects.requireNonNull(directoryPath,
                               () -> "directoryPath");
        final Optional<File> dirPathAsFile = LiftOps.tryCatchLift(directoryPath::toFile);
        if (dirPathAsFile.filter(LiftOps.<File>tryCatchLift(f -> f.exists() && f.isDirectory()))
                         .isPresent()) {
            return getPackageNameAsPath().flatMap(LiftOps.<Path, Path>tryCatchLift(directoryPath::resolve));
        }
        return Optional.empty();
    }

    String getClassName();

    default String getFileName() {
        return getClassName() + getExtension();
    }

    default boolean exists() {
        return getFile().filter(LiftOps.<File>tryCatchLift(File::exists))
                        .isPresent();
    }

    Optional<File> getFile();

    default Optional<Path> getFullPath() {
        return getFile().flatMap(LiftOps.tryCatchLift(File::toPath));
    }

    default JavaSourceFile withFile(final File sourceFile) {
        return updateWithFile(this,
                              sourceFile);
    }
}
