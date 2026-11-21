package com.example.moviejournal.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) // 50 = users.username column size
        String username,

        // BCrypt only uses the first 72 bytes of a password, so longer ones are rejected.
        @NotBlank @Size(min = 8, max = 72)
        String password) {
}
