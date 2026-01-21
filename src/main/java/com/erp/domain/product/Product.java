package com.erp.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product aggregate root - the main domain entity.
 * Contains business rules and invariants.
 */
@Getter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "code", "description", "status"})
public final class Product {

    private final ProductId id;
    private ProductCode code;
    private String integrationCode;
    private String description;
    private String detailedDescription;
    private UnitOfMeasure unit;
    private NcmCode ncmCode;
    private EanCode eanCode;
    private ProductType type;
    private ProductStatus status;
    private String brand;
    private String model;
    private String family;
    private Money unitPrice;
    private Money costPrice;
    private Weight grossWeight;
    private Weight netWeight;
    private Dimensions dimensions;
    private String internalNotes;
    private Integer stockQuantity;
    private Integer minimumStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Product(Builder builder) {
        validateRequiredFields(builder);

        this.id = builder.id != null ? builder.id : ProductId.generate();
        this.code = builder.code;
        this.integrationCode = builder.integrationCode;
        this.description = builder.description;
        this.detailedDescription = builder.detailedDescription;
        this.unit = builder.unit;
        this.ncmCode = builder.ncmCode;
        this.eanCode = builder.eanCode != null ? builder.eanCode : EanCode.empty();
        this.type = builder.type != null ? builder.type : ProductType.PRODUCT;
        this.status = builder.status != null ? builder.status : ProductStatus.ACTIVE;
        this.brand = builder.brand;
        this.model = builder.model;
        this.family = builder.family;
        this.unitPrice = builder.unitPrice != null ? builder.unitPrice : Money.zero();
        this.costPrice = builder.costPrice != null ? builder.costPrice : Money.zero();
        this.grossWeight = builder.grossWeight != null ? builder.grossWeight : Weight.zero();
        this.netWeight = builder.netWeight != null ? builder.netWeight : Weight.zero();
        this.dimensions = builder.dimensions != null ? builder.dimensions : Dimensions.zero();
        this.internalNotes = builder.internalNotes;
        this.stockQuantity = builder.stockQuantity != null ? builder.stockQuantity : 0;
        this.minimumStock = builder.minimumStock != null ? builder.minimumStock : 0;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    private void validateRequiredFields(Builder builder) {
        Objects.requireNonNull(builder.code, "Product code is required");
        Objects.requireNonNull(builder.description, "Product description is required");
        Objects.requireNonNull(builder.unit, "Unit of measure is required");
        Objects.requireNonNull(builder.ncmCode, "NCM code is required");

        if (builder.description.isBlank()) {
            throw new IllegalArgumentException("Product description cannot be blank");
        }
        if (builder.description.length() > 200) {
            throw new IllegalArgumentException("Product description cannot exceed 200 characters");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Behavior methods

    public void activate() {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new IllegalStateException("Cannot activate a discontinued product");
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(Money newPrice) {
        Objects.requireNonNull(newPrice, "Price cannot be null");
        this.unitPrice = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCostPrice(Money newCostPrice) {
        Objects.requireNonNull(newCostPrice, "Cost price cannot be null");
        this.costPrice = newCostPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void adjustStock(int quantity) {
        int newQuantity = this.stockQuantity + quantity;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = newQuantity;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return this.stockQuantity <= this.minimumStock;
    }

    public boolean canBeSold() {
        return this.status.canBeSold() && this.stockQuantity > 0;
    }

    public Money calculateProfit() {
        if (costPrice.isZero()) {
            return unitPrice;
        }
        return unitPrice.subtract(costPrice);
    }

    public void updateDetails(String description, String detailedDescription,
                             String brand, String model, String family, String internalNotes) {
        if (description != null && !description.isBlank()) {
            if (description.length() > 200) {
                throw new IllegalArgumentException("Description cannot exceed 200 characters");
            }
            this.description = description;
        }
        this.detailedDescription = detailedDescription;
        this.brand = brand;
        this.model = model;
        this.family = family;
        this.internalNotes = internalNotes;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePhysicalAttributes(Weight grossWeight, Weight netWeight, Dimensions dimensions) {
        this.grossWeight = grossWeight != null ? grossWeight : Weight.zero();
        this.netWeight = netWeight != null ? netWeight : Weight.zero();
        this.dimensions = dimensions != null ? dimensions : Dimensions.zero();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCodes(ProductCode code, String integrationCode, NcmCode ncmCode, EanCode eanCode) {
        Objects.requireNonNull(code, "Product code is required");
        Objects.requireNonNull(ncmCode, "NCM code is required");
        this.code = code;
        this.integrationCode = integrationCode;
        this.ncmCode = ncmCode;
        this.eanCode = eanCode != null ? eanCode : EanCode.empty();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateUnit(UnitOfMeasure unit) {
        Objects.requireNonNull(unit, "Unit of measure is required");
        this.unit = unit;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateMinimumStock(Integer minimumStock) {
        if (minimumStock != null && minimumStock < 0) {
            throw new IllegalArgumentException("Minimum stock cannot be negative");
        }
        this.minimumStock = minimumStock != null ? minimumStock : 0;
        this.updatedAt = LocalDateTime.now();
    }

    // Builder pattern for creating Product instances
    public static final class Builder {
        private ProductId id;
        private ProductCode code;
        private String integrationCode;
        private String description;
        private String detailedDescription;
        private UnitOfMeasure unit;
        private NcmCode ncmCode;
        private EanCode eanCode;
        private ProductType type;
        private ProductStatus status;
        private String brand;
        private String model;
        private String family;
        private Money unitPrice;
        private Money costPrice;
        private Weight grossWeight;
        private Weight netWeight;
        private Dimensions dimensions;
        private String internalNotes;
        private Integer stockQuantity;
        private Integer minimumStock;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private Builder() {}

        public Builder id(ProductId id) {
            this.id = id;
            return this;
        }

        public Builder code(ProductCode code) {
            this.code = code;
            return this;
        }

        public Builder code(String code) {
            this.code = ProductCode.of(code);
            return this;
        }

        public Builder integrationCode(String integrationCode) {
            this.integrationCode = integrationCode;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder detailedDescription(String detailedDescription) {
            this.detailedDescription = detailedDescription;
            return this;
        }

        public Builder unit(UnitOfMeasure unit) {
            this.unit = unit;
            return this;
        }

        public Builder unit(String unit) {
            this.unit = UnitOfMeasure.of(unit);
            return this;
        }

        public Builder ncmCode(NcmCode ncmCode) {
            this.ncmCode = ncmCode;
            return this;
        }

        public Builder ncmCode(String ncmCode) {
            this.ncmCode = NcmCode.of(ncmCode);
            return this;
        }

        public Builder eanCode(EanCode eanCode) {
            this.eanCode = eanCode;
            return this;
        }

        public Builder eanCode(String eanCode) {
            this.eanCode = eanCode != null && !eanCode.isBlank() ? EanCode.of(eanCode) : EanCode.empty();
            return this;
        }

        public Builder type(ProductType type) {
            this.type = type;
            return this;
        }

        public Builder status(ProductStatus status) {
            this.status = status;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder family(String family) {
            this.family = family;
            return this;
        }

        public Builder unitPrice(Money unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder costPrice(Money costPrice) {
            this.costPrice = costPrice;
            return this;
        }

        public Builder grossWeight(Weight grossWeight) {
            this.grossWeight = grossWeight;
            return this;
        }

        public Builder netWeight(Weight netWeight) {
            this.netWeight = netWeight;
            return this;
        }

        public Builder dimensions(Dimensions dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder internalNotes(String internalNotes) {
            this.internalNotes = internalNotes;
            return this;
        }

        public Builder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder minimumStock(Integer minimumStock) {
            this.minimumStock = minimumStock;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
