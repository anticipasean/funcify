package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@JsonFormat(shape = Shape.OBJECT)
public enum JavaModifier {
    PUBLIC("public"),
    PROTECTED("protected"),
    PRIVATE("private"),
    ABSTRACT("abstract"),
    STATIC("static"),
    FINAL("final"),
    TRANSIENT("transient"),
    VOLATILE("volatile"),
    SYNCHRONIZED("synchronized"),
    NATIVE("native"),
    STRICT("strictfp"),
    INTERFACE("interface"),
    DEFAULT("default");

    @JsonProperty("keyword")
    private final String keyword;

    JavaModifier(final String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
