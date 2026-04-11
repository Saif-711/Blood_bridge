package blood.bridging.donating.Request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class BloodRequestAddDTO {
    private String bloodType;
    @JsonAlias("hospital")
    private String hospitalName;
    private String location;
    private Integer quantity;
}
