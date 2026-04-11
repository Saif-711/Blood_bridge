package blood.bridging.donating.Auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    /** Public registration: DONOR or REQUESTER only (ADMIN is not allowed here). */
    private String role;
}
