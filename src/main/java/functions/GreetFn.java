package functions;

import java.util.concurrent.CompletableFuture;
import events.CustomTypes;
import events.GreetMessage;
import events.GreetResponse;
import org.apache.flink.statefun.sdk.java.Context;
import org.apache.flink.statefun.sdk.java.StatefulFunction;
import org.apache.flink.statefun.sdk.java.StatefulFunctionSpec;
import org.apache.flink.statefun.sdk.java.TypeName;
import org.apache.flink.statefun.sdk.java.ValueSpec;
import org.apache.flink.statefun.sdk.java.io.KafkaEgressMessage;
import org.apache.flink.statefun.sdk.java.message.Message;

public class GreetFn implements StatefulFunction {

    private static final ValueSpec<Integer> SEEN_COUNT = ValueSpec.named("seen_count").withIntType();

    public static final TypeName TYPENAME = TypeName.typeNameOf("com.example", "greet-fn");
    public static final StatefulFunctionSpec SPEC =
        StatefulFunctionSpec.builder(TYPENAME)
            .withSupplier(GreetFn::new)
            .withValueSpec(SEEN_COUNT)
            .build();

    private static final TypeName KAFKA_EGRESS =
        TypeName.typeNameOf("com.example", "greet-response");

    @Override public CompletableFuture<Void> apply(Context context, Message argument) throws Throwable {
        System.out.println("Inside GreetFn...");
        if (argument.is(CustomTypes.GREET_MESSAGE_TYPE)) {
            GreetMessage greetMessage = argument.as(CustomTypes.GREET_MESSAGE_TYPE);

            System.out.println("Message... " + greetMessage.toString());

            int seenCount = context.storage().get(SEEN_COUNT).orElse(0);
            seenCount++;

            context.storage().set(SEEN_COUNT, seenCount);

            System.out.println("Sending message to output topic...");
            context.send(
                KafkaEgressMessage.forEgress(KAFKA_EGRESS)
                    .withTopic("greet-response")
                    .withUtf8Key(context.self().id())
                    .withUtf8Value(createGreetResponse(greetMessage, seenCount))
                    .build());
        }
        System.out.println("Returning from GreetFn...");
        return context.done();
    }

    private String createGreetResponse(GreetMessage greetMessage, int seenCount) {
        GreetResponse greetResponse = new GreetResponse(greetMessage.getUserId(), seenCount, greetMessage.getMessage());
        return greetResponse.toString();
    }
}
