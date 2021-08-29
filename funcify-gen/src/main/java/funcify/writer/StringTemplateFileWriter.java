package funcify.writer;

import funcify.error.FuncifyCodeGenException;
import funcify.error.TemplateErrorFeedbackHandler;
import funcify.error.TemplateErrorFeedbackHandler.TemplateErrorFeedbackListener;
import funcify.spec.StringTemplateSpec;
import java.io.File;
import java.nio.file.Files;
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
public class StringTemplateFileWriter<R> implements StringTemplateWriter<File, R> {

    @NonNull
    private final FileWriteResultHandler<R> successWriteResultHandler;

    @NonNull
    private final ErrorWriteResultHandler<R> failureWriteResultHandler;

    private final TemplateErrorFeedbackHandler templateErrorFeedbackHandler = TemplateErrorFeedbackHandler.of();

    @Override
    public R write(final StringTemplateSpec templateSpec) {
        try {
            final File destinationFile = Files.createFile(templateSpec.getDestinationFilePath())
                                              .toFile();
            final TemplateErrorFeedbackListener errorListener = templateErrorFeedbackHandler.createErrorListener();
            final int written = templateSpec.getStringTemplate()
                                            .write(destinationFile,
                                                   errorListener,
                                                   templateSpec.getCharacterEncoding()
                                                               .displayName());
            if (errorListener.hasFeedback()) {
                throw errorListener.getFeedbackAsException();
            }
            return successWriteResultHandler.transform(templateSpec,
                                                       destinationFile);
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
