package com.erp.shared.exception;

import java.util.Collections;
import java.util.List;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends DomainException {

    private final List<ValidationError> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = Collections.emptyList();
    }

    public ValidationException(List<ValidationError> errors) {
        super("Validation failed");
        this.errors = errors != null ? List.copyOf(errors) : Collections.emptyList();
    }

    public ValidationException(String field, String message) {
        super(message);
        this.errors = List.of(new ValidationError(field, message));
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public record ValidationError(String field, String message) {}
}
