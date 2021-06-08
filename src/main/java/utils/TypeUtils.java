package utils;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;

public class TypeUtils {

    private TypeUtils() {
    }

    private static final ObjectMapper JSON_OBJ_MAPPER = new ObjectMapper();

    public static <T> Type<T> createJsonType(String typeNamespace, Class<T> jsonClass) {
        return SimpleType.simpleImmutableTypeFrom(
            TypeName.typeNameOf(typeNamespace, jsonClass.getName()),
            JSON_OBJ_MAPPER::writeValueAsBytes,
            bytes -> JSON_OBJ_MAPPER.readValue(bytes, jsonClass)
        );
    }

    public static <T> String toJson(T event) {
        try {
            return JSON_OBJ_MAPPER.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ""; // TODO decide response in case of error
        }
    }
}
