package funcify.writer;

import funcify.spec.StringTemplateSpec;
import java.io.File;

/**
 * @author smccarron
 * @created 2021-08-29
 */
public interface StringTemplateWriter<V, R> {

    WriteResultHandler<V, R> getSuccessWriteResultHandler();

    ErrorWriteResultHandler<R> getFailureWriteResultHandler();

    WriteResult<R> write(StringTemplateSpec templateSpec);

    interface WriteResultHandler<V, R> {

        WriteResult<R> transform(final StringTemplateSpec spec,
                                 final V resultValue);

    }

    @FunctionalInterface
    interface FileWriteResultHandler<R> extends WriteResultHandler<File, R> {

    }

    @FunctionalInterface
    interface StringWriteResultHandler<R> extends WriteResultHandler<String, R> {

    }

    @FunctionalInterface
    interface ConsoleWriteResultHandler extends WriteResultHandler<String, Void> {

    }

    @FunctionalInterface
    interface ErrorWriteResultHandler<R> extends WriteResultHandler<Throwable, R> {

    }

}
