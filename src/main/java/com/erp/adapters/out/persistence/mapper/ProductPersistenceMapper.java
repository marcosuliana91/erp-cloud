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

        entity.setId(product.id().value());
        entity.setCode(product.code().value());
        entity.setIntegrationCode(product.integrationCode());
        entity.setDescription(product.description());
        entity.setDetailedDescription(product.detailedDescription());
        entity.setUnit(product.unit().value());
        entity.setNcmCode(product.ncmCode().value());
        entity.setEanCode(product.eanCode().isEmpty() ? null : product.eanCode().value());
        entity.setType(mapType(product.type()));
        entity.setStatus(mapStatus(product.status()));
        entity.setBrand(product.brand());
        entity.setModel(product.model());
        entity.setFamily(product.family());
        entity.setUnitPrice(product.unitPrice().amount());
        entity.setCostPrice(product.costPrice().amount());
        entity.setGrossWeight(product.grossWeight().inKilograms());
        entity.setNetWeight(product.netWeight().inKilograms());
        entity.setHeight(product.dimensions().height());
        entity.setWidth(product.dimensions().width());
        entity.setDepth(product.dimensions().depth());
        entity.setInternalNotes(product.internalNotes());
        entity.setStockQuantity(product.stockQuantity());
        entity.setMinimumStock(product.minimumStock());
        entity.setCreatedAt(product.createdAt());
        entity.setUpdatedAt(product.updatedAt());

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
