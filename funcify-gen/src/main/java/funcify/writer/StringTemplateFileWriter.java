package funcify.writer;

import funcify.error.FuncifyCodeGenException;
import funcify.error.TemplateErrorFeedbackHandler;
import funcify.error.TemplateErrorFeedbackHandler.TemplateErrorFeedbackListener;
import funcify.spec.StringTemplateSpec;
import funcify.tool.container.SyncMap;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smccarron
 * @created 2021-08-29
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE,
                    staticName = "of")
@Getter
public class StringTemplateFileWriter<R> implements StringTemplateWriter<File, R> {

    private static final Logger logger = LoggerFactory.getLogger(StringTemplateFileWriter.class);

    @NonNull
    private final FileWriteResultHandler<R> successWriteResultHandler;

    @NonNull
    private final ErrorWriteResultHandler<R> failureWriteResultHandler;

    private final TemplateErrorFeedbackHandler templateErrorFeedbackHandler = TemplateErrorFeedbackHandler.of();

    @Override
    public WriteResult<R> write(final StringTemplateSpec templateSpec) {
        try {
            logger.debug("write: [ {} ]",
                         SyncMap.empty()
                                .put("string_template_group_file_path",
                                     templateSpec.getStringTemplateGroupFilePath())
                                .put("destination_file_path",
                                     templateSpec.getDestinationFilePath()));
            final Set<PosixFilePermission> posixFilePermissionsDefaultSet = EnumSet.of(PosixFilePermission.OWNER_READ,
                                                                                       PosixFilePermission.OWNER_WRITE,
                                                                                       PosixFilePermission.OWNER_EXECUTE,
                                                                                       PosixFilePermission.GROUP_READ,
                                                                                       PosixFilePermission.GROUP_EXECUTE,
                                                                                       PosixFilePermission.OTHERS_READ);
            final FileAttribute<Set<PosixFilePermission>> fileAttribute = PosixFilePermissions.asFileAttribute(posixFilePermissionsDefaultSet);
            if (!templateSpec.getDestinationFilePath()
                             .getParent()
                             .toFile()
                             .exists()) {
                Files.createDirectories(templateSpec.getDestinationFilePath()
                                                    .getParent(),
                                        fileAttribute);
            }
            final File destinationFile = Files.createFile(templateSpec.getDestinationFilePath(),
                                                          fileAttribute)
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
            logger.error("write: [ status: failed ] due to [ type: {}, message: {} ]",
                         e.getClass()
                          .getSimpleName(),
                         e.getMessage());
            return failureWriteResultHandler.transform(templateSpec,
                                                       e);
        } catch (final Throwable t) {
            logger.error("write: [ status: failed ] due to [ type: {}, message: {} ]",
                         t.getClass()
                          .getSimpleName(),
                         t.getMessage());
            return failureWriteResultHandler.transform(templateSpec,
                                                       new FuncifyCodeGenException(t.getMessage(),
                                                                                   t));
        }
    }
}
