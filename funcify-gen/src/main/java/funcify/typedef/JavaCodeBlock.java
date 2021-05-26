package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.javastatement.JavaStatement;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

/**
 * @author smccarron
 * @created 2021-05-22
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
@With
public class JavaCodeBlock {

    @JsonProperty("statements")
    @Default
    List<JavaStatement> statements = new ArrayList<>();

}
