package com.example.moviejournal.movie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * What a client may send when creating or updating a movie.
 * There is no id or userId field, so the client cannot set them.
 * The size limits match the column sizes in schema.sql.
 */
public record MovieRequest(
        @NotBlank @Size(max = 250)
        String title,

        @NotNull @Min(1) @Max(10)
        Integer rating,

        @Size(max = 1000)
        String review, // optional

        // 1888 = the oldest surviving film (Roundhay Garden Scene); 2100 leaves room for announced films.
        @NotNull @Min(1888) @Max(2100)
        Integer releaseYear) {
}
