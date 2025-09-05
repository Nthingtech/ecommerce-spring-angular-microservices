package com.loiane.ecommerce.product.factory;

import com.loiane.ecommerce.product.entity.Category;
import com.loiane.ecommerce.product.entity.Product;
import com.loiane.ecommerce.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating complete test scenarios with related data.
 * Useful for integration tests and complex business logic testing.
 */
public class TestScenarios {
    
    // Price tier constants
    private static final String BUDGET_TIER = "BUDGET";
    private static final String MID_TIER = "MID";
    private static final String PREMIUM_TIER = "PREMIUM";
    
    private TestScenarios() {
        // Static factory class
    }
    
    /**
     * Creates a basic e-commerce catalog scenario with categories and products.
     */
    public static EcommerceScenario createBasicEcommerceCatalog() {
        // Create categories
        Category electronics = CategoryTestDataFactory.createRoot("Electronics");
        Category computers = CategoryTestDataFactory.createChild("Computers", electronics);
        Category laptops = CategoryTestDataFactory.createChild("Laptops", computers);
        
        // Create products
        List<Product> products = new ArrayList<>();
        
        // Active laptop products
        products.add(ProductTestDataFactory.aProduct()
                .withName("Gaming Laptop Pro")
                .withSku("LAPTOP-GAMING-001")
                .withPrice("1599.99")
                .withCategory(laptops)
                .withStock(25)
                .thatIsActive()
                .build());
        
        products.add(ProductTestDataFactory.aProduct()
                .withName("Business Laptop")
                .withSku("LAPTOP-BUSINESS-001")
                .withPrice("1299.99")
                .withCategory(laptops)
                .withStock(50)
                .thatIsActive()
                .build());
        
        // Low stock product
        products.add(ProductTestDataFactory.aProduct()
                .withName("Budget Laptop")
                .withSku("LAPTOP-BUDGET-001")
                .withPrice("599.99")
                .withCategory(laptops)
                .withStock(3)
                .withLowStockThreshold(5)
                .thatIsActive()
                .build());
        
        // Out of stock product
        products.add(ProductTestDataFactory.aProduct()
                .withName("Clearance Laptop")
                .withSku("LAPTOP-CLEARANCE-001")
                .withPrice("399.99")
                .withCategory(laptops)
                .thatIsOutOfStock()
                .thatIsActive()
                .build());
        
        // Discontinued product
        products.add(ProductTestDataFactory.aProduct()
                .withName("Old Model Laptop")
                .withSku("LAPTOP-OLD-001")
                .withPrice("799.99")
                .withCategory(laptops)
                .thatIsDiscontinued()
                .build());
        
        return new EcommerceScenario(
                List.of(electronics, computers, laptops),
                products
        );
    }
    
    /**
     * Creates a scenario for testing inventory management.
     */
    public static InventoryScenario createInventoryTestScenario() {
        Category category = CategoryTestDataFactory.createDefault();
        
        return new InventoryScenario(
                // High stock product
                ProductTestDataFactory.aProduct()
                        .withName("High Stock Product")
                        .withSku("HIGH-STOCK-001")
                        .withStock(1000)
                        .withReservedStock(50)
                        .withCategory(category)
                        .build(),
                
                // Low stock product
                ProductTestDataFactory.aProduct()
                        .withName("Low Stock Product")
                        .withSku("LOW-STOCK-001")
                        .withStock(5)
                        .withLowStockThreshold(10)
                        .withCategory(category)
                        .build(),
                
                // Out of stock product
                ProductTestDataFactory.aProduct()
                        .withName("Out of Stock Product")
                        .withSku("OUT-STOCK-001")
                        .thatIsOutOfStock()
                        .withCategory(category)
                        .build(),
                
                // Product with reservations
                ProductTestDataFactory.aProduct()
                        .withName("Reserved Product")
                        .withSku("RESERVED-001")
                        .withStock(100)
                        .withReservedStock(90)
                        .withCategory(category)
                        .build()
        );
    }
    
