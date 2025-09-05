package com.loiane.ecommerce.product.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO for updating product inventory.
 */
public record UpdateProductInventoryRequest(
        @NotNull(message = "Stock quantity is required")
        @Positive(message = "Stock quantity must be positive")
        Integer stockQuantity,
        
        @NotNull(message = "Low stock threshold is required")
        @Positive(message = "Low stock threshold must be positive")
        Integer lowStockThreshold
) {}
