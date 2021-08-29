package funcify.writer;

import funcify.error.FuncifyCodeGenException;
import funcify.spec.StringTemplateSpec;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE,
                    staticName = "of")
@Getter
public class StringTemplateConsoleWriter implements StringTemplateWriter<String, Void> {

    private final StringTemplateWriter.ConsoleWriteResultHandler successWriteResultHandler;

    private final StringTemplateWriter.ErrorWriteResultHandler<Void> failureWriteResultHandler;

    @Override
    public Void write(final StringTemplateSpec templateSpec) {
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
