package com.loiane.ecommerce.product.factory;

import com.loiane.ecommerce.product.dto.category.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Factory for creating Category DTOs for testing.
 * Controllers only work with DTOs, never entities.
 */
public class CategoryDTOTestFactory {
    
    private static int counter = 0;
    
    public static CategoryResponse createDefaultResponse() {
        String id = UUID.randomUUID().toString();
        return new CategoryResponse(
                id,
                "Test Category " + (++counter),
                "test-category-" + counter,
                "Test description",
                0,
                counter,
                true,
                null,
                new ArrayList<>(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
    
    public static CategoryResponse createRootResponse(String name) {
        return new CategoryResponse(
                UUID.randomUUID().toString(),
                name,
                slugify(name),
                name + " description",
                0,
                1,
                true,
                null,
                new ArrayList<>(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
    
    public static CategoryResponse createChildResponse(String name, CategorySummary parent) {
        return new CategoryResponse(
                UUID.randomUUID().toString(),
                name,
                slugify(name),
                name + " description",
                1,
                1,
                true,
                parent,
                new ArrayList<>(),
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
    
    public static CategorySummary createSummary(String name) {
        return new CategorySummary(
                UUID.randomUUID().toString(),
                name,
                slugify(name)
        );
    }
    
    public static CreateCategoryRequest createValidCreateRequest() {
        counter++;
        return new CreateCategoryRequest(
                "New Category " + counter,
                "new-category-" + counter,
                "Description for new category",
                null,
                counter
        );
    }
    
    public static CreateCategoryRequest createChildCreateRequest(String parentId) {
        counter++;
        return new CreateCategoryRequest(
                "Child Category " + counter,
                "child-category-" + counter,
                "Description for child category",
                parentId,
                counter
        );
    }
    
    public static UpdateCategoryRequest createValidUpdateRequest() {
        return new UpdateCategoryRequest(
                "Updated Category",
                "Updated description",
                10
        );
    }
    
    private static String slugify(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
    
    public static void resetCounter() {
        counter = 0;
    }
}
