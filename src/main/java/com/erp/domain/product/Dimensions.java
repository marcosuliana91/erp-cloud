package com.erp.domain.product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object representing product dimensions in centimeters.
 */
public final class Dimensions {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final BigDecimal height;
    private final BigDecimal width;
    private final BigDecimal depth;

    private Dimensions(BigDecimal height, BigDecimal width, BigDecimal depth) {
        this.height = normalize(height, "Height");
        this.width = normalize(width, "Width");
        this.depth = normalize(depth, "Depth");
    }

    private static BigDecimal normalize(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
        return value.setScale(SCALE, ROUNDING);
    }

    public static Dimensions of(BigDecimal height, BigDecimal width, BigDecimal depth) {
        return new Dimensions(height, width, depth);
    }

    public static Dimensions of(double height, double width, double depth) {
        return new Dimensions(
            BigDecimal.valueOf(height),
            BigDecimal.valueOf(width),
            BigDecimal.valueOf(depth)
        );
    }

    public static Dimensions zero() {
        return new Dimensions(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public BigDecimal height() {
        return height;
    }

    public BigDecimal width() {
        return width;
    }

    public BigDecimal depth() {
        return depth;
    }

    public BigDecimal volumeInCubicCentimeters() {
        return height.multiply(width).multiply(depth);
    }

    public BigDecimal volumeInCubicMeters() {
        return volumeInCubicCentimeters().divide(BigDecimal.valueOf(1_000_000), 6, ROUNDING);
    }

    public boolean isZero() {
        return height.compareTo(BigDecimal.ZERO) == 0
            && width.compareTo(BigDecimal.ZERO) == 0
            && depth.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return height.compareTo(that.height) == 0
            && width.compareTo(that.width) == 0
            && depth.compareTo(that.depth) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, width, depth);
    }

    @Override
    public String toString() {
        return height + " x " + width + " x " + depth + " cm";
    }
}
