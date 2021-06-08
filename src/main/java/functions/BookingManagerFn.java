package functions;

import events.CustomTypes;
import events.SlotBookedEvent;
import events.SlotBookingEvent;
import messages.SlotBookingResponse;
import messages.VaccineBookingRequest;
import messages.VaccineBookingResponse;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.io.KafkaEgressMessage;
import org.apache.flink.statefun.sdk.java.message.Message;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;
import state.VaccineStock;
import utils.FunctionUtils;
import utils.TypeUtils;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static events.CustomTypes.*;
import static functions.SlotBookingFn.SLOT_BOOKING_FN_TYPE;
import static functions.StockUpdateFn.STOCK_UPDATE_FN_TYPE;
import static utils.FunctionUtils.sendMessageToTarget;

public class BookingManagerFn implements StatefulFunction {

    public static final TypeName BOOKING_MANAGER_FN_TYPE = TypeName.typeNameOf(NAMESPACE, "bookingManagerFn");

    public static final StatefulFunctionSpec SPEC = StatefulFunctionSpec.builder(BOOKING_MANAGER_FN_TYPE)
            .withSupplier(BookingManagerFn::new).build();

    private static final ValueSpec<SlotBookedEvent> SLOT_BOOKED = ValueSpec.named("slotBooking").withCustomType(CustomTypes.SLOT_BOOKED);

    private static final TypeName KAFKA_EGRESS =
            TypeName.typeNameOf(NAMESPACE, "slot-booking-response");

    @Override
    public CompletableFuture<Void> apply(Context context, Message argument) throws Throwable {

        if (argument.is(SLOT_BOOKING_EVENT_TYPE)) {
            SlotBookingEvent slotBookingEvent = argument.as(SLOT_BOOKING_EVENT_TYPE);

            SlotBookedEvent slotBookedEvent = new SlotBookedEvent();
            slotBookedEvent.setCitizenId(slotBookingEvent.getCitizenId());
            slotBookedEvent.setSlotId(slotBookingEvent.getSlotId());
            context.storage().set(SLOT_BOOKED, slotBookedEvent);

            sendMessageToTarget(context, SLOT_BOOKING_FN_TYPE, slotBookingEvent.getSlotId(),
                    SLOT_BOOKING_EVENT_TYPE, slotBookingEvent);
        }
        else if (argument.is(SLOT_BOOKING_RESPONSE_TYPE)) {
            SlotBookingResponse response = argument.as(SLOT_BOOKING_RESPONSE_TYPE);
            if ("Success".equals(response.getBookingStatus())) {
                String centerId = response.getCenterId();
                String vaccineType = response.getVaccineType();

                // TODO handle optional
                SlotBookedEvent slotBookedEvent = context.storage().get(SLOT_BOOKED).get();
                slotBookedEvent.setCenterId(response.getCenterId());
                slotBookedEvent.setSlotStartTime(response.getSlotStartTime());
                slotBookedEvent.setSlotEndTime(response.getSlotEndTime());
                slotBookedEvent.setVaccineType(response.getVaccineType());
                context.storage().set(SLOT_BOOKED, slotBookedEvent);

                VaccineBookingRequest vaccineBookingRequest =
                        new VaccineBookingRequest(centerId, vaccineType, response.getCitizenId());
                sendMessageToTarget(context, STOCK_UPDATE_FN_TYPE, centerId + "-" + vaccineType,
                        VACCINE_BOOKING_REQUEST_TYPE, vaccineBookingRequest);
            } else {
                // Slot Booking failed
            }
        }
        else if (argument.is(VACCINE_BOOKING_RESPONSE_TYPE)) {
            VaccineBookingResponse response = argument.as(VACCINE_BOOKING_RESPONSE_TYPE);
            if ("Success".equals(response.getStatus())) {
                // TODO handle optional
                SlotBookedEvent slotBookedEvent = context.storage().get(SLOT_BOOKED).get();
                slotBookedEvent.setBookingTimestamp(Instant.now());
                context.storage().set(SLOT_BOOKED, slotBookedEvent);

                System.out.println("Sending message to output topic...");
                context.send(
                        KafkaEgressMessage.forEgress(KAFKA_EGRESS)
                                .withTopic("slot-booking-response")
                                .withUtf8Key(context.self().id())
                                .withUtf8Value(TypeUtils.toJson(slotBookedEvent))
                                .build());
            } else {
                // TODO Slot Booking failed
            }
        }
        System.out.println("Returning from BookingManagerFn...");
        return context.done();
    }
}
