package com.loiane.ecommerce.product.repository;

import com.loiane.ecommerce.product.entity.Category;
import com.loiane.ecommerce.product.entity.Product;
import com.loiane.ecommerce.product.entity.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Product Repository Tests")
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Category electronicsCategory;
    private Category clothingCategory;
    private Product laptop;
    private Product smartphone;
    private Product tshirt;

    @BeforeEach
    void setUp() {
        // Create test categories
        electronicsCategory = Category.builder()
                .name("Electronics")
                .slug("electronics")
                .description("Electronic products")
                .level(0)
                .displayOrder(1)
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persistAndFlush(electronicsCategory);

        clothingCategory = Category.builder()
                .name("Clothing")
                .slug("clothing")
                .description("Clothing items")
                .level(0)
                .displayOrder(2)
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persistAndFlush(clothingCategory);

        // Create test products
        laptop = Product.builder()
                .name("Gaming Laptop")
                .description("High-performance gaming laptop")
                .shortDescription("Gaming laptop with RTX graphics")
                .sku("LAPTOP-001")
                .basePrice(new BigDecimal("1299.99"))
                .status(ProductStatus.ACTIVE)
                .category(electronicsCategory)
                .stockQuantity(10)
                .reservedQuantity(2)
                .lowStockThreshold(10) // Changed from 5 to 10, so available (8) < threshold (10)
                .trackInventory(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .publishedAt(OffsetDateTime.now())
                .build();
        entityManager.persistAndFlush(laptop);

        smartphone = Product.builder()
                .name("Smartphone Pro")
                .description("Latest smartphone with advanced features")
                .shortDescription("Pro smartphone with premium camera")
                .sku("PHONE-001")
                .basePrice(new BigDecimal("899.99"))
                .status(ProductStatus.ACTIVE)
                .category(electronicsCategory)
                .stockQuantity(25)
                .reservedQuantity(0)
                .lowStockThreshold(10)
                .trackInventory(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .publishedAt(OffsetDateTime.now())
                .build();
        entityManager.persistAndFlush(smartphone);

        tshirt = Product.builder()
                .name("Cotton T-Shirt")
                .description("Comfortable cotton t-shirt")
                .shortDescription("100% cotton casual t-shirt")
                .sku("TSHIRT-001")
                .basePrice(new BigDecimal("29.99"))
                .status(ProductStatus.INACTIVE)
                .category(clothingCategory)
                .stockQuantity(0)
                .reservedQuantity(0)
                .lowStockThreshold(20)
                .trackInventory(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persistAndFlush(tshirt);

        entityManager.clear();
    }

    @Test
    @DisplayName("Should save and find product by ID")
    void shouldSaveAndFindProductById() {
        // Given
        Product newProduct = Product.builder()
                .name("Test Product")
                .description("Test description")
                .sku("TEST-001")
                .basePrice(new BigDecimal("99.99"))
                .status(ProductStatus.ACTIVE)
                .stockQuantity(5)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        // When
        Product savedProduct = productRepository.save(newProduct);
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // Then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
        assertThat(foundProduct.get().getSku()).isEqualTo("TEST-001");
        assertThat(foundProduct.get().getBasePrice()).isEqualTo(new BigDecimal("99.99"));
    }

    @Test
    @DisplayName("Should find product by SKU")
    void shouldFindProductBySku() {
        // When
        Optional<Product> foundProduct = productRepository.findBySku("LAPTOP-001");

        // Then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Gaming Laptop");
        assertThat(foundProduct.get().getCategory().getName()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("Should return empty when finding non-existent SKU")
    void shouldReturnEmptyForNonExistentSku() {
        // When
        Optional<Product> foundProduct = productRepository.findBySku("NON-EXISTENT");

        // Then
        assertThat(foundProduct).isEmpty();
    }

    @Test
    @DisplayName("Should find products by status")
    void shouldFindProductsByStatus() {
        // When
        List<Product> activeProducts = productRepository.findByStatus(ProductStatus.ACTIVE);
        List<Product> inactiveProducts = productRepository.findByStatus(ProductStatus.INACTIVE);

        // Then
        assertThat(activeProducts).hasSize(2);
        assertThat(activeProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");

        assertThat(inactiveProducts).hasSize(1);
        assertThat(inactiveProducts.get(0).getName()).isEqualTo("Cotton T-Shirt");
    }

    @Test
    @DisplayName("Should find products by category")
    void shouldFindProductsByCategory() {
        // When
        List<Product> electronicsProducts = productRepository.findByCategory(electronicsCategory);
        List<Product> clothingProducts = productRepository.findByCategory(clothingCategory);

        // Then
        assertThat(electronicsProducts).hasSize(2);
        assertThat(electronicsProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");

        assertThat(clothingProducts).hasSize(1);
        assertThat(clothingProducts.get(0).getName()).isEqualTo("Cotton T-Shirt");
    }

    @Test
    @DisplayName("Should find products with low stock")
    void shouldFindProductsWithLowStock() {
        // When
        List<Product> lowStockProducts = productRepository.findProductsWithLowStock();

        // Then
        assertThat(lowStockProducts).hasSize(2);
        // laptop: available=8 (10-2), threshold=5 -> low stock
        // tshirt: available=0, threshold=20 -> low stock
        assertThat(lowStockProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Cotton T-Shirt");
    }

    @Test
    @DisplayName("Should find products by name containing search term")
    void shouldFindProductsByNameContaining() {
        // When
        List<Product> laptopResults = productRepository.findByNameContainingIgnoreCase("laptop");
        List<Product> phoneResults = productRepository.findByNameContainingIgnoreCase("PHONE");
        List<Product> emptyResults = productRepository.findByNameContainingIgnoreCase("nonexistent");

        // Then
        assertThat(laptopResults).hasSize(1);
        assertThat(laptopResults.get(0).getName()).isEqualTo("Gaming Laptop");

        assertThat(phoneResults).hasSize(1);
        assertThat(phoneResults.get(0).getName()).isEqualTo("Smartphone Pro");

        assertThat(emptyResults).isEmpty();
    }

    @Test
    @DisplayName("Should find products by category and status with pagination")
    void shouldFindProductsByCategoryAndStatusWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Product> activeElectronics = productRepository.findByCategoryAndStatus(
                electronicsCategory, ProductStatus.ACTIVE, pageable);

        // Then
        assertThat(activeElectronics.getContent()).hasSize(2);
        assertThat(activeElectronics.getTotalElements()).isEqualTo(2);
        assertThat(activeElectronics.isFirst()).isTrue();
        assertThat(activeElectronics.hasNext()).isFalse();
    }

    @Test
    @DisplayName("Should find products in stock")
    void shouldFindProductsInStock() {
        // When
        List<Product> inStockProducts = productRepository.findProductsInStock();

        // Then
        assertThat(inStockProducts).hasSize(2);
        assertThat(inStockProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");
    }

    @Test
    @DisplayName("Should count products by category")
    void shouldCountProductsByCategory() {
        // When
        long electronicsCount = productRepository.countByCategory(electronicsCategory);
        long clothingCount = productRepository.countByCategory(clothingCategory);

        // Then
        assertThat(electronicsCount).isEqualTo(2);
        assertThat(clothingCount).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find products by price range")
    void shouldFindProductsByPriceRange() {
        // When
        List<Product> midRangeProducts = productRepository.findByBasePriceBetween(
                new BigDecimal("50.00"), new BigDecimal("1000.00"));

        // Then
        assertThat(midRangeProducts).hasSize(1);
        assertThat(midRangeProducts.get(0).getName()).isEqualTo("Smartphone Pro");
    }

    @Test
    @DisplayName("Should check if SKU exists")
    void shouldCheckIfSkuExists() {
        // When
        boolean existingSku = productRepository.existsBySku("LAPTOP-001");
        boolean nonExistentSku = productRepository.existsBySku("NON-EXISTENT");

        // Then
        assertThat(existingSku).isTrue();
        assertThat(nonExistentSku).isFalse();
    }

    @Test
    @DisplayName("Should find all active products")
    void shouldFindActiveProducts() {
        // When
        List<Product> activeProducts = productRepository.findActiveProducts();

        // Then
        assertThat(activeProducts).hasSize(2); // laptop and smartphone are active
        assertThat(activeProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");
        assertThat(activeProducts).allMatch(p -> p.getStatus() == ProductStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should find active products by category")
    void shouldFindActiveProductsByCategory() {
        // When
        List<Product> activeElectronics = productRepository.findActiveProductsByCategory(electronicsCategory);
        List<Product> activeClothing = productRepository.findActiveProductsByCategory(clothingCategory);

        // Then
        assertThat(activeElectronics).hasSize(2);
        assertThat(activeElectronics).extracting(Product::getName)
                .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");

        assertThat(activeClothing).isEmpty(); // tshirt is INACTIVE
    }

    @Test
    @DisplayName("Should find active products by name containing")
    void shouldFindActiveProductsByNameContaining() {
        // When
        List<Product> laptopProducts = productRepository.findActiveProductsByNameContaining("laptop");
        List<Product> shirtProducts = productRepository.findActiveProductsByNameContaining("shirt");

        // Then
        assertThat(laptopProducts).hasSize(1);
        assertThat(laptopProducts.get(0).getName()).isEqualTo("Gaming Laptop");

        assertThat(shirtProducts).isEmpty(); // tshirt is INACTIVE, won't be found
    }

    @Test
    @DisplayName("Should find active products by price range")
    void shouldFindActiveProductsByPriceRange() {
        // When - Looking for products between $800-$1500 (laptop range)
        List<Product> expensiveProducts = productRepository.findActiveProductsByPriceRange(
                new BigDecimal("800.00"), new BigDecimal("1500.00"));

        // Then
        assertThat(expensiveProducts).hasSize(1);
        assertThat(expensiveProducts.get(0).getName()).isEqualTo("Gaming Laptop");
        assertThat(expensiveProducts.get(0).getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }
}
