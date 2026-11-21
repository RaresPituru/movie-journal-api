package com.example.moviejournal.user;

import com.example.moviejournal.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    // A real encoder: the test proves the stored value is a BCrypt hash of the password.
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void register_storesBCryptHash_notRawPassword() {
        given(userRepository.create(any())).willAnswer(invocation -> {
            User u = invocation.getArgument(0);
            return new User(1, u.username(), u.password(), u.role());
        });

        UserResponse response = userService.register(new RegisterRequest("alice", "password123"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).create(captor.capture());
        String storedPassword = captor.getValue().password();
        assertThat(storedPassword).isNotEqualTo("password123").startsWith("$2a$");
        assertThat(passwordEncoder.matches("password123", storedPassword)).isTrue();
        assertThat(captor.getValue().role()).isEqualTo("USER");
        assertThat(response).isEqualTo(new UserResponse(1, "alice"));
    }

    @Test
    void register_throwsConflict_whenUsernameIsTaken() {
        given(userRepository.create(any())).willThrow(new DuplicateKeyException("duplicate key"));

        assertThatThrownBy(() -> userService.register(new RegisterRequest("alice", "password123")))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("alice");
    }

    @Test
    void login_returnsToken_whenPasswordMatches() {
        User alice = new User(1, "alice", passwordEncoder.encode("password123"), "USER");
        given(userRepository.findByUsername("alice")).willReturn(Optional.of(alice));
        given(jwtService.generateToken(1, "alice")).willReturn("jwt-token");

        AuthResponse response = userService.login(new LoginRequest("alice", "password123"));

        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void login_throwsBadCredentials_whenPasswordIsWrong() {
        User alice = new User(1, "alice", passwordEncoder.encode("password123"), "USER");
        given(userRepository.findByUsername("alice")).willReturn(Optional.of(alice));

        assertThatThrownBy(() -> userService.login(new LoginRequest("alice", "wrong-password")))
                .isInstanceOf(BadCredentialsException.class);
    }
}
