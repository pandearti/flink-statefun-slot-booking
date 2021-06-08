package state;

import lombok.Getter;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class VaccineStock {
    @JsonProperty
    private String centerId;
    @JsonProperty
    private String vaccineType;
    @JsonProperty
    private long totalReceived;
    @JsonProperty
    private long totalAvailable;

    @JsonCreator
    public VaccineStock(String centerId, String vaccineType) {
        this.centerId = centerId;
        this.vaccineType = vaccineType;
    }
}
