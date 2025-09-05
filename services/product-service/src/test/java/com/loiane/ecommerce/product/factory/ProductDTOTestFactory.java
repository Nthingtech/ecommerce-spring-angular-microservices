package com.loiane.ecommerce.product.factory;

import com.loiane.ecommerce.product.dto.product.*;
import com.loiane.ecommerce.product.entity.Product;
import com.loiane.ecommerce.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Factory for creating Product DTOs for testing.
 * Controllers only work with DTOs, never entities.
 */
public class ProductDTOTestFactory {
    
    private static int counter = 0;
    
    public static ProductResponse createProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getDescription(),
                product.getBasePrice(),
                product.getStatus(),
                product.getCategory() != null ? 
                    new CategorySummary(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getSlug()
                    ) : null,
                product.getStockQuantity(),
                product.getReservedQuantity(),
                product.getLowStockThreshold(),
                product.isTrackInventory(),
                product.getPublishedAt(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
    
    public static ProductResponse createDefaultResponse() {
        String id = UUID.randomUUID().toString();
        return new ProductResponse(
                id,
                "Test Product " + (++counter),
                "TEST-SKU-" + counter,
                "Test description",
                new BigDecimal("99.99"),
                ProductStatus.ACTIVE,
                new CategorySummary(
                    UUID.randomUUID().toString(),
                    "Test Category",
                    "test-category"
                ),
                100,
                10,
                20,
                true,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
    
    public static CreateProductRequest createValidCreateRequest(String categoryId) {
        counter++;
        return new CreateProductRequest(
                "New Product " + counter,
                "NEW-SKU-" + counter,
                "Description for new product",
                new BigDecimal("149.99"),
                categoryId,
                50,
                25,
                true
        );
    }
    
    public static CreateProductRequest createInvalidCreateRequest() {
        return new CreateProductRequest(
                "", // Invalid: empty name
                "", // Invalid: empty SKU
                "Description",
                new BigDecimal("-10.00"), // Invalid: negative price
                null, // Invalid: null category
                -5, // Invalid: negative stock
                10,
                true
        );
    }
    
    public static UpdateProductRequest createValidUpdateRequest() {
        return new UpdateProductRequest(
                "Updated Product Name",
                "Updated description",
                new BigDecimal("199.99"),
                15
        );
    }
    
    public static UpdateProductStatusRequest createStatusUpdateRequest(ProductStatus status) {
        return new UpdateProductStatusRequest(status);
    }
    
    public static UpdateProductInventoryRequest createInventoryUpdateRequest(int stock, int threshold) {
        return new UpdateProductInventoryRequest(stock, threshold);
    }
    
    public static void resetCounter() {
        counter = 0;
    }
}
