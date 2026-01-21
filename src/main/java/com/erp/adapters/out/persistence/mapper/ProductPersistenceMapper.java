package com.erp.adapters.out.persistence.mapper;

import com.erp.domain.product.*;
import com.erp.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Mapper for converting between Product domain entity and ProductEntity JPA entity.
 */
@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();

        entity.setId(product.getId().getValue());
        entity.setCode(product.getCode().getValue());
        entity.setIntegrationCode(product.getIntegrationCode());
        entity.setDescription(product.getDescription());
        entity.setDetailedDescription(product.getDetailedDescription());
        entity.setUnit(product.getUnit().getValue());
        entity.setNcmCode(product.getNcmCode().getValue());
        entity.setEanCode(product.getEanCode().isEmpty() ? null : product.getEanCode().getValue());
        entity.setType(mapType(product.getType()));
        entity.setStatus(mapStatus(product.getStatus()));
        entity.setBrand(product.getBrand());
        entity.setModel(product.getModel());
        entity.setFamily(product.getFamily());
        entity.setUnitPrice(product.getUnitPrice().getAmount());
        entity.setCostPrice(product.getCostPrice().getAmount());
        entity.setGrossWeight(product.getGrossWeight().inKilograms());
        entity.setNetWeight(product.getNetWeight().inKilograms());
        entity.setHeight(product.getDimensions().getHeight());
        entity.setWidth(product.getDimensions().getWidth());
        entity.setDepth(product.getDimensions().getDepth());
        entity.setInternalNotes(product.getInternalNotes());
        entity.setStockQuantity(product.getStockQuantity());
        entity.setMinimumStock(product.getMinimumStock());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());

        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        return Product.builder()
            .id(ProductId.of(entity.getId()))
            .code(ProductCode.of(entity.getCode()))
            .integrationCode(entity.getIntegrationCode())
            .description(entity.getDescription())
            .detailedDescription(entity.getDetailedDescription())
            .unit(UnitOfMeasure.of(entity.getUnit()))
            .ncmCode(NcmCode.of(entity.getNcmCode()))
            .eanCode(entity.getEanCode() != null ? EanCode.of(entity.getEanCode()) : EanCode.empty())
            .type(mapType(entity.getType()))
            .status(mapStatus(entity.getStatus()))
            .brand(entity.getBrand())
            .model(entity.getModel())
            .family(entity.getFamily())
            .unitPrice(Money.of(entity.getUnitPrice() != null ? entity.getUnitPrice() : BigDecimal.ZERO))
            .costPrice(Money.of(entity.getCostPrice() != null ? entity.getCostPrice() : BigDecimal.ZERO))
            .grossWeight(Weight.ofKilograms(entity.getGrossWeight() != null ? entity.getGrossWeight() : BigDecimal.ZERO))
            .netWeight(Weight.ofKilograms(entity.getNetWeight() != null ? entity.getNetWeight() : BigDecimal.ZERO))
            .dimensions(Dimensions.of(
                entity.getHeight() != null ? entity.getHeight() : BigDecimal.ZERO,
                entity.getWidth() != null ? entity.getWidth() : BigDecimal.ZERO,
                entity.getDepth() != null ? entity.getDepth() : BigDecimal.ZERO
            ))
            .internalNotes(entity.getInternalNotes())
            .stockQuantity(entity.getStockQuantity())
            .minimumStock(entity.getMinimumStock())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    private ProductEntity.ProductTypeEnum mapType(ProductType type) {
        return switch (type) {
            case PRODUCT -> ProductEntity.ProductTypeEnum.PRODUCT;
            case SERVICE -> ProductEntity.ProductTypeEnum.SERVICE;
            case RAW_MATERIAL -> ProductEntity.ProductTypeEnum.RAW_MATERIAL;
            case CONSUMABLE -> ProductEntity.ProductTypeEnum.CONSUMABLE;
            case ASSET -> ProductEntity.ProductTypeEnum.ASSET;
        };
    }

    private ProductType mapType(ProductEntity.ProductTypeEnum type) {
        return switch (type) {
            case PRODUCT -> ProductType.PRODUCT;
            case SERVICE -> ProductType.SERVICE;
            case RAW_MATERIAL -> ProductType.RAW_MATERIAL;
            case CONSUMABLE -> ProductType.CONSUMABLE;
            case ASSET -> ProductType.ASSET;
        };
    }

    private ProductEntity.ProductStatusEnum mapStatus(ProductStatus status) {
        return switch (status) {
            case ACTIVE -> ProductEntity.ProductStatusEnum.ACTIVE;
            case INACTIVE -> ProductEntity.ProductStatusEnum.INACTIVE;
            case DISCONTINUED -> ProductEntity.ProductStatusEnum.DISCONTINUED;
        };
    }

    private ProductStatus mapStatus(ProductEntity.ProductStatusEnum status) {
        return switch (status) {
            case ACTIVE -> ProductStatus.ACTIVE;
            case INACTIVE -> ProductStatus.INACTIVE;
            case DISCONTINUED -> ProductStatus.DISCONTINUED;
        };
    }
}
