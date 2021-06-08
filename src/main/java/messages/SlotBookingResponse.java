package messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class SlotBookingResponse {

    @JsonProperty
    private String bookingStatus; // Success / Failed / Slot-Not-Found
    @JsonProperty
    private long citizenId;
    @JsonProperty
    private String slotId;
    @JsonProperty
    private String centerId;
    @JsonProperty
    private Instant slotStartTime;
    @JsonProperty
    private Instant slotEndTime;
    @JsonProperty
    private String vaccineType;
}
