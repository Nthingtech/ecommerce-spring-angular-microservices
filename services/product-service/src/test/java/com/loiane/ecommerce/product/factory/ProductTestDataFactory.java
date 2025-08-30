package com.loiane.ecommerce.product.factory;

import com.loiane.ecommerce.product.entity.Category;
import com.loiane.ecommerce.product.entity.Product;
import com.loiane.ecommerce.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating Product test data using fluent builder pattern.
 * Provides common product scenarios for testing.
 */
public class ProductTestDataFactory extends TestDataFactory {
    
    private ProductTestDataFactory() {
        // Static factory class
    }
    
    /**
     * Creates a basic valid product with all required fields.
     * This is the most common starting point for tests.
     */
    public static Product createDefault() {
        return Product.builder()
                .name("Test Product " + nextInt())
                .description("Description for test product")
                .shortDescription("Short description")
                .sku(nextString("SKU"))
                .basePrice(new BigDecimal("99.99"))
                .stockQuantity(100)
                .reservedQuantity(0)
                .trackInventory(true)
                .lowStockThreshold(10)
                .status(ProductStatus.ACTIVE)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
    
    /**
     * Creates a product with specific status.
     */
    public static Product createWithStatus(ProductStatus status) {
        Product product = createDefault();
        product.setStatus(status);
        if (status == ProductStatus.ACTIVE) {
            product.setPublishedAt(OffsetDateTime.now());
        }
        return product;
    }
    
    /**
     * Creates an active, published product.
     */
    public static Product createActive() {
        return createWithStatus(ProductStatus.ACTIVE);
    }
    
    /**
     * Creates an inactive product.
     */
    public static Product createInactive() {
        return createWithStatus(ProductStatus.INACTIVE);
    }
    
    /**
     * Creates a discontinued product.
     */
    public static Product createDiscontinued() {
        return createWithStatus(ProductStatus.DISCONTINUED);
    }
    
    /**
     * Creates a product with specific stock levels.
     */
    public static Product createWithStock(int stockQuantity, int reservedQuantity) {
        Product product = createDefault();
        product.setStockQuantity(stockQuantity);
        product.setReservedQuantity(reservedQuantity);
        return product;
    }
    
    /**
     * Creates a product with low stock (below threshold).
     */
    public static Product createWithLowStock() {
        Product product = createDefault();
        product.setStockQuantity(5);
        product.setLowStockThreshold(10);
        return product;
    }
    
    /**
     * Creates an out-of-stock product.
     */
    public static Product createOutOfStock() {
        return createWithStock(0, 0);
    }
    
    /**
     * Creates a product with specific price.
     */
    public static Product createWithPrice(String price) {
        Product product = createDefault();
        product.setBasePrice(new BigDecimal(price));
        return product;
    }
    
    /**
     * Creates a product with specific SKU.
     */
    public static Product createWithSku(String sku) {
        Product product = createDefault();
        product.setSku(sku);
        return product;
    }
    
    /**
     * Creates a product with a specific category.
     */
    public static Product createWithCategory(Category category) {
        Product product = createDefault();
        product.setCategory(category);
        return product;
    }
    
    /**
     * Creates multiple products with default values.
     */
    public static List<Product> createList(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(createDefault());
        }
        return products;
    }
    
    /**
     * Fluent builder for more complex product scenarios.
     */
    public static ProductBuilder aProduct() {
        return new ProductBuilder();
    }
    
    /**
     * Common product scenarios as named factory methods.
     */
    public static class Products {
        public static Product laptop() {
            return aProduct()
                    .withName("Gaming Laptop")
                    .withSku("LAPTOP-001")
                    .withPrice("1299.99")
                    .withStock(50)
                    .withLowStockThreshold(5)
                    .build();
        }
        
        public static Product smartphone() {
            return aProduct()
                    .withName("Smartphone Pro")
                    .withSku("PHONE-001")
                    .withPrice("899.99")
                    .withStock(100)
                    .build();
        }
        
        public static Product tshirt() {
            return aProduct()
                    .withName("Cotton T-Shirt")
                    .withSku("TSHIRT-001")
                    .withPrice("29.99")
                    .withStock(200)
                    .withLowStockThreshold(20)
                    .build();
        }
        
        public static Product expensiveWatch() {
            return aProduct()
                    .withName("Luxury Watch")
                    .withSku("WATCH-001")
                    .withPrice("5999.99")
                    .withStock(10)
                    .build();
        }
    }
    
    /**
     * Fluent builder for creating customized products.
     */
    public static class ProductBuilder {
        private final Product product;
        
        private ProductBuilder() {
            this.product = createDefault();
        }
        
        public ProductBuilder withId(String id) {
            product.setId(id);
            return this;
        }
        
        public ProductBuilder withName(String name) {
            product.setName(name);
            return this;
        }
        
        public ProductBuilder withDescription(String description) {
            product.setDescription(description);
            return this;
        }
        
        public ProductBuilder withSku(String sku) {
            product.setSku(sku);
            return this;
        }
        
        public ProductBuilder withPrice(String price) {
            product.setBasePrice(new BigDecimal(price));
            return this;
        }
        
        public ProductBuilder withPrice(BigDecimal price) {
            product.setBasePrice(price);
            return this;
        }
        
        public ProductBuilder withStock(int quantity) {
            product.setStockQuantity(quantity);
            return this;
        }
        
        public ProductBuilder withReservedStock(int quantity) {
            product.setReservedQuantity(quantity);
            return this;
        }
        
        public ProductBuilder withLowStockThreshold(int threshold) {
            product.setLowStockThreshold(threshold);
            return this;
        }
        
        public ProductBuilder withCategory(Category category) {
            product.setCategory(category);
            return this;
        }
        
        public ProductBuilder withStatus(ProductStatus status) {
            product.setStatus(status);
            return this;
        }
        
        public ProductBuilder thatIsActive() {
            product.setStatus(ProductStatus.ACTIVE);
            product.setPublishedAt(OffsetDateTime.now());
            return this;
        }
        
        public ProductBuilder thatIsInactive() {
            product.setStatus(ProductStatus.INACTIVE);
            return this;
        }
        
        public ProductBuilder thatIsDiscontinued() {
            product.setStatus(ProductStatus.DISCONTINUED);
            return this;
        }
        
        public ProductBuilder thatIsLowOnStock() {
            product.setStockQuantity(product.getLowStockThreshold() - 1);
            return this;
        }
        
        public ProductBuilder thatIsOutOfStock() {
            product.setStockQuantity(0);
            product.setReservedQuantity(0);
            return this;
        }
        
        public ProductBuilder withoutInventoryTracking() {
            product.setTrackInventory(false);
            return this;
        }
        
        public Product build() {
            return product;
        }
    }
}
