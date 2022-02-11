package funcify.st.adapter.model;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.JsonNode;
import funcify.tool.LiftOps;
import java.util.OptionalInt;
import java.util.function.BiPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

/**
 * @author smccarron
 * @created 2021-05-20
 */
public class JsonNodeModelAdapter implements ModelAdaptor<JsonNode> {

    private static Logger logger = LoggerFactory.getLogger(JsonNodeModelAdapter.class);

    public JsonNodeModelAdapter() {

    }

    private static <T> BiPredicate<JsonNode, T> propertyFoundAndNotContainerPropertyValue(Class<T> propertyNameType) {
        return (jsonNode, propName) -> {
            if (String.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.hasNonNull(((String) propName)) && !jsonNode.get(((String) propName))
                                                                            .isContainerNode();
            } else if (Integer.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.hasNonNull(((Integer) propName)) && !jsonNode.get(((Integer) propName))
                                                                             .isContainerNode();
            } else {
                return false;
            }
        };
    }

    private static <T> BiPredicate<JsonNode, T> propertyFoundAndContainerPropertyValue(Class<T> propertyNameType) {
        return (jsonNode, propName) -> {
            if (String.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.hasNonNull(((String) propName)) && jsonNode.get(((String) propName))
                                                                           .isContainerNode();
            } else if (Integer.class.isAssignableFrom(propertyNameType)) {
                return jsonNode.hasNonNull(((Integer) propName)) && jsonNode.get(((Integer) propName))
                                                                            .isContainerNode();
            } else {
                return false;
            }
        };
    }

    @Override
    public Object getProperty(final Interpreter interp,
                              final ST self,
                              final JsonNode model,
                              final Object property,
                              final String propertyName) throws STNoSuchPropertyException {
        requireNonNull(model, "model");
        //        logger.info("model_adapter: [ node_type: {}, property: {}, property.type: {}, property_name: {} ]",
        //                    model.getNodeType(),
        //                    property,
        //                    property == null ? "null" : property.getClass()
        //                                                        .getSimpleName(),
        //                    propertyName);
        if (property == null) {
            return throwNoSuchProperty(JsonNode.class, propertyName, null);
        }
        requireNonNull(propertyName, "propertyName");
        //        logger.info("{}: model: {}, property_name: {}", model.getNodeType(), model, propertyName);
        switch (model.getNodeType()) {
            case POJO:
                return model.asText();
            case OBJECT:
                if (propertyFoundAndContainerPropertyValue(String.class).test(model, propertyName)) {
                    return model.get(propertyName);
                } else if (propertyFoundAndNotContainerPropertyValue(String.class).test(model, propertyName)) {
                    return model.get(propertyName)
                                .isBoolean() ? model.get(propertyName)
                                                    .asBoolean() : model.get(propertyName)
                                                                        .asText();
                } else if (propertyName.isEmpty()) {
                    return model;
                } else {
                    return throwNoSuchProperty(JsonNode.class, propertyName, null);
                }
            case ARRAY:
                final OptionalInt indexOpt = LiftOps.tryCatchLift(() -> Integer.parseInt(propertyName))
                                                    .map(OptionalInt::of)
                                                    .orElse(OptionalInt.empty());
                if (indexOpt.isPresent() && propertyFoundAndContainerPropertyValue(Integer.class).test(model,
                                                                                                       indexOpt.getAsInt())) {
                    return model.get(indexOpt.getAsInt());
                } else if (indexOpt.isPresent() && propertyFoundAndNotContainerPropertyValue(Integer.class).test(model,
                                                                                                                 indexOpt.getAsInt())) {
                    return model.get(indexOpt.getAsInt())
                                .isBoolean() ? model.get(indexOpt.getAsInt())
                                                    .asBoolean() : model.get(indexOpt.getAsInt())
                                                                        .asText();
                } else if (!indexOpt.isPresent() && propertyName.isEmpty()) {
                    return model;
                }
                //                else if (!indexOpt.isPresent() && propertyName.isEmpty() && model.size() == 1) {
                //                    return model.get(0);
                //                }
                else {
                    return throwNoSuchProperty(JsonNode.class, propertyName, null);
                }
            case BOOLEAN:
                return model.asBoolean();
            case NUMBER:
            case BINARY:
            case STRING:
                return model.asText();
            case MISSING:
            case NULL:
            default:
                return throwNoSuchProperty(JsonNode.class, propertyName, null);
        }
    }

    private <T> T throwNoSuchProperty(Class<T> cls, String propertyName, Throwable cause) {
        throw new STNoSuchPropertyException(cause instanceof Exception ? ((Exception) cause) : new RuntimeException(cause),
                                            null,
                                            cls.getName() + "." + propertyName);
    }
}
