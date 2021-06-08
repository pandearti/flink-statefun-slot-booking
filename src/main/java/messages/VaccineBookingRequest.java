package messages;

import lombok.Getter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class VaccineBookingRequest {
    @JsonProperty
    private String centerId;
    @JsonProperty
    private String vaccineType;
    @JsonProperty
    private long citizenId;

    @JsonCreator
    public VaccineBookingRequest(String centerId, String vaccineType, long citizenId) {
        this.centerId = centerId;
        this.vaccineType = vaccineType;
        this.citizenId = citizenId;
    }
}
