package funcify;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.function.BiPredicate;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

/**
 * @author smccarron
 * @created 2021-05-20
 */
public class JsonNodeModelAdapter implements ModelAdaptor<JsonNode> {

    public JsonNodeModelAdapter() {

    }

    @Override
    public Object getProperty(final Interpreter interp,
                              final ST self,
                              final JsonNode model,
                              final Object property,
                              final String propertyName) throws STNoSuchPropertyException {
        requireNonNull(model,
                       "model");
        if (property == null) {
            return throwNoSuchProperty(JsonNode.class,
                                       propertyName,
                                       null);
        }
        requireNonNull(propertyName,
                       "propertyName");
        //        System.out.println(String.format("model_adapter: [ node_type: %s, property: %s, property_name: %s ]",
        //                                         model.getNodeType(),
        //                                         property,
        //                                         propertyName));
        try {
            return mapJsonNode(model,
                               propertyName);
        } catch (final Throwable t) {
            return throwNoSuchProperty(JsonNode.class,
                                       propertyName,
                                       t);
        }
    }

    private Object mapJsonNode(JsonNode model,
                               String propertyName) {
        switch (model.getNodeType()) {
            case POJO:
            case OBJECT:
                if (propertyFoundAndContainerPropertyValue(String.class).test(model,
                                                                              propertyName)) {
                    return model.get(propertyName);
                } else if (propertyFoundAndNotContainerPropertyValue(String.class).test(model,
                                                                                        propertyName)) {
                    return mapJsonNode(model.get(propertyName),
                                       "");
                } else {
                    return throwNoSuchProperty(JsonNode.class,
                                               propertyName,
                                               null);
                }
            case ARRAY:
                final int index = Integer.parseInt(propertyName);
                if (propertyFoundAndContainerPropertyValue(Integer.class).test(model,
                                                                               index)) {
                    return model.get(index);
                } else if (propertyFoundAndNotContainerPropertyValue(Integer.class).test(model,
                                                                                         index)) {
                    return mapJsonNode(model.get(index),
                                       "");
                } else {
                    return throwNoSuchProperty(JsonNode.class,
                                               propertyName,
                                               null);
                }
            case BOOLEAN:
                return model.asBoolean();
            case NUMBER:
                return model.isIntegralNumber() ? model.asLong() : model.asDouble();
            case BINARY:
            case STRING:
                return model.asText();
            case MISSING:
            case NULL:
            default:
                return throwNoSuchProperty(JsonNode.class,
                                           propertyName,
                                           null);
        }
    }

    private static <T> BiPredicate<JsonNode, T> propertyFoundAndNotContainerPropertyValue(Class<T> propertyNameType) {
        return (jsonNode, propName) -> {
            if (String.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.has(((String) propName)) && !jsonNode.get(((String) propName))
                                                                     .isContainerNode();
            } else if (Integer.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.has(((Integer) propName)) && !jsonNode.get(((Integer) propName))
                                                                      .isContainerNode();
            } else {
                return false;
            }
        };
    }

    private static <T> BiPredicate<JsonNode, T> propertyFoundAndContainerPropertyValue(Class<T> propertyNameType) {
        return (jsonNode, propName) -> {
            if (String.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.has(((String) propName)) && jsonNode.get(((String) propName))
                                                                    .isContainerNode();
            } else if (Integer.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.has(((Integer) propName)) && jsonNode.get(((Integer) propName))
                                                                     .isContainerNode();
            } else {
                return false;
            }
        };
    }

    private <T> T throwNoSuchProperty(Class<T> cls,
                                      String propertyName,
                                      Throwable cause) {
        throw new STNoSuchPropertyException(cause instanceof Exception ? ((Exception) cause) : new RuntimeException(cause),
                                            null,
                                            cls.getName() + "." + propertyName);
    }
}
