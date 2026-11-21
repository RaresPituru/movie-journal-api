package com.example.moviejournal.security;

/**
 * The principal stored in the SecurityContext for an authenticated request.
 * Built from the JWT, so controllers get the current user's id without a database lookup.
 */
public record AuthenticatedUser(Integer id, String username) {
}
