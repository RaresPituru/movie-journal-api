package com.example.moviejournal.user;

/** Returned by a successful login. The client sends the token as "Authorization: Bearer &lt;token&gt;". */
public record AuthResponse(String token) {
}
