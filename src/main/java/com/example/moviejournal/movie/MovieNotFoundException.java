package com.example.moviejournal.movie;

/**
 * Thrown when a movie does not exist OR belongs to another user.
 * Both cases deliberately look the same to the client (404), so the API never reveals
 * that another user's movie with that id exists.
 */
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Integer id) {
        super("Movie not found with id " + id);
    }
}
