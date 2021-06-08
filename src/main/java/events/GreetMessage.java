package events;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class GreetMessage {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("message")
    private String message;

    public GreetMessage() {
    }

    @JsonCreator
    public GreetMessage(@JsonProperty("userId") String userId,
                        @JsonProperty("message") String message) {
        this.userId = userId;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override public String toString() {
        return "GreetMessage{" +
            "userId='" + userId + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
