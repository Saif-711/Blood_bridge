package blood.bridging.donating.Auth;

import blood.bridging.donating.Config.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // React default
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Object register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = authService.authenticate(request);
        String token = jwtService.generateToken(request.getEmail());
        AuthResponse res=new AuthResponse();
        res.setToken(token);
        return new ResponseEntity<>(
                 res, HttpStatus.OK
        );
    }
}