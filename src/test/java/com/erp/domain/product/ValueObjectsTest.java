package com.erp.domain.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Value Objects Tests")
class ValueObjectsTest {

    // ProductId Tests
    @Test
    @DisplayName("Should create ProductId from UUID")
    void shouldCreateProductIdFromUuid() {
        UUID uuid = UUID.randomUUID();
        ProductId productId = ProductId.of(uuid);

        assertEquals(uuid, productId.value());
    }

    @Test
    @DisplayName("Should create ProductId from string")
    void shouldCreateProductIdFromString() {
        String uuidString = "550e8400-e29b-41d4-a716-446655440000";
        ProductId productId = ProductId.of(uuidString);

        assertEquals(UUID.fromString(uuidString), productId.value());
    }

    @Test
    @DisplayName("Should generate new ProductId")
    void shouldGenerateNewProductId() {
        ProductId productId = ProductId.generate();

        assertNotNull(productId.value());
    }

    // ProductCode Tests
    @Test
    @DisplayName("Should create valid ProductCode")
    void shouldCreateValidProductCode() {
        ProductCode code = ProductCode.of("PROD-001");

        assertEquals("PROD-001", code.value());
    }

    @Test
    @DisplayName("Should throw exception for blank ProductCode")
    void shouldThrowExceptionForBlankProductCode() {
        assertThrows(IllegalArgumentException.class, () -> ProductCode.of("   "));
    }

    // NcmCode Tests
    @ParameterizedTest
    @ValueSource(strings = {"1234.56.78", "12345678"})
    @DisplayName("Should create valid NcmCode")
    void shouldCreateValidNcmCode(String input) {
        NcmCode ncmCode = NcmCode.of(input);

        assertEquals("1234.56.78", ncmCode.value());
    }

    @Test
    @DisplayName("Should throw exception for invalid NcmCode")
    void shouldThrowExceptionForInvalidNcmCode() {
        assertThrows(IllegalArgumentException.class, () -> NcmCode.of("123"));
    }

    // EanCode Tests
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890123", "12345678901234"})
    @DisplayName("Should create valid EanCode")
    void shouldCreateValidEanCode(String input) {
        EanCode eanCode = EanCode.of(input);

        assertEquals(input, eanCode.value());
    }

    @Test
    @DisplayName("Should create empty EanCode")
    void shouldCreateEmptyEanCode() {
        EanCode eanCode = EanCode.empty();

        assertTrue(eanCode.isEmpty());
    }

    // UnitOfMeasure Tests
    @Test
    @DisplayName("Should create UnitOfMeasure uppercase")
    void shouldCreateUnitOfMeasureUppercase() {
        UnitOfMeasure unit = UnitOfMeasure.of("un");

        assertEquals("UN", unit.value());
    }

    @Test
    @DisplayName("Should create common UnitOfMeasure")
    void shouldCreateCommonUnitOfMeasure() {
        assertEquals("UN", UnitOfMeasure.unit().value());
        assertEquals("KG", UnitOfMeasure.kilogram().value());
        assertEquals("L", UnitOfMeasure.liter().value());
        assertEquals("M", UnitOfMeasure.meter().value());
    }

    // Money Tests
    @Test
    @DisplayName("Should create Money with correct scale")
    void shouldCreateMoneyWithCorrectScale() {
        Money money = Money.of(new BigDecimal("99.99"));

        assertEquals(new BigDecimal("99.9900"), money.amount());
    }

    @Test
    @DisplayName("Should add Money correctly")
    void shouldAddMoney() {
        Money a = Money.of(new BigDecimal("10.00"));
        Money b = Money.of(new BigDecimal("5.50"));

        Money result = a.add(b);

        assertEquals(new BigDecimal("15.5000"), result.amount());
    }

    @Test
    @DisplayName("Should throw exception for negative Money")
    void shouldThrowExceptionForNegativeMoney() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(new BigDecimal("-10.00")));
    }

    // Weight Tests
    @Test
    @DisplayName("Should create Weight in kilograms")
    void shouldCreateWeightInKilograms() {
        Weight weight = Weight.ofKilograms(new BigDecimal("2.5"));

        assertEquals(new BigDecimal("2.5000"), weight.inKilograms());
    }

    @Test
    @DisplayName("Should convert Weight to grams")
    void shouldConvertWeightToGrams() {
        Weight weight = Weight.ofKilograms(new BigDecimal("1.5"));

        assertEquals(new BigDecimal("1500.0000"), weight.inGrams());
    }

    // Dimensions Tests
    @Test
    @DisplayName("Should create Dimensions")
    void shouldCreateDimensions() {
        Dimensions dimensions = Dimensions.of(10.0, 20.0, 30.0);

        assertEquals(new BigDecimal("10.00"), dimensions.height());
        assertEquals(new BigDecimal("20.00"), dimensions.width());
        assertEquals(new BigDecimal("30.00"), dimensions.depth());
    }

    @Test
    @DisplayName("Should calculate volume")
    void shouldCalculateVolume() {
        Dimensions dimensions = Dimensions.of(10.0, 20.0, 30.0);

        // Use compareTo for BigDecimal comparison (ignores scale differences)
        assertEquals(0, new BigDecimal("6000").compareTo(dimensions.volumeInCubicCentimeters()));
    }
}
