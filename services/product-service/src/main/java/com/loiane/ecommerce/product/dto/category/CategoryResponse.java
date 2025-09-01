package com.loiane.ecommerce.product.dto.category;

import java.time.OffsetDateTime;
import java.util.List;

public record CategoryResponse(
        String id,
        String name,
        String slug,
        String description,
        Integer level,
        Integer displayOrder,
        boolean active,
        CategorySummary parent,
        List<CategoryResponse> children,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
