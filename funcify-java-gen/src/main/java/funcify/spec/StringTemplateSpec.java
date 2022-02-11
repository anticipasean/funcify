package funcify.spec;

import funcify.error.FuncifyCodeGenException;
import funcify.tool.LiftOps;
import funcify.tool.container.SyncMap;
import funcify.tool.container.SyncMap.Tuple2;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.stringtemplate.v4.ModelAdaptor;
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
            return new STGroupFile(getStringTemplateGroupFilePath().toString(), getCharacterEncoding().displayName());
        } catch (final Throwable t) {
            throw new FuncifyCodeGenException(t.getMessage(), t);

        }
    }

    Path getDestinationParentDirectoryPath();

    default Path getDestinationPackageDirectoryPath() {
        final Supplier<IllegalArgumentException> invalidTypePackageErrorSupplier = () -> {
            final String message = String.format("type_package [ %s ] is empty or invalid and cannot be converted into a path",
                                                 String.join(".", getTypePackagePathSegments()));
            return new IllegalArgumentException(message);
        };
        final Path typePackagePath = getTypePackageAsFilePath().orElseThrow(invalidTypePackageErrorSupplier);
        return getDestinationParentDirectoryPath().resolve(typePackagePath);
    }

    default Path getDestinationFilePath() {
        return getDestinationPackageDirectoryPath().resolve(getTypeName() + getFileTypeExtension());
    }

    default boolean stringTemplateGroupFileExists() {
        return LiftOps.tryCatchLift(this::getStringTemplateGroupFile)
                      .isPresent();
    }

    SyncMap<Class<?>, ModelAdaptor<?>> getModelAdapters();

    String getTemplateFunctionName();

    SyncMap<String, Object> getTemplateFunctionParameterInput();

    default ST getStringTemplate() {
        try {
            final STGroupFile stGroupFile = getStringTemplateGroupFile();
            final Function<Tuple2<Class<?>, ModelAdaptor<?>>, FuncifyCodeGenException> modelAdaptorRegistryErrorFunc = tup -> {
                return new FuncifyCodeGenException(String.format(
                    "error occurred when attempting to register model adapter [ cls: %s, adaptor: %s ]",
                    tup._1()
                       .getSimpleName(),
                    tup._2()
                       .getClass()
                       .getSimpleName()));
            };
            getModelAdapters().foldLeft(stGroupFile, (f, tup) -> {
                return LiftOps.tryCatchLift(() -> typedModelAdaptorRegistrar(tup).apply(f))
                              .orElseThrow(() -> modelAdaptorRegistryErrorFunc.apply(tup));
            });
            return getTemplateFunctionParameterInput().foldLeft(Objects.requireNonNull(stGroupFile.getInstanceOf(
                getTemplateFunctionName()), () -> "template_function_name was not found"),
                                                                (st, entry) -> st.add(entry._1(), entry._2()));
        } catch (final Throwable t) {
            if (t instanceof FuncifyCodeGenException) {
                throw ((FuncifyCodeGenException) t);
            } else {
                throw new FuncifyCodeGenException(t.getMessage(), t);
            }
        }
    }

    static <T> UnaryOperator<STGroupFile> typedModelAdaptorRegistrar(final Tuple2<Class<?>, ModelAdaptor<?>> tuple) {
        @SuppressWarnings("unchecked") final Class<T> cls = (Class<T>) tuple._1();
        @SuppressWarnings("unchecked") final ModelAdaptor<T> modelAdaptor = (ModelAdaptor<T>) tuple._2();
        return stGroupFile -> {
            stGroupFile.registerModelAdaptor(cls, modelAdaptor);
            return stGroupFile;
        };
    }

}