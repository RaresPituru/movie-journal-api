package com.example.moviejournal.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "test-secret-that-is-at-least-32-characters-long";
    private static final long ONE_HOUR_MS = 60 * 60 * 1000;

    private final JwtService jwtService = new JwtService(SECRET, ONE_HOUR_MS);

    @Test
    void generatedToken_containsUsernameAndUserId() {
        String token = jwtService.generateToken(42, "alice");

        assertThat(jwtService.parseToken(token))
                .contains(new AuthenticatedUser(42, "alice"));
    }

    @Test
    void tamperedToken_isRejected() {
        String token = jwtService.generateToken(42, "alice");
        String[] parts = token.split("\\."); // header.payload.signature

        // An attacker rewrites the payload to impersonate "admin" but cannot re-sign it.
        Base64.Decoder decoder = Base64.getUrlDecoder();
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String payload = new String(decoder.decode(parts[1]), StandardCharsets.UTF_8);
        String forgedPayload = encoder.encodeToString(
                payload.replace("\"alice\"", "\"admin\"").getBytes(StandardCharsets.UTF_8));
        String forgedToken = parts[0] + "." + forgedPayload + "." + parts[2];

        assertThat(jwtService.parseToken(forgedToken)).isEmpty();
    }

    @Test
    void tokenSignedWithAnotherSecret_isRejected() {
        JwtService otherService = new JwtService("another-secret-that-is-also-32-characters-long", ONE_HOUR_MS);
        String token = otherService.generateToken(42, "alice");

        assertThat(jwtService.parseToken(token)).isEmpty();
    }

    @Test
    void expiredToken_isRejected() {
        JwtService expiringService = new JwtService(SECRET, -1000); // already expired when issued
        String token = expiringService.generateToken(42, "alice");

        assertThat(jwtService.parseToken(token)).isEmpty();
    }

    @Test
    void malformedToken_isRejected() {
        assertThat(jwtService.parseToken("not-a-jwt")).isEmpty();
    }
}
