package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.types.SimpleType;
import org.apache.flink.statefun.sdk.java.types.Type;

public class TypeUtils {

    private TypeUtils() {
    }

    private static final ObjectMapper JSON_OBJ_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

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
