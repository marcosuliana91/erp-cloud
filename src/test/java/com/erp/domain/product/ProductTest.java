package com.erp.domain.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Domain Tests")
class ProductTest {

    @Test
    @DisplayName("Should create a valid product with required fields")
    void shouldCreateValidProduct() {
        Product product = Product.builder()
            .code("PROD-001")
            .description("Test Product")
            .unit("UN")
            .ncmCode("1234.56.78")
            .build();

        assertNotNull(product.getId());
        assertEquals("PROD-001", product.getCode().getValue());
        assertEquals("Test Product", product.getDescription());
        assertEquals("UN", product.getUnit().getValue());
        assertEquals("1234.56.78", product.getNcmCode().getValue());
        assertEquals(ProductStatus.ACTIVE, product.getStatus());
        assertEquals(ProductType.PRODUCT, product.getType());
    }

    @Test
    @DisplayName("Should throw exception when code is null")
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(NullPointerException.class, () ->
            Product.builder()
                .description("Test Product")
                .unit("UN")
                .ncmCode("1234.56.78")
                .build()
        );
    }

    @Test
    @DisplayName("Should throw exception when description is null")
    void shouldThrowExceptionWhenDescriptionIsNull() {
        assertThrows(NullPointerException.class, () ->
            Product.builder()
                .code("PROD-001")
                .unit("UN")
                .ncmCode("1234.56.78")
                .build()
        );
    }

    @Test
    @DisplayName("Should update price correctly")
    void shouldUpdatePrice() {
        Product product = createValidProduct();
        Money newPrice = Money.of(new BigDecimal("199.99"));

        product.updatePrice(newPrice);

        assertEquals(newPrice, product.getUnitPrice());
    }

    @Test
    @DisplayName("Should activate product")
    void shouldActivateProduct() {
        Product product = createValidProduct();
        product.deactivate();

        product.activate();

        assertEquals(ProductStatus.ACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Should deactivate product")
    void shouldDeactivateProduct() {
        Product product = createValidProduct();

        product.deactivate();

        assertEquals(ProductStatus.INACTIVE, product.getStatus());
    }

    @Test
    @DisplayName("Should discontinue product")
    void shouldDiscontinueProduct() {
        Product product = createValidProduct();

        product.discontinue();

        assertEquals(ProductStatus.DISCONTINUED, product.getStatus());
    }

    @Test
    @DisplayName("Should not activate discontinued product")
    void shouldNotActivateDiscontinuedProduct() {
        Product product = createValidProduct();
        product.discontinue();

        assertThrows(IllegalStateException.class, () -> product.activate());
    }

    @Test
    @DisplayName("Should detect low stock")
    void shouldDetectLowStock() {
        Product product = Product.builder()
            .code("PROD-001")
            .description("Test Product")
            .unit("UN")
            .ncmCode("1234.56.78")
            .stockQuantity(5)
            .minimumStock(10)
            .build();

        assertTrue(product.isLowStock());
    }

    @Test
    @DisplayName("Should adjust stock correctly")
    void shouldAdjustStock() {
        Product product = Product.builder()
            .code("PROD-001")
            .description("Test Product")
            .unit("UN")
            .ncmCode("1234.56.78")
            .stockQuantity(10)
            .build();

        product.adjustStock(5);

        assertEquals(15, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should not allow negative stock")
    void shouldNotAllowNegativeStock() {
        Product product = Product.builder()
            .code("PROD-001")
            .description("Test Product")
            .unit("UN")
            .ncmCode("1234.56.78")
            .stockQuantity(5)
            .build();

        assertThrows(IllegalArgumentException.class, () -> product.adjustStock(-10));
    }

    private Product createValidProduct() {
        return Product.builder()
            .code("PROD-001")
            .description("Test Product")
            .unit("UN")
            .ncmCode("1234.56.78")
            .unitPrice(Money.of(new BigDecimal("99.99")))
            .build();
    }
}
