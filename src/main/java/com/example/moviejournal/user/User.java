package com.example.moviejournal.user;

/**
 * Internal model of a row in the users table. The password field holds the BCrypt hash.
 * Never exposed through the API.
 */
public record User(Integer id, String username, String password, String role) {
}
