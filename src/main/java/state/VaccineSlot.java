package state;

import lombok.Getter;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class VaccineSlot {
    @JsonProperty
    private String slotId;
    @JsonProperty
    private String centerId;
    @JsonProperty
    private Instant fromTime;
    @JsonProperty
    private Instant toTime;
    @JsonProperty
    private SlotStatus slotStatus;
    @JsonProperty
    private String vaccineType;
    @JsonProperty
    private int totalCapacity;
    @JsonProperty
    private int availableCapacity;
    @JsonProperty
    private Map<Integer, Long> citizenIds;

    @JsonCreator
    public VaccineSlot(String slotId) {
        this.slotId = slotId;
        this.citizenIds = new HashMap<>();
    }
}
