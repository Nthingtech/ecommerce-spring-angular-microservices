package com.loiane.ecommerce.product.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for creating a new product.
 */
public record CreateProductRequest(
        @NotBlank(message = "Product name is required")
        String name,
        
        @NotBlank(message = "SKU is required")
        String sku,
        
        String description,
        
        @NotNull(message = "Base price is required")
        @Positive(message = "Base price must be positive")
        BigDecimal basePrice,
        
        @NotBlank(message = "Category ID is required")
        String categoryId,
        
        @Positive(message = "Stock quantity must be positive")
        Integer stockQuantity,
        
        @Positive(message = "Low stock threshold must be positive")
        Integer lowStockThreshold,
        
        @NotNull(message = "Track inventory flag is required")
        Boolean trackInventory
) {}
