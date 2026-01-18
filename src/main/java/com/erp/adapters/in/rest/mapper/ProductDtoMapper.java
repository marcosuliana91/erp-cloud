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
            product.id().value(),
            product.code().value(),
            product.integrationCode(),
            product.description(),
            product.detailedDescription(),
            product.unit().value(),
            product.ncmCode().value(),
            product.eanCode().value(),
            product.type().name(),
            product.status().name(),
            product.brand(),
            product.model(),
            product.family(),
            product.unitPrice().amount(),
            product.costPrice().amount(),
            product.grossWeight().inKilograms(),
            product.netWeight().inKilograms(),
            new ProductResponse.DimensionsResponse(
                product.dimensions().height(),
                product.dimensions().width(),
                product.dimensions().depth()
            ),
            product.internalNotes(),
            product.stockQuantity(),
            product.minimumStock(),
            product.isLowStock(),
            product.createdAt(),
            product.updatedAt()
        );
    }
}
