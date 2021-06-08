package events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class SlotBookingEvent {

    @JsonProperty("citizenId")
    private long citizenId;

    @JsonProperty("slotId")
    private String slotId;

    @JsonCreator
    public SlotBookingEvent(@JsonProperty("citizenId") long citizenId,
                            @JsonProperty("slotId") String slotId) {
        this.citizenId = citizenId;
        this.slotId = slotId;
    }
}
