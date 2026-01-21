package com.erp.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Product persistence.
 * Mapped to the 'products' table.
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_products_code", columnList = "code", unique = true),
    @Index(name = "idx_products_integration_code", columnList = "integration_code"),
    @Index(name = "idx_products_description", columnList = "description"),
    @Index(name = "idx_products_family", columnList = "family"),
    @Index(name = "idx_products_brand", columnList = "brand"),
    @Index(name = "idx_products_status", columnList = "status"),
    @Index(name = "idx_products_ncm_code", columnList = "ncm_code")
})
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 60)
    private String code;

    @Column(name = "integration_code", length = 60)
    private String integrationCode;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(name = "unit", nullable = false, length = 6)
    private String unit;

    @Column(name = "ncm_code", nullable = false, length = 10)
    private String ncmCode;

    @Column(name = "ean_code", length = 14)
    private String eanCode;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProductTypeEnum type;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProductStatusEnum status;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "family", length = 100)
    private String family;

    @Column(name = "unit_price", precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "cost_price", precision = 19, scale = 4)
    private BigDecimal costPrice;

    @Column(name = "gross_weight", precision = 15, scale = 4)
    private BigDecimal grossWeight;

    @Column(name = "net_weight", precision = 15, scale = 4)
    private BigDecimal netWeight;

    @Column(name = "height", precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "depth", precision = 10, scale = 2)
    private BigDecimal depth;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "minimum_stock")
    private Integer minimumStock;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum ProductTypeEnum {
        PRODUCT,
        SERVICE,
        RAW_MATERIAL,
        CONSUMABLE,
        ASSET
    }

    public enum ProductStatusEnum {
        ACTIVE,
        INACTIVE,
        DISCONTINUED
    }
}
