package com.example.moviejournal.movie;

/**
 * What the API returns for a movie. The owner's userId is internal and not exposed.
 */
public record MovieResponse(Integer id, String title, Integer rating, String review, Integer releaseYear) {

    static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.id(), movie.title(), movie.rating(), movie.review(), movie.releaseYear());
    }
}
