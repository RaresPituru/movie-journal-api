package com.example.moviejournal.movie;

import org.springframework.http.HttpStatus;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }
}
