package funcify.typedef.javaexpr;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-25
 */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
public class TextExpression implements JavaExpression {

    @JsonProperty("text")
    private String text;

}
