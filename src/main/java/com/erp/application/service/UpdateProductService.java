package com.erp.application.service;

import com.erp.application.port.in.UpdateProductUseCase;
import com.erp.application.port.out.ProductRepository;
import com.erp.domain.product.*;
import com.erp.shared.exception.DuplicateEntityException;
import com.erp.shared.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Service implementing the Update Product use case.
 * Orchestrates the update of an existing Product.
 */
@Service
@Transactional
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepository productRepository;

    public UpdateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(UpdateProductCommand command) {
        ProductId productId = ProductId.of(command.id());
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product", command.id()));

        validateUniqueCodeIfChanged(product, command.code());
        validateUniqueIntegrationCodeIfChanged(product, command.integrationCode());

        updateProduct(product, command);

        return productRepository.save(product);
    }

    private void validateUniqueCodeIfChanged(Product product, String newCode) {
        if (newCode != null && !newCode.isBlank()) {
            ProductCode newProductCode = ProductCode.of(newCode);
            if (!product.code().equals(newProductCode)) {
                if (productRepository.existsByCode(newProductCode)) {
                    throw new DuplicateEntityException("Product", "code", newCode);
                }
            }
        }
    }

    private void validateUniqueIntegrationCodeIfChanged(Product product, String newIntegrationCode) {
        if (newIntegrationCode != null && !newIntegrationCode.isBlank()) {
            String currentIntegrationCode = product.integrationCode();
            if (currentIntegrationCode == null || !currentIntegrationCode.equals(newIntegrationCode)) {
                if (productRepository.existsByIntegrationCode(newIntegrationCode)) {
                    throw new DuplicateEntityException("Product", "integrationCode", newIntegrationCode);
                }
            }
        }
    }

    private void updateProduct(Product product, UpdateProductCommand command) {
        // Update codes
        ProductCode code = command.code() != null && !command.code().isBlank()
            ? ProductCode.of(command.code())
            : product.code();
        NcmCode ncmCode = command.ncmCode() != null && !command.ncmCode().isBlank()
            ? NcmCode.of(command.ncmCode())
            : product.ncmCode();
        EanCode eanCode = command.eanCode() != null
            ? EanCode.of(command.eanCode())
            : product.eanCode();

        product.updateCodes(code, command.integrationCode(), ncmCode, eanCode);

        // Update details
        product.updateDetails(
            command.description(),
            command.detailedDescription(),
            command.brand(),
            command.model(),
            command.family(),
            command.internalNotes()
        );

        // Update unit
        if (command.unit() != null && !command.unit().isBlank()) {
            product.updateUnit(UnitOfMeasure.of(command.unit()));
        }

        // Update prices
        if (command.unitPrice() != null) {
            product.updatePrice(Money.of(command.unitPrice()));
        }

        if (command.costPrice() != null) {
            product.updateCostPrice(Money.of(command.costPrice()));
        }

        // Update physical attributes
        Weight grossWeight = command.grossWeight() != null
            ? Weight.ofKilograms(command.grossWeight())
            : product.grossWeight();
        Weight netWeight = command.netWeight() != null
            ? Weight.ofKilograms(command.netWeight())
            : product.netWeight();
        Dimensions dimensions = buildDimensions(command, product);

        product.updatePhysicalAttributes(grossWeight, netWeight, dimensions);

        // Update minimum stock
        if (command.minimumStock() != null) {
            product.updateMinimumStock(command.minimumStock());
        }

        // Update status
        if (command.status() != null && !command.status().isBlank()) {
            ProductStatus newStatus = ProductStatus.valueOf(command.status().toUpperCase());
            switch (newStatus) {
                case ACTIVE -> product.activate();
                case INACTIVE -> product.deactivate();
                case DISCONTINUED -> product.discontinue();
            }
        }
    }

    private Dimensions buildDimensions(UpdateProductCommand command, Product product) {
        BigDecimal height = command.height() != null ? command.height() : product.dimensions().height();
        BigDecimal width = command.width() != null ? command.width() : product.dimensions().width();
        BigDecimal depth = command.depth() != null ? command.depth() : product.dimensions().depth();
        return Dimensions.of(height, width, depth);
    }
}
