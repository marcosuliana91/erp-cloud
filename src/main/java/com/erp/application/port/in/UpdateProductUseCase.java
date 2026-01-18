package com.erp.application.port.in;

import com.erp.domain.product.Product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Use case port for updating a Product.
 */
public interface UpdateProductUseCase {

    Product execute(UpdateProductCommand command);

    record UpdateProductCommand(
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
        BigDecimal height,
        BigDecimal width,
        BigDecimal depth,
        String internalNotes,
        Integer minimumStock
    ) {}
}
