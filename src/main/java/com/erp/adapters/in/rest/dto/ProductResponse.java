package com.erp.adapters.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Product response.
 */
public record ProductResponse(
    UUID id,
    String code,
    String integrationCode,
    String description,
    String detailedDescription,
    String unit,
    String ncmCode,
    String eanCode,
    String type,
    String status,
    String brand,
    String model,
    String family,
    BigDecimal unitPrice,
    BigDecimal costPrice,
    BigDecimal grossWeight,
    BigDecimal netWeight,
    DimensionsResponse dimensions,
    String internalNotes,
    Integer stockQuantity,
    Integer minimumStock,
    boolean lowStock,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record DimensionsResponse(
        BigDecimal height,
        BigDecimal width,
        BigDecimal depth
    ) {}
}
