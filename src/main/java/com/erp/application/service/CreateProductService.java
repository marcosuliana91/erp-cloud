package com.erp.application.service;

import com.erp.application.port.in.CreateProductUseCase;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.*;
import com.erp.shared.exception.DuplicateEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service implementing the Create Product use case.
 * Orchestrates the creation of a new Product.
 */
@Service
@Transactional
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(CreateProductCommand command) {
        validateUniqueCode(command.code());
        validateUniqueIntegrationCode(command.integrationCode());

        Product product = buildProduct(command);
        return productRepository.save(product);
    }

    private void validateUniqueCode(String code) {
        ProductCode productCode = ProductCode.of(code);
        if (productRepository.existsByCode(productCode)) {
            throw new DuplicateEntityException("Product", "code", code);
        }
    }

    private void validateUniqueIntegrationCode(String integrationCode) {
        if (integrationCode != null && !integrationCode.isBlank()) {
            if (productRepository.existsByIntegrationCode(integrationCode)) {
                throw new DuplicateEntityException("Product", "integrationCode", integrationCode);
            }
        }
    }

    private Product buildProduct(CreateProductCommand command) {
        Product.Builder builder = Product.builder()
            .code(command.code())
            .integrationCode(command.integrationCode())
            .description(command.description())
            .detailedDescription(command.detailedDescription())
            .unit(command.unit())
            .ncmCode(command.ncmCode())
            .eanCode(command.eanCode())
            .brand(command.brand())
            .model(command.model())
            .family(command.family())
            .internalNotes(command.internalNotes())
            .stockQuantity(command.stockQuantity())
            .minimumStock(command.minimumStock());

        if (command.type() != null && !command.type().isBlank()) {
            builder.type(ProductType.valueOf(command.type().toUpperCase()));
        }

        if (command.unitPrice() != null) {
            builder.unitPrice(Money.of(command.unitPrice()));
        }

        if (command.costPrice() != null) {
            builder.costPrice(Money.of(command.costPrice()));
        }

        if (command.grossWeight() != null) {
            builder.grossWeight(Weight.ofKilograms(command.grossWeight()));
        }

        if (command.netWeight() != null) {
            builder.netWeight(Weight.ofKilograms(command.netWeight()));
        }

        if (hasDimensions(command)) {
            builder.dimensions(Dimensions.of(
                command.height() != null ? command.height() : BigDecimal.ZERO,
                command.width() != null ? command.width() : BigDecimal.ZERO,
                command.depth() != null ? command.depth() : BigDecimal.ZERO
            ));
        }

        return builder.build();
    }

    private boolean hasDimensions(CreateProductCommand command) {
        return command.height() != null || command.width() != null || command.depth() != null;
    }
}
