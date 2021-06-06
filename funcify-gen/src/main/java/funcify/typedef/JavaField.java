package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.container.SyncList;
import funcify.typedef.javatype.JavaType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author smccarron
 * @created 2021-05-19
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Builder
@Getter
@ToString
public class JavaField {

    @JsonProperty("modifiers")
    @Default
    private SyncList<JavaModifier> modifiers = SyncList.empty();

    @JsonProperty("type")
    private JavaType type;

    @JsonProperty("name")
    private String name;


}
