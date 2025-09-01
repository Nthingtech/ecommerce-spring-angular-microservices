package com.loiane.ecommerce.product.dto.category;

import jakarta.validation.constraints.*;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        String name,
        
        @NotBlank(message = "Slug is required")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
        @Size(min = 2, max = 100, message = "Slug must be between 2 and 100 characters")
        String slug,
        
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,
        
        String parentId,
        
        @Min(value = 0, message = "Display order cannot be negative")
        Integer displayOrder
) {
    // Provide default values through a static factory method if needed
    public static CreateCategoryRequest withDefaults(String name, String slug, String description, String parentId) {
        return new CreateCategoryRequest(name, slug, description, parentId, 0);
    }
}
