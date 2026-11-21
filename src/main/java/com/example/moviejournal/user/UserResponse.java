package com.example.moviejournal.user;

/** Public view of a user: never contains the password hash. */
public record UserResponse(Integer id, String username) {
}
