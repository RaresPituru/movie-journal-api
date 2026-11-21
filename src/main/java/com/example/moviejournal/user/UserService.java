package com.example.moviejournal.user;

import com.example.moviejournal.security.JwtService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String DEFAULT_ROLE = "USER";
    // Same message for unknown user and wrong password, so attackers cannot discover valid usernames.
    private static final String INVALID_CREDENTIALS = "Invalid username or password";

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
        try {
            User saved = userRepository.create(new User(null, request.username(), hashedPassword, DEFAULT_ROLE));
            return new UserResponse(saved.id(), saved.username());
        } catch (DuplicateKeyException e) {
            // Rely on the UNIQUE constraint instead of "check, then insert":
            // that would still fail if two requests register the same name at the same time.
            throw new UsernameAlreadyExistsException(request.username());
        }
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }

        return new AuthResponse(jwtService.generateToken(user.id(), user.username()));
    }
}
