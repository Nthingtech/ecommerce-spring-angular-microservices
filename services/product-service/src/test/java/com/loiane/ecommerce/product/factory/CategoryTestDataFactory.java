package com.loiane.ecommerce.product.factory;

import com.loiane.ecommerce.product.entity.Category;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating Category test data with support for hierarchies.
 * Provides common category scenarios and tree structures.
 */
public class CategoryTestDataFactory extends TestDataFactory {
    
    private CategoryTestDataFactory() {
        // Static factory class
    }
    
    /**
     * Creates a basic valid category with all required fields.
     */
    public static Category createDefault() {
        String name = "Test Category " + nextInt();
        return Category.builder()
                .name(name)
                .slug(slugify(name))
                .description("Description for " + name)
                .level(0)
                .displayOrder(nextInt())
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
    
    /**
     * Creates a root category (no parent).
     */
    public static Category createRoot(String name) {
        return Category.builder()
                .name(name)
                .slug(slugify(name))
                .description("Root category: " + name)
                .level(0)
                .displayOrder(nextInt())
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
    
    /**
     * Creates a child category with specified parent.
     */
    public static Category createChild(String name, Category parent) {
        Category child = Category.builder()
                .name(name)
                .slug(slugify(name))
                .description("Child category: " + name)
                .parent(parent)
                .level(parent.getLevel() + 1)
                .displayOrder(nextInt())
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        
        // Add to parent's children if needed
        if (parent.getChildren() == null) {
            parent.setChildren(new ArrayList<>());
        }
        parent.getChildren().add(child);
        
        return child;
    }
    
    /**
     * Creates an inactive category.
     */
    public static Category createInactive() {
        Category category = createDefault();
        category.setIsActive(false);
        return category;
    }
    
    /**
     * Creates a category with specific slug.
     */
    public static Category createWithSlug(String slug) {
        Category category = createDefault();
        category.setSlug(slug);
        return category;
    }
    
    /**
     * Creates a complete category hierarchy for testing.
     * Returns the root with all children properly linked.
     */
    public static CategoryHierarchy createHierarchy() {
        // Root categories
        Category electronics = createRoot("Electronics");
        Category clothing = createRoot("Clothing");
        
        // Electronics children
        Category computers = createChild("Computers", electronics);
        Category smartphones = createChild("Smartphones", electronics);
        Category accessories = createChild("Accessories", electronics);
        
        // Computers children
        Category laptops = createChild("Laptops", computers);
        Category desktops = createChild("Desktops", computers);
        
        // Clothing children
        Category mens = createChild("Men's Clothing", clothing);
        Category womens = createChild("Women's Clothing", clothing);
        
        return new CategoryHierarchy(
                List.of(electronics, clothing),
                List.of(electronics, clothing, computers, smartphones, 
                       accessories, laptops, desktops, mens, womens)
        );
    }
    
    /**
     * Fluent builder for creating customized categories.
     */
    public static CategoryBuilder aCategory() {
        return new CategoryBuilder();
    }
    
    /**
     * Common category scenarios as named factory methods.
     */
    public static class Categories {
        public static Category electronics() {
            return aCategory()
                    .withName("Electronics")
                    .withSlug("electronics")
                    .withDescription("Electronic products and gadgets")
                    .thatIsActive()
                    .build();
        }
        
        public static Category computers() {
            return aCategory()
                    .withName("Computers")
                    .withSlug("computers")
                    .withDescription("Desktop and laptop computers")
                    .withParent(electronics())
                    .thatIsActive()
                    .build();
        }
        
        public static Category inactiveCategory() {
            return aCategory()
                    .withName("Discontinued")
                    .withSlug("discontinued")
                    .thatIsInactive()
                    .build();
        }
    }
    
    /**
     * Helper class to return category hierarchy data.
     */
    public static class CategoryHierarchy {
        private final List<Category> roots;
        private final List<Category> allCategories;
        
        public CategoryHierarchy(List<Category> roots, List<Category> allCategories) {
            this.roots = roots;
            this.allCategories = allCategories;
        }
        
        public List<Category> getRoots() {
            return roots;
        }
        
        public List<Category> getAllCategories() {
            return allCategories;
        }
        
        public Category findByName(String name) {
            return allCategories.stream()
                    .filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + name));
        }
    }
    
    /**
     * Fluent builder for creating customized categories.
     */
    public static class CategoryBuilder {
        private final Category category;
        
        private CategoryBuilder() {
            this.category = createDefault();
        }
        
        public CategoryBuilder withId(String id) {
            category.setId(id);
            return this;
        }
        
        public CategoryBuilder withName(String name) {
            category.setName(name);
            return this;
        }
        
        public CategoryBuilder withSlug(String slug) {
            category.setSlug(slug);
            return this;
        }
        
        public CategoryBuilder withDescription(String description) {
            category.setDescription(description);
            return this;
        }
        
        public CategoryBuilder withParent(Category parent) {
            category.setParent(parent);
            category.setLevel(parent.getLevel() + 1);
            return this;
        }
        
        public CategoryBuilder withLevel(int level) {
            category.setLevel(level);
            return this;
        }
        
        public CategoryBuilder withDisplayOrder(int order) {
            category.setDisplayOrder(order);
            return this;
        }
        
        public CategoryBuilder thatIsActive() {
            category.setIsActive(true);
            return this;
        }
        
        public CategoryBuilder thatIsInactive() {
            category.setIsActive(false);
            return this;
        }
        
        public Category build() {
            return category;
        }
    }
    
    /**
     * Converts a name to a URL-friendly slug.
     */
    private static String slugify(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
