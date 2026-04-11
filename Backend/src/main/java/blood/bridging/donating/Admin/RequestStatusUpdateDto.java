package blood.bridging.donating.Admin;

import blood.bridging.donating.Utils.Enum.RequestStatus;
import lombok.Data;

@Data
public class RequestStatusUpdateDto {
    private RequestStatus status;
}
