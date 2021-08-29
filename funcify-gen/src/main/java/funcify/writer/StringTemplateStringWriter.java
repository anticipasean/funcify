package funcify.writer;

import funcify.error.FuncifyCodeGenException;
import funcify.spec.StringTemplateSpec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE,
                    staticName = "of")
@Getter
public class StringTemplateStringWriter<R> implements StringTemplateWriter<String, R> {

    @NonNull
    private final StringWriteResultHandler<R> successWriteResultHandler;

    @NonNull
    private final ErrorWriteResultHandler<R> failureWriteResultHandler;

    @Override
    public R write(final StringTemplateSpec templateSpec) {
        try {
            final String output = templateSpec.getStringTemplate()
                                              .render();
            return successWriteResultHandler.transform(templateSpec,
                                                       output);
        } catch (final FuncifyCodeGenException e) {
            return failureWriteResultHandler.transform(templateSpec,
                                                       e);
        } catch (final Throwable t) {
            return failureWriteResultHandler.transform(templateSpec,
                                                       new FuncifyCodeGenException(t.getMessage(),
                                                                                   t));
        }
    }
}
