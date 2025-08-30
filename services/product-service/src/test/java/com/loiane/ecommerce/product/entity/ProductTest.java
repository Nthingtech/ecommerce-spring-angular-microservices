package com.loiane.ecommerce.product.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Product Entity Tests")
class ProductTest {

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .name("Electronics")
                .slug("electronics")
                .build();

        product = Product.builder()
                .name("Test Product")
                .sku("TEST-001")
                .basePrice(new BigDecimal("99.99"))
                .status(ProductStatus.ACTIVE)
                .category(category)
                .stockQuantity(100)
                .reservedQuantity(10)
                .lowStockThreshold(20)
                .trackInventory(true)
                .build();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create product with basic fields")
        void shouldCreateProductWithBasicFields() {
            // Given
            String name = "Test Product";
            String sku = "TEST-SKU-001";
            BigDecimal price = new BigDecimal("49.99");

            // When
            Product testProduct = new Product(name, sku, price);

            // Then
            assertThat(testProduct.getName()).isEqualTo(name);
            assertThat(testProduct.getSku()).isEqualTo(sku);
            assertThat(testProduct.getBasePrice()).isEqualTo(price);
            assertThat(testProduct.getStatus()).isEqualTo(ProductStatus.ACTIVE);
            assertThat(testProduct.getStockQuantity()).isZero();
            assertThat(testProduct.getTrackInventory()).isTrue(); // Default value is true
        }
    }

    @Nested
    @DisplayName("Stock Management Tests")
    class StockManagementTests {

        @Test
        @DisplayName("Should calculate available quantity correctly")
        void shouldCalculateAvailableQuantity() {
            // Given
            product.setStockQuantity(100);
            product.setReservedQuantity(25);

            // When
            Integer availableQuantity = product.getAvailableQuantity();

            // Then
            assertThat(availableQuantity).isEqualTo(75);
        }

        @Test
        @DisplayName("Should return true when product is in stock")
        void shouldReturnTrueWhenInStock() {
            // Given
            product.setStockQuantity(50);
            product.setReservedQuantity(10);
            product.setTrackInventory(true);

            // When & Then
            assertThat(product.isInStock()).isTrue();
        }

        @Test
        @DisplayName("Should return false when product is out of stock")
        void shouldReturnFalseWhenOutOfStock() {
            // Given
            product.setStockQuantity(10);
            product.setReservedQuantity(10);
            product.setTrackInventory(true);

            // When & Then
            assertThat(product.isInStock()).isFalse();
        }

        @Test
        @DisplayName("Should return true when inventory tracking is disabled")
        void shouldReturnTrueWhenInventoryTrackingDisabled() {
            // Given
            product.setStockQuantity(0);
            product.setTrackInventory(false);

            // When & Then
            assertThat(product.isInStock()).isTrue();
        }

        @Test
        @DisplayName("Should return true when stock is at low stock threshold")
        void shouldReturnTrueWhenAtLowStockThreshold() {
            // Given
            product.setStockQuantity(20);
            product.setReservedQuantity(0);
            product.setLowStockThreshold(20);
            product.setTrackInventory(true);

            // When & Then
            assertThat(product.isLowStock()).isTrue();
        }

        @Test
        @DisplayName("Should return true when stock is below low stock threshold")
        void shouldReturnTrueWhenBelowLowStockThreshold() {
            // Given
            product.setStockQuantity(15);
            product.setReservedQuantity(0);
            product.setLowStockThreshold(20);
            product.setTrackInventory(true);

            // When & Then
            assertThat(product.isLowStock()).isTrue();
        }

        @Test
        @DisplayName("Should return false when stock is above low stock threshold")
        void shouldReturnFalseWhenAboveLowStockThreshold() {
            // Given
            product.setStockQuantity(50);
            product.setReservedQuantity(0);
            product.setLowStockThreshold(20);
            product.setTrackInventory(true);

            // When & Then
            assertThat(product.isLowStock()).isFalse();
        }

        @Test
        @DisplayName("Should return false for low stock when inventory tracking is disabled")
        void shouldReturnFalseForLowStockWhenInventoryTrackingDisabled() {
            // Given
            product.setStockQuantity(5);
            product.setLowStockThreshold(20);
            product.setTrackInventory(false);

            // When & Then
            assertThat(product.isLowStock()).isFalse();
        }
    }

    @Nested
    @DisplayName("Publication Status Tests")
    class PublicationStatusTests {

        @Test
        @DisplayName("Should return false when product is not published")
        void shouldReturnFalseWhenNotPublished() {
            // Given
            product.setPublishedAt(null);
            product.setStatus(ProductStatus.ACTIVE);

            // When & Then
            assertThat(product.isPublished()).isFalse();
        }

        @Test
        @DisplayName("Should return false when product status is inactive")
        void shouldReturnFalseWhenStatusInactive() {
            // Given
            product.setPublishedAt(OffsetDateTime.now());
            product.setStatus(ProductStatus.INACTIVE);

            // When & Then
            assertThat(product.isPublished()).isFalse();
        }

        @Test
        @DisplayName("Should return true when product is active and published")
        void shouldReturnTrueWhenActiveAndPublished() {
            // Given
            product.setPublishedAt(OffsetDateTime.now());
            product.setStatus(ProductStatus.ACTIVE);

            // When & Then
            assertThat(product.isPublished()).isTrue();
        }

        @Test
        @DisplayName("Should set published date when publishing product")
        void shouldSetPublishedDateWhenPublishing() {
            // Given
            product.setPublishedAt(null);
            OffsetDateTime beforePublish = OffsetDateTime.now().minusSeconds(1);

            // When
            product.publish();

            // Then
            assertThat(product.getPublishedAt()).isNotNull();
            assertThat(product.getPublishedAt()).isAfter(beforePublish);
            assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should clear published date when unpublishing product")
        void shouldClearPublishedDateWhenUnpublishing() {
            // Given
            product.setPublishedAt(OffsetDateTime.now());
            product.setStatus(ProductStatus.ACTIVE);

            // When
            product.unpublish();

            // Then
            assertThat(product.getPublishedAt()).isNull();
            assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should set status to discontinued when discontinuing product")
        void shouldSetStatusToDiscontinuedWhenDiscontinuing() {
            // Given
            product.setStatus(ProductStatus.ACTIVE);

            // When
            product.discontinue();

            // Then
            assertThat(product.getStatus()).isEqualTo(ProductStatus.DISCONTINUED);
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when products have same id")
        void shouldBeEqualWhenSameId() {
            // Given
            Product product1 = new Product();
            product1.setId("123");
            product1.setName("Product 1");

            Product product2 = new Product();
            product2.setId("123");
            product2.setName("Product 2");

            // When & Then
            assertThat(product1)
                    .isEqualTo(product2)
                    .hasSameHashCodeAs(product2);
        }

        @Test
        @DisplayName("Should not be equal when products have different ids")
        void shouldNotBeEqualWhenDifferentIds() {
            // Given
            Product product1 = new Product();
            product1.setId("123");

            Product product2 = new Product();
            product2.setId("456");

            // When & Then
            assertThat(product1).isNotEqualTo(product2);
        }

        @Test
        @DisplayName("Should not be equal when comparing with null")
        void shouldNotBeEqualWhenComparingWithNull() {
            // Given
            Product testProduct = new Product();
            testProduct.setId("123");

            // When & Then
            assertThat(testProduct).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal when comparing with different class")
        void shouldNotBeEqualWhenComparingWithDifferentClass() {
            // Given
            Product testProduct = new Product();
            testProduct.setId("123");

            // When & Then
            assertThat(testProduct).isNotEqualTo("not a product");
        }

        @Test
        @DisplayName("Should generate hash code")
        void shouldGenerateHashCode() {
            // Given
            product.setId("test-id");

            // When
            int hashCode = product.hashCode();

            // Then
            assertThat(hashCode).isNotZero();
        }

        @Test
        @DisplayName("Should generate string representation")
        void shouldGenerateStringRepresentation() {
            // Given
            product.setId("test-id");
            product.setName("Test Product");

            // When
            String toString = product.toString();

            // Then
            assertThat(toString)
                    .contains("Product")
                    .contains("test-id")
                    .contains("Test Product");
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build product with all fields using builder")
        void shouldBuildProductWithAllFields() {
            // Given
            String name = "Builder Product";
            String description = "Builder Description";
            String shortDescription = "Short desc";
            String sku = "BUILDER-001";
            BigDecimal basePrice = new BigDecimal("199.99");
            ProductStatus status = ProductStatus.ACTIVE;
            Integer stockQuantity = 50;
            Integer reservedQuantity = 5;
            Integer lowStockThreshold = 10;
            Boolean trackInventory = true;
            OffsetDateTime createdAt = OffsetDateTime.now();
            OffsetDateTime updatedAt = OffsetDateTime.now();
            OffsetDateTime publishedAt = OffsetDateTime.now();

            // When
            Product builtProduct = Product.builder()
                    .name(name)
                    .description(description)
                    .shortDescription(shortDescription)
                    .sku(sku)
                    .basePrice(basePrice)
                    .status(status)
                    .category(category)
                    .stockQuantity(stockQuantity)
                    .reservedQuantity(reservedQuantity)
                    .lowStockThreshold(lowStockThreshold)
                    .trackInventory(trackInventory)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .publishedAt(publishedAt)
                    .build();

            // Then
            assertThat(builtProduct.getName()).isEqualTo(name);
            assertThat(builtProduct.getDescription()).isEqualTo(description);
            assertThat(builtProduct.getShortDescription()).isEqualTo(shortDescription);
            assertThat(builtProduct.getSku()).isEqualTo(sku);
            assertThat(builtProduct.getBasePrice()).isEqualTo(basePrice);
            assertThat(builtProduct.getStatus()).isEqualTo(status);
            assertThat(builtProduct.getCategory()).isEqualTo(category);
            assertThat(builtProduct.getStockQuantity()).isEqualTo(stockQuantity);
            assertThat(builtProduct.getReservedQuantity()).isEqualTo(reservedQuantity);
            assertThat(builtProduct.getLowStockThreshold()).isEqualTo(lowStockThreshold);
            assertThat(builtProduct.getTrackInventory()).isEqualTo(trackInventory);
            assertThat(builtProduct.getCreatedAt()).isEqualTo(createdAt);
            assertThat(builtProduct.getUpdatedAt()).isEqualTo(updatedAt);
            assertThat(builtProduct.getPublishedAt()).isEqualTo(publishedAt);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get updatedAt field")
        void shouldGetUpdatedAt() {
            // Given
            OffsetDateTime updatedAt = OffsetDateTime.now();
            product.setUpdatedAt(updatedAt);

            // When & Then
            assertThat(product.getUpdatedAt()).isEqualTo(updatedAt);
        }
    }
}
