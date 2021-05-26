package funcify.typedef.javatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Getter
@Builder
public class SimpleJavaTypeVariable implements JavaType {

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameterized")
    private final boolean parameterized = false;

}
