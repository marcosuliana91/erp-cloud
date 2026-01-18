package com.erp.application.port.in;

import com.erp.domain.product.Product;

import java.math.BigDecimal;

/**
 * Use case port for creating a Product.
 */
public interface CreateProductUseCase {

    Product execute(CreateProductCommand command);

    record CreateProductCommand(
        String code,
        String integrationCode,
        String description,
        String detailedDescription,
        String unit,
        String ncmCode,
        String eanCode,
        String type,
        String brand,
        String model,
        String family,
        BigDecimal unitPrice,
        BigDecimal costPrice,
        BigDecimal grossWeight,
        BigDecimal netWeight,
        BigDecimal height,
        BigDecimal width,
        BigDecimal depth,
        String internalNotes,
        Integer stockQuantity,
        Integer minimumStock
    ) {}
}
