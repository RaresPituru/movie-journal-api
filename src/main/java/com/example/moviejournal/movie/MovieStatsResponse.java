package com.example.moviejournal.movie;

import java.math.BigDecimal;

/**
 * Statistics about the current user's movies.
 * averageRating and highestRatedMovie are null when the user has no movies.
 */
public record MovieStatsResponse(long totalMovies, BigDecimal averageRating, HighestRatedMovie highestRatedMovie) {

    public record HighestRatedMovie(String title, int rating) {
    }
}
