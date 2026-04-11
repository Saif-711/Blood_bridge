package blood.bridging.donating.Auth;

import blood.bridging.donating.Utils.Enum.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepo userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalStateException("Email already registered");
        }

        Role role = resolveRegistrationRole(request.getRole());

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRole(role);
        return userRepository.save(user);
    }

    private static Role resolveRegistrationRole(String roleRaw) {
        if (roleRaw == null || roleRaw.isBlank()) {
            return Role.REQUESTER;
        }
        Role role;
        try {
            role = Role.valueOf(roleRaw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleRaw);
        }
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot register as ADMIN through public API");
        }
        return role;
    }

    public User authenticate(AuthRequest loginRequest) {
        String email = loginRequest.getEmail() != null
                ? loginRequest.getEmail().trim().toLowerCase()
                : null;
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        loginRequest.getPassword()
                )
        );
        return userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Invalid email or password")
        );
    }
}
