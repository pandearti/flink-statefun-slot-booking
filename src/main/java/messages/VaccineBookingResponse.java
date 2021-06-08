package messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;

@Getter
@Setter
@NoArgsConstructor
public class VaccineBookingResponse {

    @JsonProperty("status")
    private String status; // Success / Failed

    @JsonCreator
    public VaccineBookingResponse(@JsonProperty("status") String status) {
        this.status = status;
    }
}
