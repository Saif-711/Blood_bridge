package blood.bridging.donating.Auth;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
}