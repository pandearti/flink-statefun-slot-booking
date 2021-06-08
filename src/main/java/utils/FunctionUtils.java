package utils;

import org.apache.flink.statefun.sdk.java.Address;
import org.apache.flink.statefun.sdk.java.Context;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;
import org.apache.flink.statefun.sdk.java.types.Type;

public class FunctionUtils {
    public static <T> void sendMessageToTarget(Context context, TypeName functionTypeName, String id,
                                               Type<T> customType, T element) {
        Address targetFnAddress = new Address(functionTypeName, id);
        context.send(MessageBuilder.forAddress(targetFnAddress)
                .withCustomType(customType, element)
                .build());
    }
}
