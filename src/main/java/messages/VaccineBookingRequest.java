package messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class VaccineBookingRequest {
    @JsonProperty("centerId")
    private String centerId;
    @JsonProperty("vaccineType")
    private String vaccineType;
    @JsonProperty("citizenId")
    private long citizenId;

    @JsonCreator
    public VaccineBookingRequest(@JsonProperty("centerId") String centerId,
                                 @JsonProperty("vaccineType") String vaccineType,
                                 @JsonProperty("citizenId") long citizenId) {
        this.centerId = centerId;
        this.vaccineType = vaccineType;
        this.citizenId = citizenId;
    }
}
