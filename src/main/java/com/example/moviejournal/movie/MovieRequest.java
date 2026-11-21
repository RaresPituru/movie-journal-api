package com.example.moviejournal.movie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

/**
 * What a client may send when creating or updating a movie.
 * There is no id or userId field, so the client cannot set them.
 */
public record MovieRequest(
        @NotEmpty String title,
        @Min(1) @Max(5) Integer rating,
        String review,
        Integer releaseYear) {
}
