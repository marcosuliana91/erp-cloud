package com.erp.domain.product;

/**
 * Enum representing the type of a Product.
 */
public enum ProductType {
    PRODUCT,           // Physical product
    SERVICE,           // Service
    RAW_MATERIAL,      // Raw material for production
    CONSUMABLE,        // Consumable items
    ASSET;             // Fixed asset

    public boolean isPhysical() {
        return this == PRODUCT || this == RAW_MATERIAL || this == CONSUMABLE || this == ASSET;
    }

    public boolean requiresStock() {
        return isPhysical();
    }
}
