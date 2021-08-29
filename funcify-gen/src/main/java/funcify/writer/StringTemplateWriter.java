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

    R write(StringTemplateSpec templateSpec);

    static interface WriteResultHandler<V, R> {

        R transform(final StringTemplateSpec spec,
                    final V resultValue);

    }

    @FunctionalInterface
    static interface FileWriteResultHandler<R> extends WriteResultHandler<File, R> {

    }

    @FunctionalInterface
    static interface StringWriteResultHandler<R> extends WriteResultHandler<String, R> {

    }

    @FunctionalInterface
    static interface ConsoleWriteResultHandler extends WriteResultHandler<String, Void> {

    }

    @FunctionalInterface
    static interface ErrorWriteResultHandler<R> extends WriteResultHandler<Throwable, R> {

    }

}
