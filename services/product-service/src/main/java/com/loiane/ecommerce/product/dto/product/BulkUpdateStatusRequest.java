package com.loiane.ecommerce.product.dto.product;

import com.loiane.ecommerce.product.entity.ProductStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request DTO for bulk updating product status.
 */
public record BulkUpdateStatusRequest(
        @NotEmpty(message = "Product IDs list cannot be empty")
        List<String> productIds,
        
        @NotNull(message = "Product status is required")
        ProductStatus status
) {}
