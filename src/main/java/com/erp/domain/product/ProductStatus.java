package com.erp.domain.product;

/**
 * Enum representing the status of a Product.
 */
public enum ProductStatus {
    ACTIVE,
    INACTIVE,
    DISCONTINUED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canBeSold() {
        return this == ACTIVE;
    }
}
