package com.example.moviejournal.exception;

import java.time.Instant;
import java.util.List;

/**
 * The single JSON error format returned by the API for every error.
 * fieldErrors is empty unless the request failed validation.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> fieldErrors) {

    public record FieldError(String field, String message) {
    }
}
