package com.example.moviejournal.movie;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record Movie(Integer id, @NotEmpty String title, @Min(1
) @Max(5) Integer rating, String review, Integer releaseYear) {
}
