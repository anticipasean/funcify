package funcify.writer;

import funcify.error.FuncifyCodeGenException;
import funcify.file.JavaSourceFile;
import funcify.spec.StringTemplateSpec;
import java.io.File;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * @author smccarron
 * @created 2021-08-29
 */
public interface StringTemplateWriterFactory {

    static StringTemplateWriter<File, WriteResult<File>> createStringTemplateFileWriter() {
        return createStringTemplateFileWriter((spec, f) -> WriteResult.success(f),
                                              (spec, t) -> WriteResult.failure(t));
    }

    static StringTemplateWriter<String, WriteResult<String>> createStringTemplateStringWriter() {
        return createStringTemplateStringWriter((spec, s) -> WriteResult.success(s),
                                                (spec, t) -> WriteResult.failure(t));
    }

    static <R> StringTemplateWriter<File, R> createStringTemplateFileWriter(final BiFunction<? super StringTemplateSpec, ? super File, ? extends R> successHandler,
                                                                            final BiFunction<? super StringTemplateSpec, ? super Throwable, ? extends R> failureHandler) {
        Objects.requireNonNull(successHandler,
                               () -> "successHandler");
        Objects.requireNonNull(failureHandler,
                               () -> "failureHandler");
        return StringTemplateFileWriter.of((spec, f) -> WriteResult.success(successHandler.apply(spec,
                                                                                                 f)),
                                           (spec, thr) -> {
                                               try {
                                                   return WriteResult.success(failureHandler.apply(spec,
                                                                                                   thr));
                                               } catch (final Throwable t) {
                                                   return WriteResult.failure(t);
                                               }
                                           });
    }

    static <R> StringTemplateWriter<String, R> createStringTemplateStringWriter(final BiFunction<? super StringTemplateSpec, ? super String, ? extends R> successHandler,
                                                                                final BiFunction<? super StringTemplateSpec, ? super Throwable, ? extends R> failureHandler) {
        Objects.requireNonNull(successHandler,
                               () -> "successHandler");
        Objects.requireNonNull(failureHandler,
                               () -> "failureHandler");
        return StringTemplateStringWriter.of((spec, s) -> {
                                                 try {
                                                     return WriteResult.success(successHandler.apply(spec,
                                                                                                     s));
                                                 } catch (final Throwable t) {
                                                     return WriteResult.failure(t);
                                                 }
                                             },
                                             (spec, r) -> {
                                                 try {
                                                     return WriteResult.success(failureHandler.apply(spec,
                                                                                                     r));
                                                 } catch (final Throwable t) {
                                                     return WriteResult.failure(t);
                                                 }
                                             });
    }

    static StringTemplateWriter<String, Void> createStringTemplateConsoleWriter() {
        final String boundaryLine = IntStream.range(0,
                                                    100)
                                             .mapToObj(i -> new StringBuilder("-"))
                                             .reduce(StringBuilder::append)
                                             .orElseGet(StringBuilder::new)
                                             .toString();
        return StringTemplateConsoleWriter.of((spec, resultValue) -> {
                                                  System.out.println(boundaryLine);
                                                  System.out.println("type_name: " + spec.getTypeName());
                                                  System.out.println(boundaryLine);
                                                  System.out.println(resultValue);
                                                  System.out.println(boundaryLine);
                                                  return null;
                                              },
                                              (spec, throwable) -> {
                                                  System.out.println(boundaryLine);
                                                  System.err.println(new StringBuilder().append("error occurred when processing output for [ type_name: ")
                                                                                        .append(spec.getTypeName())
                                                                                        .append(", type_template_function: ")
                                                                                        .append(spec.getTemplateFunctionName())
                                                                                        .append(" ] --> ")
                                                                                        .append("[ type: ")
                                                                                        .append(throwable.getClass()
                                                                                                         .getSimpleName())
                                                                                        .append(", message: \"")
                                                                                        .append(throwable.getMessage())
                                                                                        .append("\", cause: \"")
                                                                                        .append(String.join(": ",
                                                                                                            throwable.getCause()
                                                                                                                == null ? "null"
                                                                                                                : throwable.getCause()
                                                                                                                           .getClass()
                                                                                                                           .getSimpleName(),
                                                                                                            throwable.getCause()
                                                                                                                == null ? "null"
                                                                                                                : throwable.getCause()
                                                                                                                           .getMessage()))
                                                                                        .append("\" ]"));
                                                  System.out.println(boundaryLine);
                                                  return null;
                                              });
    }

    static StringTemplateWriter<File, JavaSourceFile> createStringTemplateJavaSourceFileWriter() {
        return createStringTemplateFileWriter((spec, f) -> JavaSourceFile.of(spec.getTypePackagePathSegments(),
                                                                             f),
                                              (spec, t) -> {
                                                  if (t instanceof RuntimeException) {
                                                      throw ((RuntimeException) t);
                                                  } else {
                                                      throw new FuncifyCodeGenException(t.getMessage(),
                                                                                        t);
                                                  }
                                              });
    }
}
