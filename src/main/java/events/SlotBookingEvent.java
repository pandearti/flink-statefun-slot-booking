package events;

import lombok.Getter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class SlotBookingEvent {

    @JsonProperty
    private long citizenId;

    @JsonProperty
    private String slotId;

    @JsonCreator
    public SlotBookingEvent(long citizenId, String slotId) {
        this.citizenId = citizenId;
        this.slotId = slotId;
    }
}
