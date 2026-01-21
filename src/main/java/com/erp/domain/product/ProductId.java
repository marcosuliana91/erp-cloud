package com.erp.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object representing a Product identifier.
 * Immutable and self-validating.
 */
@Getter
@EqualsAndHashCode
public final class ProductId {

    private final UUID value;

    private ProductId(UUID value) {
        this.value = Objects.requireNonNull(value, "ProductId value cannot be null");
    }

    public static ProductId of(UUID value) {
        return new ProductId(value);
    }

    public static ProductId of(String value) {
        Objects.requireNonNull(value, "ProductId string value cannot be null");
        return new ProductId(UUID.fromString(value));
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
