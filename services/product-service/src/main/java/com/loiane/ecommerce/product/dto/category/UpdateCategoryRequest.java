package com.loiane.ecommerce.product.dto.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        String name,
        
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        
        @Min(value = 0, message = "Display order cannot be negative")
        Integer displayOrder
) {}
