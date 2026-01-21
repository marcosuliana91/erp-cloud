package com.erp.adapters.in.rest.mapper;

import com.erp.adapters.in.rest.dto.CreateProductRequest;
import com.erp.adapters.in.rest.dto.ProductResponse;
import com.erp.adapters.in.rest.dto.UpdateProductRequest;
import com.erp.application.port.in.CreateProductUseCase;
import com.erp.application.port.in.UpdateProductUseCase;
import com.erp.domain.product.Product;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper for converting between DTOs and domain commands/responses.
 */
@Component
public class ProductDtoMapper {

    public CreateProductUseCase.CreateProductCommand toCreateCommand(CreateProductRequest request) {
        return new CreateProductUseCase.CreateProductCommand(
            request.code(),
            request.integrationCode(),
            request.description(),
            request.detailedDescription(),
            request.unit(),
            request.ncmCode(),
            request.eanCode(),
            request.type(),
            request.brand(),
            request.model(),
            request.family(),
            request.unitPrice(),
            request.costPrice(),
            request.grossWeight(),
            request.netWeight(),
            request.height(),
            request.width(),
            request.depth(),
            request.internalNotes(),
            request.stockQuantity(),
            request.minimumStock()
        );
    }

    public UpdateProductUseCase.UpdateProductCommand toUpdateCommand(UUID id, UpdateProductRequest request) {
        return new UpdateProductUseCase.UpdateProductCommand(
            id,
            request.code(),
            request.integrationCode(),
            request.description(),
            request.detailedDescription(),
            request.unit(),
            request.ncmCode(),
            request.eanCode(),
            request.type(),
            request.status(),
            request.brand(),
            request.model(),
            request.family(),
            request.unitPrice(),
            request.costPrice(),
            request.grossWeight(),
            request.netWeight(),
            request.height(),
            request.width(),
            request.depth(),
            request.internalNotes(),
            request.minimumStock()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId().getValue(),
            product.getCode().getValue(),
            product.getIntegrationCode(),
            product.getDescription(),
            product.getDetailedDescription(),
            product.getUnit().getValue(),
            product.getNcmCode().getValue(),
            product.getEanCode().getValue(),
            product.getType().name(),
            product.getStatus().name(),
            product.getBrand(),
            product.getModel(),
            product.getFamily(),
            product.getUnitPrice().getAmount(),
            product.getCostPrice().getAmount(),
            product.getGrossWeight().inKilograms(),
            product.getNetWeight().inKilograms(),
            new ProductResponse.DimensionsResponse(
                product.getDimensions().getHeight(),
                product.getDimensions().getWidth(),
                product.getDimensions().getDepth()
            ),
            product.getInternalNotes(),
            product.getStockQuantity(),
            product.getMinimumStock(),
            product.isLowStock(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
