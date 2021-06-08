package functions;

import events.SlotBookingEvent;
import messages.SlotBookingResponse;
import state.SlotStatus;
import state.VaccineSlot;
import org.apache.flink.statefun.sdk.java.*;
import org.apache.flink.statefun.sdk.java.message.Message;
import org.apache.flink.statefun.sdk.java.message.MessageBuilder;
import utils.FunctionUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static events.CustomTypes.*;
import static functions.BookingManagerFn.BOOKING_MANAGER_FN_TYPE;
import static utils.FunctionUtils.sendMessageToTarget;

public class SlotBookingFn implements StatefulFunction {

    public static final TypeName SLOT_BOOKING_FN_TYPE = TypeName.typeNameOf("com.example", "slotBookingFn");

    private static final ValueSpec<VaccineSlot> VACCINE_SLOT = ValueSpec.named("vaccineSlot").withCustomType(VACCINE_SLOT_TYPE);

    public static final StatefulFunctionSpec SPEC = StatefulFunctionSpec.builder(SLOT_BOOKING_FN_TYPE)
            .withSupplier(SlotBookingFn::new)
            .withValueSpec(VACCINE_SLOT)
            .build();

    @Override
    public CompletableFuture<Void> apply(Context context, Message argument) throws Throwable {
        if (argument.is(SLOT_BOOKING_EVENT_TYPE)) {
            SlotBookingEvent slotBookingEvent = argument.as(SLOT_BOOKING_EVENT_TYPE);

            SlotBookingResponse response = new SlotBookingResponse();
            String slotId = slotBookingEvent.getSlotId();
            response.setSlotId(slotId);
            long citizenId = slotBookingEvent.getCitizenId();
            response.setCitizenId(citizenId);

            VaccineSlot vaccineSlotOption = context.storage().get(VACCINE_SLOT).orElse(createVaccineSlot(slotId, citizenId));
//            if (vaccineSlotOption.isPresent()) { // TODO uncomment after state bootstrapping is implemented
                VaccineSlot vaccineSlot = bookVaccineSlot(slotBookingEvent, vaccineSlotOption);

                response.setBookingStatus("Success");
                response.setCenterId(vaccineSlot.getCenterId());
                response.setVaccineType(vaccineSlot.getVaccineType());
                response.setSlotStartTime(vaccineSlot.getFromTime());
                response.setSlotEndTime(vaccineSlot.getToTime());
                context.storage().set(VACCINE_SLOT, vaccineSlot);
//            } else {
//                response.setBookingStatus("Slot-Not-Found"); // TODO
//            }

            sendMessageToTarget(context, BOOKING_MANAGER_FN_TYPE,
                    String.valueOf(slotBookingEvent.getCitizenId()),
                    SLOT_BOOKING_RESPONSE_TYPE, response);
        } // TODO handle other types of input events
        System.out.println("Returning from SlotBookingFn...");
        return context.done();
    }

    private VaccineSlot createVaccineSlot(String slotId, long citizenId) {
        VaccineSlot vaccineSlot = new VaccineSlot(slotId);
        if (citizenId % 2 == 0) {
            vaccineSlot.setCenterId("Mumbai");
            vaccineSlot.setVaccineType("CoviShield");
            vaccineSlot.setSlotStatus(SlotStatus.AVAILABLE);
            vaccineSlot.setAvailableCapacity(5);
            vaccineSlot.setTotalCapacity(5);
            vaccineSlot.setFromTime(Instant.parse("2021-06-10T10:00:00.00Z"));
            vaccineSlot.setToTime(Instant.parse("2021-06-10T12:00:00.00Z"));
        } else {
            vaccineSlot.setCenterId("Mumbai");
            vaccineSlot.setVaccineType("COVACCINE");
            vaccineSlot.setSlotStatus(SlotStatus.AVAILABLE);
            vaccineSlot.setAvailableCapacity(3);
            vaccineSlot.setTotalCapacity(3);
            vaccineSlot.setFromTime(Instant.parse("2021-06-08T14:00:00.00Z"));
            vaccineSlot.setToTime(Instant.parse("2021-06-08T16:00:00.00Z"));
        }
        return vaccineSlot;
    }

    private VaccineSlot bookVaccineSlot(SlotBookingEvent slotBookingEvent, VaccineSlot vaccineSlot) {
        int availableCapacity = vaccineSlot.getAvailableCapacity();
        if (availableCapacity > 0) {
            int citizenSeqNumber = vaccineSlot.getTotalCapacity() - availableCapacity;
            vaccineSlot.setAvailableCapacity(availableCapacity - 1);
            vaccineSlot.getCitizenIds().put(citizenSeqNumber, slotBookingEvent.getCitizenId());
        }
        if (vaccineSlot.getAvailableCapacity() == 0) {
            vaccineSlot.setSlotStatus(SlotStatus.BOOKED);
        }
        return vaccineSlot;
    }
}
