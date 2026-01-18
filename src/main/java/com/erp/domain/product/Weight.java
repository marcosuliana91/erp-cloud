package com.erp.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing weight in kilograms.
 */
public final class Weight {

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final BigDecimal valueInKg;

    private Weight(BigDecimal valueInKg) {
        Objects.requireNonNull(valueInKg, "Weight value cannot be null");
        if (valueInKg.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.valueInKg = valueInKg.setScale(SCALE, ROUNDING);
    }

    public static Weight ofKilograms(BigDecimal value) {
        return new Weight(value);
    }

    public static Weight ofKilograms(double value) {
        return new Weight(BigDecimal.valueOf(value));
    }

    public static Weight ofGrams(BigDecimal value) {
        return new Weight(value.divide(BigDecimal.valueOf(1000), SCALE, ROUNDING));
    }

    public static Weight zero() {
        return new Weight(BigDecimal.ZERO);
    }

    public BigDecimal inKilograms() {
        return valueInKg;
    }

    public BigDecimal inGrams() {
        return valueInKg.multiply(BigDecimal.valueOf(1000));
    }

    public boolean isZero() {
        return valueInKg.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight weight = (Weight) o;
        return valueInKg.compareTo(weight.valueInKg) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueInKg);
    }

    @Override
    public String toString() {
        return valueInKg.toString() + " kg";
    }
}
