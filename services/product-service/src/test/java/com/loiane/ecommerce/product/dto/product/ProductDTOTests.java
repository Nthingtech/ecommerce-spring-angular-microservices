package com.loiane.ecommerce.product.dto.product;

import com.loiane.ecommerce.product.entity.ProductStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DTO Tests for Product Request Records")
class ProductDTOTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean()) {
            factory.afterPropertiesSet();
            validator = factory.getValidator();
        }
    }

    @DisplayName("UpdateProductInventoryRequest Tests")
    @Test
    void testUpdateProductInventoryRequestValid() {
        // Given
        var request = new UpdateProductInventoryRequest(50, 10);

        // When
        Set<ConstraintViolation<UpdateProductInventoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.stockQuantity()).isEqualTo(50);
        assertThat(request.lowStockThreshold()).isEqualTo(10);
    }

    @Test
    @DisplayName("UpdateProductInventoryRequest - Should fail validation with null stock quantity")
    void testUpdateProductInventoryRequestNullStockQuantity() {
        // Given
        var request = new UpdateProductInventoryRequest(null, 10);

        // When
        Set<ConstraintViolation<UpdateProductInventoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Stock quantity is required");
    }

    @Test
    @DisplayName("UpdateProductInventoryRequest - Should fail validation with negative stock quantity")
    void testUpdateProductInventoryRequestNegativeStockQuantity() {
        // Given
        var request = new UpdateProductInventoryRequest(-5, 10);

        // When
        Set<ConstraintViolation<UpdateProductInventoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Stock quantity must be positive");
    }

    @Test
    @DisplayName("UpdateProductInventoryRequest - Should fail validation with null low stock threshold")
    void testUpdateProductInventoryRequestNullLowStockThreshold() {
        // Given
        var request = new UpdateProductInventoryRequest(50, null);

        // When
        Set<ConstraintViolation<UpdateProductInventoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Low stock threshold is required");
    }

    @Test
    @DisplayName("UpdateProductInventoryRequest - Should fail validation with negative low stock threshold")
    void testUpdateProductInventoryRequestNegativeLowStockThreshold() {
        // Given
        var request = new UpdateProductInventoryRequest(50, -3);

        // When
        Set<ConstraintViolation<UpdateProductInventoryRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Low stock threshold must be positive");
    }

    @DisplayName("UpdateProductStatusRequest Tests")
    @Test
    void testUpdateProductStatusRequestValid() {
        // Given
        var request = new UpdateProductStatusRequest(ProductStatus.ACTIVE);

        // When
        Set<ConstraintViolation<UpdateProductStatusRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
        assertThat(request.status()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("UpdateProductStatusRequest - Should fail validation with null status")
    void testUpdateProductStatusRequestNullStatus() {
        // Given
        var request = new UpdateProductStatusRequest(null);

        // When
        Set<ConstraintViolation<UpdateProductStatusRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).contains("Product status is required");
    }

    @Test
    @DisplayName("UpdateProductStatusRequest - Should work with all status values")
    void testUpdateProductStatusRequestAllStatuses() {
        // Test all enum values
        for (ProductStatus status : ProductStatus.values()) {
            // Given
            var request = new UpdateProductStatusRequest(status);

            // When
            Set<ConstraintViolation<UpdateProductStatusRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.status()).isEqualTo(status);
        }
    }
}
