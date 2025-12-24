package devenv.ips.controller.auth;

import devenv.ips.repository.UserRepository;
import devenv.ips.config.JwtConfig;
import devenv.ips.entity.user.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repo;
    private final JwtConfig jwt;
    private final PasswordEncoder encoder; 

    public AuthController(UserRepository repo, JwtConfig jwt, PasswordEncoder encoder) {
        this.repo = repo;
        this.jwt = jwt;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if(repo.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists!";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        User dbUser = repo.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!encoder.matches(password, dbUser.getPassword())) {
             throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        String token = jwt.generateToken(dbUser.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}