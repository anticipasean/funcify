package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.Definition;
import funcify.tool.SyncList;
import funcify.typedef.javastatement.JavaStatement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class JavaCodeBlock implements Definition<JavaCodeBlock> {

    @JsonProperty("statements")
    @Default
    SyncList<JavaStatement> statements = SyncList.empty();

}
