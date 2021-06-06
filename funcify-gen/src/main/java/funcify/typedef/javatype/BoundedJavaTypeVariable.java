package funcify.typedef.javatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.container.SyncList;
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
 * @created 2021-05-22
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Getter
@Builder
@ToString
public class BoundedJavaTypeVariable implements JavaType {

    @JsonProperty("parameterized")
    private final boolean parameterized = true;

    @Override
    public String getName() {
        return getBaseType().getName();
    }

    @JsonProperty("base_type")
    private JavaType baseType;

    @Default
    @JsonProperty("lower_bound_types")
    private SyncList<JavaType> lowerBoundTypes = SyncList.empty();

    @Default
    @JsonProperty("upper_bound_types")
    private SyncList<JavaType> upperBoundTypes = SyncList.empty();

}
