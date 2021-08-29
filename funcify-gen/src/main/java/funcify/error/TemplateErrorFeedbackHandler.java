package funcify.error;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

/**
 * @author smccarron
 * @created 2021-08-29
 */
public interface TemplateErrorFeedbackHandler {

    static TemplateErrorFeedbackHandler of() {
        return Holder.INSTANCE.feedbackHandler;
    }

    TemplateErrorFeedbackListener createErrorListener();

    static enum Holder {
        INSTANCE(DefaultTemplateErrorFeedbackHandler.of());

        private final TemplateErrorFeedbackHandler feedbackHandler;

        Holder(final TemplateErrorFeedbackHandler feedbackHandler) {
            this.feedbackHandler = feedbackHandler;
        }
    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE,
                        staticName = "of")
    static class DefaultTemplateErrorFeedbackHandler implements TemplateErrorFeedbackHandler {

        private final StringTemplateErrorListener stringTemplateErrorListener = StringTemplateErrorListener.of();

        @Override
        public TemplateErrorFeedbackListener createErrorListener() {
            return stringTemplateErrorListener;
        }
    }

    static interface TemplateErrorFeedbackListener extends STErrorListener {

        boolean hasFeedback();

        String getFeedback();

        FuncifyCodeGenException getFeedbackAsException();

    }

    @AllArgsConstructor(access = AccessLevel.PACKAGE,
                        staticName = "of")
    static class StringTemplateErrorListener implements TemplateErrorFeedbackListener {

        final AtomicReference<STMessage> messageHolder = new AtomicReference<>();

        @Override
        public void compileTimeError(final STMessage msg) {
            messageHolder.compareAndSet(null,
                                        msg);
        }

        @Override
        public void runTimeError(final STMessage msg) {
            messageHolder.compareAndSet(null,
                                        msg);
        }

        @Override
        public void IOError(final STMessage msg) {
            messageHolder.compareAndSet(null,
                                        msg);
        }

        @Override
        public void internalError(final STMessage msg) {
            messageHolder.compareAndSet(null,
                                        msg);
        }

        public Optional<STMessage> getSTMessageFeedback() {
            return Optional.ofNullable(messageHolder.get());
        }

        @Override
        public boolean hasFeedback() {
            return getSTMessageFeedback().isPresent();
        }

        public String getFeedback() {
            return getSTMessageFeedback().map(formatSTMessageAsString())
                                         .orElse("");
        }

        private static Function<STMessage, String> formatSTMessageAsString() {
            return stMessage -> new StringBuilder().append("[ message: \"")
                                                   .append(stMessage.error.message)
                                                   .append("\", ")
                                                   .append("cause: ")
                                                   .append(stMessage.cause)
                                                   .append(" ]")
                                                   .toString();
        }

        @Override
        public FuncifyCodeGenException getFeedbackAsException() {
            return getSTMessageFeedback().map(stMessage -> new FuncifyCodeGenException(formatSTMessageAsString().apply(stMessage)))
                                         .orElseGet(() -> new FuncifyCodeGenException("no feedback given"));
        }
    }

}