    /**
     * Creates a scenario for testing price ranges.
     */
    public static PriceRangeScenario createPriceRangeScenario() {
        Category category = CategoryTestDataFactory.createDefault();
        
        List<Product> products = new ArrayList<>();
        
        // Budget products (< $50)
        products.add(createProductWithPrice(category, "29.99", BUDGET_TIER));
        products.add(createProductWithPrice(category, "39.99", BUDGET_TIER));
        products.add(createProductWithPrice(category, "49.99", BUDGET_TIER));
        
        // Mid-range products ($50 - $200)
        products.add(createProductWithPrice(category, "99.99", MID_TIER));
        products.add(createProductWithPrice(category, "149.99", MID_TIER));
        products.add(createProductWithPrice(category, "199.99", MID_TIER));
        
        // Premium products (> $200)
        products.add(createProductWithPrice(category, "299.99", PREMIUM_TIER));
        products.add(createProductWithPrice(category, "499.99", PREMIUM_TIER));
        products.add(createProductWithPrice(category, "999.99", PREMIUM_TIER));
        
        return new PriceRangeScenario(products);
    }
    
    private static Product createProductWithPrice(Category category, String price, String tier) {
        return ProductTestDataFactory.aProduct()
                .withName(tier + " Product " + price)
                .withSku(tier + "-" + price.replace(".", ""))
                .withPrice(price)
                .withCategory(category)
                .thatIsActive()
                .build();
    }
    
    /**
     * Container for e-commerce scenario data.
     */
    public static class EcommerceScenario {
        private final List<Category> categories;
        private final List<Product> products;
        
        public EcommerceScenario(List<Category> categories, List<Product> products) {
            this.categories = categories;
            this.products = products;
        }
        
        public List<Category> getCategories() {
            return categories;
        }
        
        public List<Product> getProducts() {
            return products;
        }
        
        public List<Product> getActiveProducts() {
            return products.stream()
                    .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                    .toList();
        }
        
        public List<Product> getLowStockProducts() {
            return products.stream()
                    .filter(Product::isLowStock)
                    .toList();
        }
    }
    
    /**
     * Container for inventory test scenario data.
     */
    public static class InventoryScenario {
        private final Product highStockProduct;
        private final Product lowStockProduct;
        private final Product outOfStockProduct;
        private final Product productWithReservations;
        
        public InventoryScenario(Product highStockProduct, Product lowStockProduct,
                               Product outOfStockProduct, Product productWithReservations) {
            this.highStockProduct = highStockProduct;
            this.lowStockProduct = lowStockProduct;
            this.outOfStockProduct = outOfStockProduct;
            this.productWithReservations = productWithReservations;
        }
        
        public Product getHighStockProduct() {
            return highStockProduct;
        }
        
        public Product getLowStockProduct() {
            return lowStockProduct;
        }
        
        public Product getOutOfStockProduct() {
            return outOfStockProduct;
        }
        
        public Product getProductWithReservations() {
            return productWithReservations;
        }
    }
    
    /**
     * Container for price range scenario data.
     */
    public static class PriceRangeScenario {
        private final List<Product> products;
        
        public PriceRangeScenario(List<Product> products) {
            this.products = products;
        }
        
        public List<Product> getAllProducts() {
            return products;
        }
        
        public List<Product> getBudgetProducts() {
            return filterByPriceRange(BigDecimal.ZERO, new BigDecimal("50"));
        }
        
        public List<Product> getMidRangeProducts() {
            return filterByPriceRange(new BigDecimal("50"), new BigDecimal("200"));
        }
        
        public List<Product> getPremiumProducts() {
            return filterByPriceRange(new BigDecimal("200"), new BigDecimal("10000"));
        }
        
        private List<Product> filterByPriceRange(BigDecimal min, BigDecimal max) {
            return products.stream()
                    .filter(p -> p.getBasePrice().compareTo(min) >= 0 
                              && p.getBasePrice().compareTo(max) <= 0)
                    .toList();
        }
    }
}
