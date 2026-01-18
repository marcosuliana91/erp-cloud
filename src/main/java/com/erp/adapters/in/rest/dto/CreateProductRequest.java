package com.erp.adapters.in.rest.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO for creating a new Product.
 */
public record CreateProductRequest(

    @NotBlank(message = "Product code is required")
    @Size(max = 60, message = "Product code cannot exceed 60 characters")
    String code,

    @Size(max = 60, message = "Integration code cannot exceed 60 characters")
    String integrationCode,

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description cannot exceed 200 characters")
    String description,

    String detailedDescription,

    @NotBlank(message = "Unit is required")
    @Size(max = 6, message = "Unit cannot exceed 6 characters")
    String unit,

    @NotBlank(message = "NCM code is required")
    @Pattern(regexp = "^\\d{4}\\.\\d{2}\\.\\d{2}$|^\\d{8}$", message = "NCM code must be in format XXXX.XX.XX or 8 digits")
    String ncmCode,

    @Size(max = 14, message = "EAN code cannot exceed 14 characters")
    String eanCode,

    String type,

    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    String brand,

    @Size(max = 100, message = "Model cannot exceed 100 characters")
    String model,

    @Size(max = 100, message = "Family cannot exceed 100 characters")
    String family,

    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    @Digits(integer = 15, fraction = 4, message = "Unit price format is invalid")
    BigDecimal unitPrice,

    @DecimalMin(value = "0.0", message = "Cost price must be non-negative")
    @Digits(integer = 15, fraction = 4, message = "Cost price format is invalid")
    BigDecimal costPrice,

    @DecimalMin(value = "0.0", message = "Gross weight must be non-negative")
    @Digits(integer = 11, fraction = 4, message = "Gross weight format is invalid")
    BigDecimal grossWeight,

    @DecimalMin(value = "0.0", message = "Net weight must be non-negative")
    @Digits(integer = 11, fraction = 4, message = "Net weight format is invalid")
    BigDecimal netWeight,

    @DecimalMin(value = "0.0", message = "Height must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Height format is invalid")
    BigDecimal height,

    @DecimalMin(value = "0.0", message = "Width must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Width format is invalid")
    BigDecimal width,

    @DecimalMin(value = "0.0", message = "Depth must be non-negative")
    @Digits(integer = 8, fraction = 2, message = "Depth format is invalid")
    BigDecimal depth,

    String internalNotes,

    @Min(value = 0, message = "Stock quantity must be non-negative")
    Integer stockQuantity,

    @Min(value = 0, message = "Minimum stock must be non-negative")
    Integer minimumStock
) {}
