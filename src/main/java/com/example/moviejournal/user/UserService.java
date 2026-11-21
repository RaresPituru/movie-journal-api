package com.example.moviejournal.user;

import com.example.moviejournal.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.password());
        User saved = userRepository.create(new User(null, request.username(), hashedPassword, DEFAULT_ROLE));
        return new UserResponse(saved.id(), saved.username());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw new RuntimeException("Invalid username or password");
        }

        return new AuthResponse(jwtService.generateToken(user.id(), user.username()));
    }
}
