package com.loiane.ecommerce.product.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Category Entity Tests")
class CategoryTest {

    private Category category;
    private Category parentCategory;

    @BeforeEach
    void setUp() {
        parentCategory = Category.builder()
                .name("Electronics")
                .slug("electronics")
                .level(1)
                .displayOrder(1)
                .active(true)
                .build();
        parentCategory.setId("parent-id");

        category = Category.builder()
                .name("Laptops")
                .slug("laptops")
                .parent(parentCategory)
                .level(2)
                .displayOrder(1)
                .active(true)
                .build();
        category.setId("category-id");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create category with name and slug")
        void shouldCreateCategoryWithNameAndSlug() {
            // Given
            String name = "Test Category";
            String slug = "test-category";

            // When
            Category testCategory = new Category(name, slug);

            // Then
            assertThat(testCategory.getName()).isEqualTo(name);
            assertThat(testCategory.getSlug()).isEqualTo(slug);
            assertThat(testCategory.getLevel()).isZero(); // Root category level defaults to 0
            assertThat(testCategory.getDisplayOrder()).isZero();
            assertThat(testCategory.getIsActive()).isTrue();
            assertThat(testCategory.getChildren()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should create subcategory with parent")
        void shouldCreateSubcategoryWithParent() {
            // Given
            String name = "Test Subcategory";
            String slug = "test-subcategory";
            Category parent = new Category("Parent", "parent");

            // When
            Category testCategory = new Category(name, slug, parent);

            // Then
            assertThat(testCategory.getName()).isEqualTo(name);
            assertThat(testCategory.getSlug()).isEqualTo(slug);
            assertThat(testCategory.getParent()).isEqualTo(parent);
            assertThat(testCategory.getLevel()).isEqualTo(1); // Parent level 0 + 1 = 1
            assertThat(testCategory.getDisplayOrder()).isZero();
            assertThat(testCategory.getIsActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Category Hierarchy Tests")
    class CategoryHierarchyTests {

        @Test
        @DisplayName("Should return true for root category")
        void shouldReturnTrueForRootCategory() {
            // Given
            Category rootCategory = new Category("Root", "root");

            // When & Then
            assertThat(rootCategory.isRootCategory()).isTrue();
        }

        @Test
        @DisplayName("Should return false for subcategory")
        void shouldReturnFalseForSubcategory() {
            // When & Then
            assertThat(category.isRootCategory()).isFalse();
        }

        @Test
        @DisplayName("Should return true when category has children")
        void shouldReturnTrueWhenHasChildren() {
            // Given
            Category childCategory = new Category("Child", "child");
            parentCategory.addChild(childCategory);

            // When & Then
            assertThat(parentCategory.hasChildren()).isTrue();
        }

        @Test
        @DisplayName("Should return false when category has no children")
        void shouldReturnFalseWhenHasNoChildren() {
            // When & Then
            assertThat(category.hasChildren()).isFalse();
        }

        @Test
        @DisplayName("Should return true when category has products")
        void shouldReturnTrueWhenHasProducts() {
            // Given
            category.setProducts(new ArrayList<>());
            Product product = new Product();
            category.getProducts().add(product);

            // When & Then
            assertThat(category.hasProducts()).isTrue();
        }

        @Test
        @DisplayName("Should return false when category has no products")
        void shouldReturnFalseWhenHasNoProducts() {
            // Given
            category.setProducts(new ArrayList<>());

            // When & Then
            assertThat(category.hasProducts()).isFalse();
        }

        @Test
        @DisplayName("Should return false when products list is null")
        void shouldReturnFalseWhenProductsListIsNull() {
            // Given
            category.setProducts(null);

            // When & Then
            assertThat(category.hasProducts()).isFalse();
        }

        @Test
        @DisplayName("Should generate full path for root category")
        void shouldGenerateFullPathForRootCategory() {
            // Given
            Category rootCategory = new Category("Electronics", "electronics");

            // When
            String fullPath = rootCategory.getFullPath();

            // Then
            assertThat(fullPath).isEqualTo("Electronics");
        }

        @Test
        @DisplayName("Should generate full path for subcategory")
        void shouldGenerateFullPathForSubcategory() {
            // When
            String fullPath = category.getFullPath();

            // Then
            assertThat(fullPath).isEqualTo("Electronics > Laptops");
        }

        @Test
        @DisplayName("Should add child to category")
        void shouldAddChildToCategory() {
            // Given
            Category childCategory = new Category("Child", "child");

            // When
            parentCategory.addChild(childCategory);

            // Then
            assertThat(parentCategory.getChildren()).contains(childCategory);
            assertThat(childCategory.getParent()).isEqualTo(parentCategory);
        }

        @Test
        @DisplayName("Should remove child from category")
        void shouldRemoveChildFromCategory() {
            // Given
            Category childCategory = new Category("Child", "child");
            parentCategory.addChild(childCategory);

            // When
            parentCategory.removeChild(childCategory);

            // Then
            assertThat(parentCategory.getChildren()).doesNotContain(childCategory);
            assertThat(childCategory.getParent()).isNull();
        }
    }

    @Nested
    @DisplayName("Category Status Tests")
    class CategoryStatusTests {

        @Test
        @DisplayName("Should activate category")
        void shouldActivateCategory() {
            // Given
            category.setIsActive(false);

            // When
            category.activate();

            // Then
            assertThat(category.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("Should deactivate category")
        void shouldDeactivateCategory() {
            // Given
            category.setIsActive(true);

            // When
            category.deactivate();

            // Then
            assertThat(category.getIsActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when categories have same id")
        void shouldBeEqualWhenSameId() {
            // Given
            Category category1 = new Category();
            category1.setId("123");
            category1.setName("Category 1");

            Category category2 = new Category();
            category2.setId("123");
            category2.setName("Category 2");

            // When & Then
            assertThat(category1)
                    .isEqualTo(category2)
                    .hasSameHashCodeAs(category2);
        }

        @Test
        @DisplayName("Should not be equal when categories have different ids")
        void shouldNotBeEqualWhenDifferentIds() {
            // Given
            Category category1 = new Category();
            category1.setId("123");

            Category category2 = new Category();
            category2.setId("456");

            // When & Then
            assertThat(category1).isNotEqualTo(category2);
        }

        @Test
        @DisplayName("Should generate hash code")
        void shouldGenerateHashCode() {
            // Given
            category.setId("test-id");

            // When
            int hashCode = category.hashCode();

            // Then
            assertThat(hashCode).isNotZero();
        }

        @Test
        @DisplayName("Should generate string representation")
        void shouldGenerateStringRepresentation() {
            // Given
            category.setId("test-id");
            category.setName("Test Category");

            // When
            String toString = category.toString();

            // Then
            assertThat(toString)
                    .contains("Category")
                    .contains("test-id")
                    .contains("Test Category");
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build category with all fields using builder")
        void shouldBuildCategoryWithAllFields() {
            // Given
            String name = "Builder Category";
            String slug = "builder-category";
            String description = "Builder Description";
            Integer level = 3;
            Integer displayOrder = 5;
            Boolean active = true;
            OffsetDateTime createdAt = OffsetDateTime.now();
            OffsetDateTime updatedAt = OffsetDateTime.now();

            // When
            Category builtCategory = Category.builder()
                    .name(name)
                    .slug(slug)
                    .description(description)
                    .parent(parentCategory)
                    .level(level)
                    .displayOrder(displayOrder)
                    .active(active)
                    .createdAt(createdAt)
                    .updatedAt(updatedAt)
                    .build();

            // Then
            assertThat(builtCategory.getName()).isEqualTo(name);
            assertThat(builtCategory.getSlug()).isEqualTo(slug);
            assertThat(builtCategory.getDescription()).isEqualTo(description);
            assertThat(builtCategory.getParent()).isEqualTo(parentCategory);
            assertThat(builtCategory.getLevel()).isEqualTo(level);
            assertThat(builtCategory.getDisplayOrder()).isEqualTo(displayOrder);
            assertThat(builtCategory.getIsActive()).isEqualTo(active);
            assertThat(builtCategory.getCreatedAt()).isEqualTo(createdAt);
            assertThat(builtCategory.getUpdatedAt()).isEqualTo(updatedAt);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get products field")
        void shouldGetProducts() {
            // Given
            category.setProducts(new ArrayList<>());

            // When & Then
            assertThat(category.getProducts()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should get createdAt field")
        void shouldGetCreatedAt() {
            // Given
            OffsetDateTime createdAt = OffsetDateTime.now();
            category.setCreatedAt(createdAt);

            // When & Then
            assertThat(category.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("Should get updatedAt field")
        void shouldGetUpdatedAt() {
            // Given
            OffsetDateTime updatedAt = OffsetDateTime.now();
            category.setUpdatedAt(updatedAt);

            // When & Then
            assertThat(category.getUpdatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("Should set children field")
        void shouldSetChildren() {
            // Given
            var children = new ArrayList<Category>();

            // When
            category.setChildren(children);

            // Then
            assertThat(category.getChildren()).isEqualTo(children);
        }

        @Test
        @DisplayName("Should set products field")
        void shouldSetProducts() {
            // Given
            var products = new ArrayList<Product>();

            // When
            category.setProducts(products);

            // Then
            assertThat(category.getProducts()).isEqualTo(products);
        }
    }
}
