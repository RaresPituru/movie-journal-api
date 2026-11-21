package com.example.moviejournal.user;

import jakarta.validation.constraints.NotBlank;

/** No length rules here: login should not reveal the password policy, only "wrong credentials". */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
