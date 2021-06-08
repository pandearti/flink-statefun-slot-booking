package state;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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

    public VaccineStock(String centerId, String vaccineType) {
        this.centerId = centerId;
        this.vaccineType = vaccineType;
    }
}
