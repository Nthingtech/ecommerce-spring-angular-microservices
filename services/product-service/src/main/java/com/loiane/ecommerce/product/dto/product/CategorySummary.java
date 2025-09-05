package com.loiane.ecommerce.product.dto.product;

/**
 * Summary DTO for Category references in product responses.
 */
public record CategorySummary(
        String id,
        String name,
        String slug
) {}
