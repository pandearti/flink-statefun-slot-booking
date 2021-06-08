import functions.GreetFn;
import functions.BookingManagerFn;
import functions.SlotBookingFn;
import functions.StockUpdateFn;
import io.undertow.Undertow;
import org.apache.flink.statefun.sdk.java.StatefulFunctions;
import org.apache.flink.statefun.sdk.java.handler.RequestReplyHandler;
import server.UndertowHttpHandler;

public class Application {

    public static final int PORT = 1108;

    public static void main(String[] args) {
        final StatefulFunctions functions = new StatefulFunctions();
        functions.withStatefulFunction(GreetFn.SPEC);
        functions.withStatefulFunction(BookingManagerFn.SPEC);
        functions.withStatefulFunction(SlotBookingFn.SPEC);
        functions.withStatefulFunction(StockUpdateFn.SPEC);

        RequestReplyHandler requestReplyHandler = functions.requestReplyHandler();

        final Undertow httpServer =
            Undertow.builder()
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(new UndertowHttpHandler(requestReplyHandler))
                .build();

        httpServer.start();
        System.out.println("Server started...!!");
    }
}
