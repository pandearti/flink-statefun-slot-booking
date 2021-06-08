package functions;

import messages.VaccineBookingRequest;
import messages.VaccineBookingResponse;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.message.Message;
import state.VaccineStock;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static events.CustomTypes.*;
import static functions.BookingManagerFn.BOOKING_MANAGER_FN_TYPE;
import static utils.FunctionUtils.sendMessageToTarget;

public class StockUpdateFn implements StatefulFunction {

    public static final TypeName STOCK_UPDATE_FN_TYPE = TypeName.typeNameOf(NAMESPACE, "stockUpdateFn");

    private static final ValueSpec<VaccineStock> VACCINE_STOCK = ValueSpec.named("vaccineStock").withCustomType(VACCINE_STOCK_TYPE);

    public static final StatefulFunctionSpec SPEC = StatefulFunctionSpec.builder(STOCK_UPDATE_FN_TYPE)
            .withSupplier(StockUpdateFn::new)
            .withValueSpec(VACCINE_STOCK)
            .build();

    @Override
    public CompletableFuture<Void> apply(Context context, Message argument) throws Throwable {
        if (argument.is(VACCINE_BOOKING_REQUEST_TYPE)) {
            VaccineBookingRequest request = argument.as(VACCINE_BOOKING_REQUEST_TYPE);
            String status = bookVaccineStock(context, context.storage().get(VACCINE_STOCK));

            VaccineBookingResponse response = new VaccineBookingResponse(status);
            sendMessageToTarget(context, BOOKING_MANAGER_FN_TYPE,
                    String.valueOf(request.getCitizenId()),
                    VACCINE_BOOKING_RESPONSE_TYPE, response);
        } else {
            System.out.println("Invalid argument. Expected VACCINE_BOOKING_REQUEST_TYPE");
            // TODO
        }
        System.out.println("Returning from StockUpdateFn...");
        return context.done();
    }

    private String bookVaccineStock(Context context, Optional<VaccineStock> vaccineStockOption) {
//        if (vaccineStockOption.isPresent()) { // TODO uncomment after state bootstrapping is implemented
        VaccineStock vaccineStock = vaccineStockOption.orElse(createVaccineStock(context.self().id()));
        long totalAvailable = vaccineStock.getTotalAvailable();
        if (totalAvailable > 0) {
            vaccineStock.setTotalAvailable(totalAvailable - 1);
            context.storage().set(VACCINE_STOCK, vaccineStock);
            return "Success";
        }
        return "Vaccine Unavailable";
//        }
//        return "Stock for Vaccine Type / Center Id not found";
    }

    private VaccineStock createVaccineStock(String id) {
        String[] ids = id.split("-");
        VaccineStock vaccineStock = new VaccineStock(ids[0], ids[1]);
        vaccineStock.setTotalAvailable(50);
        vaccineStock.setTotalReceived(50);
        return vaccineStock;
    }
}
