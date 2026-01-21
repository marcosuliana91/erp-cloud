package com.erp.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing NCM (Nomenclatura Comum do Mercosul) code.
 * Format: XXXX.XX.XX (8 digits)
 */
@Getter
@EqualsAndHashCode
public final class NcmCode {

    private static final Pattern NCM_PATTERN = Pattern.compile("^\\d{4}\\.\\d{2}\\.\\d{2}$");
    private static final Pattern NCM_DIGITS_ONLY = Pattern.compile("^\\d{8}$");

    private final String value;

    private NcmCode(String value) {
        Objects.requireNonNull(value, "NCM code cannot be null");
        String normalized = normalize(value);
        if (!isValid(normalized)) {
            throw new IllegalArgumentException("Invalid NCM code format. Expected: XXXX.XX.XX or 8 digits");
        }
        this.value = normalized;
    }

    public static NcmCode of(String value) {
        return new NcmCode(value);
    }

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return NCM_PATTERN.matcher(value).matches();
    }

    private static String normalize(String value) {
        String trimmed = value.trim();
        if (NCM_DIGITS_ONLY.matcher(trimmed).matches()) {
            return trimmed.substring(0, 4) + "." + trimmed.substring(4, 6) + "." + trimmed.substring(6, 8);
        }
        return trimmed;
    }

    public String digitsOnly() {
        return value.replace(".", "");
    }

    @Override
    public String toString() {
        return value;
    }
}
