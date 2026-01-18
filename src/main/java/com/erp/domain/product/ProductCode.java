package com.erp.domain.product;

import java.util.Objects;

/**
 * Value Object representing a Product code (SKU).
 * This is the business identifier visible in the ERP.
 */
public final class ProductCode {

    private static final int MAX_LENGTH = 60;
    private final String value;

    private ProductCode(String value) {
        Objects.requireNonNull(value, "Product code cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Product code cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Product code cannot exceed " + MAX_LENGTH + " characters");
        }
        this.value = value.trim();
    }

    public static ProductCode of(String value) {
        return new ProductCode(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCode that = (ProductCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
