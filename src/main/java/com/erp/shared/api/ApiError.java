package com.erp.shared.api;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API error response.
 */
public record ApiError(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldError> fieldErrors
) {
    public ApiError(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ApiError(int status, String error, String message, String path, List<FieldError> fieldErrors) {
        this(LocalDateTime.now(), status, error, message, path, fieldErrors);
    }

    public record FieldError(String field, String message) {}
}
