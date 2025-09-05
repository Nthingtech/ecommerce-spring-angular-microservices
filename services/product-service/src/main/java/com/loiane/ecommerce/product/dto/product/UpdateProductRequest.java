package com.loiane.ecommerce.product.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO for updating an existing product.
 */
public record UpdateProductRequest(
        @NotBlank(message = "Product name is required")
        String name,
        
        String description,
        
        @Positive(message = "Base price must be positive")
        BigDecimal basePrice,
        
        @Positive(message = "Low stock threshold must be positive")
        Integer lowStockThreshold
) {}
