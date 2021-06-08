package events;

public class GreetResponse {

    private final String userId;
    private final Integer seenCount;
    private final String message;

    public GreetResponse(String userId, Integer seenCount, String message) {
        this.userId = userId;
        this.seenCount = seenCount;
        this.message = message;
    }

    @Override public String toString() {
        return "GreetResponse{" +
            "userId='" + userId + '\'' +
            ", seenCount=" + seenCount +
            ", message='" + message + '\'' +
            '}';
    }
}
