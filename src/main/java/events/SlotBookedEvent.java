package events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Setter
@Getter
public class SlotBookedEvent {
    @JsonProperty
    private long citizenId;

    @JsonProperty
    private String centerId;

    @JsonProperty
    private String slotId;

    @JsonProperty
    private Instant slotStartTime;

    @JsonProperty
    private Instant slotEndTime;

    @JsonProperty
    private String vaccineType;

    @JsonProperty
    private Instant bookingTimestamp;
}
