package com.erp.domain.product;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing EAN/GTIN barcode.
 * Supports EAN-8, EAN-13, and GTIN-14 formats.
 */
public final class EanCode {

    private static final Pattern EAN_PATTERN = Pattern.compile("^\\d{8}$|^\\d{13}$|^\\d{14}$");

    private final String value;

    private EanCode(String value) {
        Objects.requireNonNull(value, "EAN code cannot be null");
        String normalized = value.trim();
        if (!normalized.isEmpty() && !EAN_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid EAN/GTIN code format. Expected: 8, 13, or 14 digits");
        }
        this.value = normalized;
    }

    public static EanCode of(String value) {
        return new EanCode(value);
    }

    public static EanCode empty() {
        return new EanCode("");
    }

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return true; // Empty is valid (optional field)
        }
        return EAN_PATTERN.matcher(value.trim()).matches();
    }

    public String value() {
        return value;
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EanCode eanCode = (EanCode) o;
        return Objects.equals(value, eanCode.value);
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
