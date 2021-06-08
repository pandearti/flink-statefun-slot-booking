package events;

import messages.SlotBookingResponse;
import messages.VaccineBookingRequest;
import messages.VaccineBookingResponse;
import state.VaccineSlot;
import state.VaccineStock;
import org.apache.flink.statefun.sdk.java.types.Type;
import utils.TypeUtils;

public class CustomTypes {

    public static final String NAMESPACE = "com.example";

    public static final Type<GreetMessage> GREET_MESSAGE_TYPE = TypeUtils.createJsonType("com.example", GreetMessage.class);

    public static final Type<SlotBookingEvent> SLOT_BOOKING_EVENT_TYPE = TypeUtils.createJsonType(NAMESPACE, SlotBookingEvent.class);

    public static final Type<SlotBookingResponse> SLOT_BOOKING_RESPONSE_TYPE = TypeUtils.createJsonType(NAMESPACE, SlotBookingResponse.class);

    public static final Type<VaccineBookingRequest> VACCINE_BOOKING_REQUEST_TYPE = TypeUtils.createJsonType(NAMESPACE, VaccineBookingRequest.class);

    public static final Type<VaccineBookingResponse> VACCINE_BOOKING_RESPONSE_TYPE = TypeUtils.createJsonType(NAMESPACE, VaccineBookingResponse.class);


    // State Types
    public static final Type<VaccineSlot> VACCINE_SLOT_TYPE = TypeUtils.createJsonType(NAMESPACE, VaccineSlot.class);
    public static final Type<VaccineStock> VACCINE_STOCK_TYPE = TypeUtils.createJsonType(NAMESPACE, VaccineStock.class);
    public static final Type<SlotBookedEvent> SLOT_BOOKED = TypeUtils.createJsonType(NAMESPACE, SlotBookedEvent.class);

}
