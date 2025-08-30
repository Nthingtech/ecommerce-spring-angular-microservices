package com.loiane.ecommerce.product.repository;

import com.loiane.ecommerce.product.entity.Category;
import com.loiane.ecommerce.product.factory.CategoryTestDataFactory;
import com.loiane.ecommerce.product.factory.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Category Repository Tests")
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category rootCategory;
    private Category electronicsCategory;
    private Category computersCategory;
    private Category laptopsCategory;
    private Category clothingCategory;
    private Category menClothingCategory;

    @BeforeEach
    void setUp() {
        TestDataFactory.resetCounter();

        // Create hierarchical category structure using factories
        rootCategory = CategoryTestDataFactory.createRoot("Root");
        entityManager.persistAndFlush(rootCategory);

        electronicsCategory = CategoryTestDataFactory.createChild("Electronics", rootCategory);
        entityManager.persistAndFlush(electronicsCategory);

        computersCategory = CategoryTestDataFactory.createChild("Computers", electronicsCategory);
        entityManager.persistAndFlush(computersCategory);

        laptopsCategory = CategoryTestDataFactory.createChild("Laptops", computersCategory);
        entityManager.persistAndFlush(laptopsCategory);

        clothingCategory = CategoryTestDataFactory.aCategory()
                .withName("Clothing")
                .withSlug("clothing")
                .withDescription("Clothing items")
                .withParent(rootCategory)
                .withLevel(1)
                .withDisplayOrder(2)
                .thatIsActive()
                .build();
        entityManager.persistAndFlush(clothingCategory);

        menClothingCategory = CategoryTestDataFactory.aCategory()
                .withName("Men's Clothing")
                .withSlug("mens-clothing")
                .withDescription("Men's clothing items")
                .withParent(clothingCategory)
                .withLevel(2)
                .withDisplayOrder(1)
                .thatIsInactive()
                .build();
        entityManager.persistAndFlush(menClothingCategory);

        entityManager.clear();
    }

    @Test
    @DisplayName("Should save and find category by ID")
    void shouldSaveAndFindCategoryById() {
        // Given
        Category newCategory = CategoryTestDataFactory.aCategory()
                .withName("Test Category")
                .withSlug("test-category")
                .withDescription("Test description")
                .build();

        // When
        Category savedCategory = categoryRepository.save(newCategory);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        // Then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Test Category");
        assertThat(foundCategory.get().getSlug()).isEqualTo("test-category");
    }

    @Test
    @DisplayName("Should find category by slug")
    void shouldFindCategoryBySlug() {
        // When
        Optional<Category> foundCategory = categoryRepository.findBySlug("electronics");

        // Then
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("Electronics");
        assertThat(foundCategory.get().getParent().getName()).isEqualTo("Root");
    }

    @Test
    @DisplayName("Should return empty when finding non-existent slug")
    void shouldReturnEmptyForNonExistentSlug() {
        // When
        Optional<Category> foundCategory = categoryRepository.findBySlug("non-existent");

        // Then
        assertThat(foundCategory).isEmpty();
    }

    @Test
    @DisplayName("Should find root categories")
    void shouldFindRootCategories() {
        // When
        List<Category> rootCategories = categoryRepository.findByParentIsNull();

        // Then
        assertThat(rootCategories).hasSize(1);
        assertThat(rootCategories.get(0).getName()).isEqualTo("Root");
        assertThat(rootCategories.get(0).getLevel()).isZero();
    }

    @Test
    @DisplayName("Should find categories by parent")
    void shouldFindCategoriesByParent() {
        // When
        List<Category> rootChildren = categoryRepository.findByParent(rootCategory);
        List<Category> electronicsChildren = categoryRepository.findByParent(electronicsCategory);

        // Then
        assertThat(rootChildren).hasSize(2);
        assertThat(rootChildren).extracting(Category::getName)
                .containsExactlyInAnyOrder("Electronics", "Clothing");

        assertThat(electronicsChildren).hasSize(1);
        assertThat(electronicsChildren.get(0).getName()).isEqualTo("Computers");
    }

    @Test
    @DisplayName("Should find categories by level")
    void shouldFindCategoriesByLevel() {
        // When
        List<Category> level0Categories = categoryRepository.findByLevel(0);
        List<Category> level1Categories = categoryRepository.findByLevel(1);
        List<Category> level2Categories = categoryRepository.findByLevel(2);

        // Then
        assertThat(level0Categories).hasSize(1);
        assertThat(level0Categories.get(0).getName()).isEqualTo("Root");

        assertThat(level1Categories).hasSize(2);
        assertThat(level1Categories).extracting(Category::getName)
                .containsExactlyInAnyOrder("Electronics", "Clothing");

        assertThat(level2Categories).hasSize(2);
        assertThat(level2Categories).extracting(Category::getName)
                .containsExactlyInAnyOrder("Computers", "Men's Clothing");
    }

    @Test
    @DisplayName("Should find active categories")
    void shouldFindActiveCategories() {
        // When
        List<Category> activeCategories = categoryRepository.findByIsActiveTrue();
        List<Category> inactiveCategories = categoryRepository.findByIsActiveFalse();

        // Then
        assertThat(activeCategories).hasSize(5);
        assertThat(activeCategories).extracting(Category::getName)
                .containsExactlyInAnyOrder("Root", "Electronics", "Computers", "Laptops", "Clothing");

        assertThat(inactiveCategories).hasSize(1);
        assertThat(inactiveCategories.get(0).getName()).isEqualTo("Men's Clothing");
    }

    @Test
    @DisplayName("Should find categories by parent and active status ordered by display order")
    void shouldFindCategoriesByParentAndActiveOrderByDisplayOrder() {
        // When
        List<Category> activeRootChildren = categoryRepository
                .findByParentAndIsActiveTrueOrderByDisplayOrder(rootCategory);

        // Then
        assertThat(activeRootChildren).hasSize(2);
        assertThat(activeRootChildren.get(0).getName()).isEqualTo("Electronics"); // displayOrder = 1
        assertThat(activeRootChildren.get(1).getName()).isEqualTo("Clothing");    // displayOrder = 2
    }

    @Test
    @DisplayName("Should find categories by name containing search term")
    void shouldFindCategoriesByNameContaining() {
        // When
        List<Category> clothingResults = categoryRepository.findByNameContainingIgnoreCase("clothing");
        List<Category> computerResults = categoryRepository.findByNameContainingIgnoreCase("COMPUTER");
        List<Category> emptyResults = categoryRepository.findByNameContainingIgnoreCase("nonexistent");

        // Then
        assertThat(clothingResults).hasSize(2);
        assertThat(clothingResults).extracting(Category::getName)
                .containsExactlyInAnyOrder("Clothing", "Men's Clothing");

        assertThat(computerResults).hasSize(1);
        assertThat(computerResults.get(0).getName()).isEqualTo("Computers");

        assertThat(emptyResults).isEmpty();
    }

    @Test
    @DisplayName("Should check if slug exists")
    void shouldCheckIfSlugExists() {
        // When
        boolean existingSlug = categoryRepository.existsBySlug("electronics");
        boolean nonExistentSlug = categoryRepository.existsBySlug("non-existent");

        // Then
        assertThat(existingSlug).isTrue();
        assertThat(nonExistentSlug).isFalse();
    }

    @Test
    @DisplayName("Should count categories by parent")
    void shouldCountCategoriesByParent() {
        // When
        long rootChildrenCount = categoryRepository.countByParent(rootCategory);
        long electronicsChildrenCount = categoryRepository.countByParent(electronicsCategory);
        long laptopsChildrenCount = categoryRepository.countByParent(laptopsCategory);

        // Then
        assertThat(rootChildrenCount).isEqualTo(2);
        assertThat(electronicsChildrenCount).isEqualTo(1);
        assertThat(laptopsChildrenCount).isZero(); // Leaf category
    }

    @Test
    @DisplayName("Should find categories with children")
    void shouldFindCategoriesWithChildren() {
        // When - This requires a custom query that we'll implement
        List<Category> categoriesWithChildren = categoryRepository.findCategoriesWithChildren();

        // Then
        assertThat(categoriesWithChildren).hasSize(4);
        assertThat(categoriesWithChildren).extracting(Category::getName)
                .containsExactlyInAnyOrder("Root", "Electronics", "Computers", "Clothing");
    }

    @Test
    @DisplayName("Should find leaf categories (categories without children)")
    void shouldFindLeafCategories() {
        // When
        List<Category> leafCategories = categoryRepository.findLeafCategories();

        // Then
        assertThat(leafCategories).hasSize(2);
        assertThat(leafCategories).extracting(Category::getName)
                .containsExactlyInAnyOrder("Laptops", "Men's Clothing");
    }
}
