package events;

import lombok.Getter;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

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
