package funcify.typedef;

import com.fasterxml.jackson.annotation.JsonProperty;
import funcify.tool.container.SyncList;
import funcify.tool.container.SyncMap;
import funcify.tool.container.SyncMap.Tuple2;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;
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
public class JavaAnnotation {

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameters")
    @Default
    private SyncMap<String, Object> parameters = SyncMap.empty();

    @JsonProperty("parameters_as_string")
    public String getParametersAsString() {
        if (getParameters().isEmpty()) {
            return null;
        }
        final SyncList<String> outputList = SyncList.empty();
        for (Tuple2<String, Object> tuple : getParameters()) {
            outputList.append(String.join(" = ",
                                          tuple._1(),
                                          convertObjectToTextualRepresentation(tuple._2())));
        }
        if (outputList.size() == 1 && outputList.stream()
                                                .anyMatch(k -> k.startsWith("value = "))) {
            return outputList.get(0)
                             .map(s -> s.replace("value = ",
                                                 ""))
                             .orElse(null);
        }
        return outputList.join(", ");
    }

    private static String convertObjectToTextualRepresentation(final Object v) {
        if (v instanceof String) {
            return String.format("\"%s\"",
                                 v);
        } else if (v instanceof Class) {
            return String.format("%s.class",
                                 v.getClass()
                                  .getName());
        } else if (v instanceof Number) {
            return NumberFormat.getInstance()
                               .format(v);
        } else if (v instanceof Boolean) {
            return Boolean.toString((Boolean) v);
        } else if (v != null && v.getClass()
                                 .isArray()) {

            if (Array.getLength(v) == 0) {
                return "{}";
            } else {
                return IntStream.range(0,
                                       Array.getLength(v))
                                .mapToObj(i -> Array.get(v,
                                                         i))
                                .map(JavaAnnotation::convertObjectToTextualRepresentation)
                                .collect(Collectors.joining(", ",
                                                            "{ ",
                                                            " }"));
            }

        } else if (v instanceof Iterable<?>) {
            return StreamSupport.stream(((Iterable<?>) v).spliterator(),
                                        false)
                                .map(JavaAnnotation::convertObjectToTextualRepresentation)
                                .collect(Collectors.joining(", ",
                                                            "{ ",
                                                            " }"));
        } else {
            throw new UnsupportedOperationException(String.format("currently not configured to support transformation of type [ %s ] into java_annotation value parameters string",
                                                                  v == null ? "null" : v.getClass()
                                                                                        .getName()));
        }
    }
}
