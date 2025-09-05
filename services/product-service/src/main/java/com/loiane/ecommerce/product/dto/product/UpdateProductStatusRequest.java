package com.loiane.ecommerce.product.dto.product;

import com.loiane.ecommerce.product.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating product status.
 */
public record UpdateProductStatusRequest(
        @NotNull(message = "Product status is required")
        ProductStatus status
) {}
