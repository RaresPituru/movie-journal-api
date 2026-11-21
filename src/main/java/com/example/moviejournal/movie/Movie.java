package com.example.moviejournal.movie;

/**
 * Internal model of a row in the movie table. Never sent to or read from the client directly:
 * see {@link MovieRequest} and {@link MovieResponse}.
 */
public record Movie(Integer id, String title, Integer rating, String review, Integer releaseYear, Integer userId) {
}
