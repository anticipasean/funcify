package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.typedef.javatype.JavaType;
import funcify.typedef.javatype.SimpleJavaType;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
public class JavaField {

    @JsonProperty("modifiers")
    @Default
    private List<JavaModifier> modifiers = new ArrayList<>();

    @JsonProperty("type")
    private JavaType type;

    @JsonProperty("name")
    private String name;


}
