package com.erp.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

/**
 * Value Object representing Unit of Measure.
 * Common units: UN (unit), KG, G, L, ML, M, CM, M2, M3, CX (box), PC (piece), etc.
 */
@Getter
@EqualsAndHashCode
public final class UnitOfMeasure {

    private static final int MAX_LENGTH = 6;

    private final String value;

    private UnitOfMeasure(String value) {
        Objects.requireNonNull(value, "Unit of measure cannot be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Unit of measure cannot be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Unit of measure cannot exceed " + MAX_LENGTH + " characters");
        }
        this.value = value.trim().toUpperCase();
    }

    public static UnitOfMeasure of(String value) {
        return new UnitOfMeasure(value);
    }

    // Common units
    public static UnitOfMeasure unit() {
        return new UnitOfMeasure("UN");
    }

    public static UnitOfMeasure kilogram() {
        return new UnitOfMeasure("KG");
    }

    public static UnitOfMeasure liter() {
        return new UnitOfMeasure("L");
    }

    public static UnitOfMeasure meter() {
        return new UnitOfMeasure("M");
    }

    @Override
    public String toString() {
        return value;
    }
}
