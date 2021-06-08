package messages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
public class VaccineBookingResponse {

    @JsonProperty
    private String status; // Success / Failed

    public VaccineBookingResponse(String status) {
        this.status = status;
    }
}
