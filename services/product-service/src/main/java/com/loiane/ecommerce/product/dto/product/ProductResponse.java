package com.loiane.ecommerce.product.dto.product;

import com.loiane.ecommerce.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Response DTO for Product entity.
 */
public record ProductResponse(
        String id,
        String name,
        String sku,
        String description,
        BigDecimal basePrice,
        ProductStatus status,
        CategorySummary category,
        Integer stockQuantity,
        Integer reservedQuantity,
        Integer lowStockThreshold,
        Boolean trackInventory,
        OffsetDateTime publishedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
