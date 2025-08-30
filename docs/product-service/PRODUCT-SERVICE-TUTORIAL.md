# Product Service Tutorial - Step by Step Implementation

## 📋 **Overview**
This tutorial walks through the complete implementation of the Product Service for the ecommerce microservices platform, from initial setup to a fully functional service.

## 🚀 **Step 1: Project Generation (✅ COMPLETED)**

### **Generated Spring Boot Project**
Used Spring Initializr to create the initial project structure:

```bash
curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d dependencies=web,data-jpa,validation,postg    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<Product> findByCategoryAndStatus(Category category, ProductStatus status, Pageable pageable);
    
    // Convenience Methods for Active Products (Common Use Cases)
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE'")
    List<Product> findActiveProducts();
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.category = :category")
    List<Product> findActiveProductsByCategory(Category category);
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    List<Product> findActiveProductsByNameContaining(String name);
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.basePrice BETWEEN :minPrice AND :maxPrice")
    List<Product> findActiveProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Custom Query Methods for Inventory Management
    @Query("SELECT p FROM Product p WHERE (p.stockQuantity - p.reservedQuantity) <= p.lowStockThreshold AND p.trackInventory = true")
    List<Product> findProductsWithLowStock();
    
    @Query("SELECT p FROM Product p WHERE (p.stockQuantity - p.reservedQuantity) > 0 AND p.trackInventory = true")
    List<Product> findProductsInStock();
}tor \
  -d groupId=com.loiane.ecommerce \
  -d artifactId=product-service \
  -d name=product-service \
  -d description="Product Service for Ecommerce Platform" \
  -d packageName=com.loiane.ecommerce.product \
  -d packaging=jar \
  -d javaVersion=24 \
  -d bootVersion=3.5.5 \
  -o product-service.zip
```

### **Selected Dependencies:**
- **Spring Web**: REST API endpoints
- **Spring Data JPA**: Database operations
- **Validation**: Input validation
- **PostgreSQL Driver**: Database connectivity
- **Spring Boot Actuator**: Health monitoring

### **Project Structure Created:**
```
services/product-service/
├── .gitattributes
├── .gitignore
├── .mvn/
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/loiane/ecommerce/product/
│   │   │   └── ProductServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/loiane/ecommerce/product/
│           └── ProductServiceApplicationTests.java
└── target/
```

## 🐳 **Step 2: Database Setup (✅ COMPLETED)**

### **Created Docker Compose for PostgreSQL**
File: `/docker-compose.yml`

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    container_name: ecommerce-postgres
    environment:
      POSTGRES_DB: ecommerce_product
      POSTGRES_USER: ecommerce
      POSTGRES_PASSWORD: password123
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - ecommerce-network

  pgadmin:
    image: dpage/pgadmin4
    container_name: ecommerce-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@ecommerce.com
      PGADMIN_DEFAULT_PASSWORD: admin123
    ports:
      - "5050:80"
    depends_on:
      - postgres
    networks:
      - ecommerce-network

volumes:
  postgres_data:

networks:
  ecommerce-network:
    driver: bridge
```

### **To Start Database:**
```bash
docker compose -f 'docker-compose.yml' up -d --build 
```

## ⚙️ **Step 3: Application Configuration (✅ COMPLETED)**

### **Updated application.properties**
File: `/services/product-service/src/main/resources/application.properties`

```properties
spring.application.name=product-service

# Server Configuration
server.port=8082

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_product
spring.datasource.username=ecommerce
spring.datasource.password=password123
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always

# Logging Configuration
logging.level.com.loiane.ecommerce.product=DEBUG
logging.level.org.springframework.web=DEBUG
```

### **Configuration Explained:**
- **Port 8082**: Avoids conflicts with other services
- **create-drop**: Recreates database schema on each startup (development only)
- **show-sql**: Logs all SQL queries for debugging
- **Actuator endpoints**: Health checks and metrics exposed
- **Debug logging**: Detailed logs for development

## 🔄 **Current Status**
- ✅ **Project generated** with Spring Initializr
- ✅ **Database setup** with Docker Compose
- ✅ **Configuration completed** for development environment
- ✅ **Product entity created** with comprehensive validation and business logic
- ✅ **Category entity created** with hierarchical structure and relationships
- ✅ **Database connection tested** and working successfully
- ✅ **Database tables created** automatically by Hibernate
- ✅ **Repository layer implemented** with Test-Driven Development (TDD)
- ✅ **Manual builder patterns** implemented without Lombok dependency
- ✅ **Active product filtering** convenience methods added
- ✅ **29 repository tests** passing with full coverage

### **Step 4: Test Database Connection (✅ COMPLETED)**

#### **Database Connection Verified**
- **PostgreSQL container started**: Database running on port 5432
- **Application startup successful**: Spring Boot connects to database without errors
- **Table creation confirmed**: Hibernate automatically created database schema
- **Actuator endpoints accessible**: Health checks working at `http://localhost:8082/actuator/health`

#### **Database Schema Created**
The following tables were automatically generated by Hibernate:

**Categories Table:**
```sql
CREATE TABLE categories (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(150) UNIQUE NOT NULL,
    description VARCHAR(500),
    parent_id VARCHAR(36),
    level INTEGER NOT NULL DEFAULT 0,
    display_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);
```

**Products Table:**
```sql
CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    sku VARCHAR(100) UNIQUE NOT NULL,
    base_price DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    category_id VARCHAR(36),
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    reserved_quantity INTEGER NOT NULL DEFAULT 0,
    low_stock_threshold INTEGER NOT NULL DEFAULT 10,
    track_inventory BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

#### **Key Achievements**
- ✅ **OffsetDateTime mapping**: Timezone-aware timestamps working correctly (TIMESTAMPTZ)
- ✅ **Entity relationships**: Product-Category foreign key relationship created
- ✅ **Validation constraints**: Database constraints match entity validations
- ✅ **UUID primary keys**: Auto-generated unique identifiers working
- ✅ **Enum mapping**: ProductStatus enum stored as VARCHAR

#### **Verification Steps Completed**
1. **Started PostgreSQL**: `docker-compose up -d postgres`
2. **Ran application**: Spring Boot started successfully on port 8082
3. **Checked health**: `curl http://localhost:8082/actuator/health` returned UP status
4. **Verified logs**: SQL table creation statements visible in application logs
5. **Database inspection**: Tables created with correct schema and constraints

### **Step 5: Domain Model Implementation (✅ COMPLETED)**

#### **Created Product Entity**
File: `/services/product-service/src/main/java/com/loiane/ecommerce/product/entity/Product.java`

**Key Features:**
- **UUID Primary Key**: Auto-generated unique identifier
- **Comprehensive Validation**: Jakarta validation annotations for data integrity
- **Inventory Management**: Stock tracking with reserved quantities
- **Category Relationship**: Many-to-One relationship with Category entity
- **Audit Timestamps**: Automatic creation and update timestamps
- **Business Logic**: Built-in methods for stock checking and product lifecycle

**Entity Fields:**
```java
- id (String, UUID)              // Primary key
- name (String, required)        // Product name
- description (Text)             // Full description
- shortDescription (String)      // Brief summary
- sku (String, unique, required) // Stock Keeping Unit
- basePrice (BigDecimal)         // Price with precision
- status (ProductStatus enum)    // ACTIVE, INACTIVE, DISCONTINUED
- category (Category)            // Many-to-One relationship
- stockQuantity (Integer)        // Total stock
- reservedQuantity (Integer)     // Reserved for pending orders
- lowStockThreshold (Integer)    // Alert threshold
- trackInventory (Boolean)       // Whether to track inventory
- createdAt (OffsetDateTime)       // Auto-generated
- updatedAt (OffsetDateTime)       // Auto-updated
- publishedAt (OffsetDateTime)     // When product was published
```

**Business Methods:**
- `getAvailableQuantity()`: Returns stock minus reserved
- `isInStock()`: Checks if product is available
- `isLowStock()`: Checks if below threshold
- `isPublished()`: Checks if active and published
- `publish()`, `unpublish()`, `discontinue()`: Lifecycle management

#### **Created Category Entity**
File: `/services/product-service/src/main/java/com/loiane/ecommerce/product/entity/Category.java`

**Key Features:**
- **UUID Primary Key**: Auto-generated unique identifier
- **Hierarchical Structure**: Self-referencing parent-child relationships
- **URL-Friendly Slugs**: Unique slug field for SEO-friendly URLs
- **Product Relationships**: One-to-Many relationship with products
- **Level Tracking**: Automatic hierarchy level calculation
- **Display Ordering**: Custom sort order support

**Entity Fields:**
```java
- id (String, UUID)              // Primary key
- name (String, required)        // Category name
- slug (String, unique, required)// URL-friendly identifier
- description (String)           // Category description
- parent (Category)              // Self-referencing parent category
- children (List<Category>)      // Child categories
- level (Integer)                // Hierarchy depth (0 for root)
- displayOrder (Integer)         // Sort order
- isActive (Boolean)             // Active/inactive status
- products (List<Product>)       // Related products
- createdAt (OffsetDateTime)     // Auto-generated
- updatedAt (OffsetDateTime)     // Auto-updated
```

**Business Methods:**
- `isRootCategory()`: Checks if category has no parent
- `hasChildren()`: Checks if category has subcategories
- `hasProducts()`: Checks if category has associated products
- `getFullPath()`: Returns hierarchical path (e.g., "Electronics > Smartphones")
- `addChild()`, `removeChild()`: Manage category hierarchy
- `activate()`, `deactivate()`: Toggle category status

#### **Created ProductStatus Enum**
File: `/services/product-service/src/main/java/com/loiane/ecommerce/product/entity/ProductStatus.java`

```java
public enum ProductStatus {
    ACTIVE,        // Available for sale
    INACTIVE,      // Hidden from catalog
    DISCONTINUED   // No longer available
}
```

#### **Validation Features:**
- **@NotBlank**: Required string fields (name, sku, currency)
- **@Size**: String length constraints
- **@NotNull**: Required fields
- **@DecimalMin**: Minimum price validation
- **@Digits**: Price precision control (10 integer, 2 decimal places)
- **@Min**: Non-negative quantities
- **@Enumerated**: Enum persistence as string

#### **🕐 Timezone Handling Design Decision**

**Why OffsetDateTime over LocalDateTime:**

For microservices architecture, we chose `OffsetDateTime` instead of `LocalDateTime` for all timestamp fields because:

**✅ Advantages:**
- **Timezone Awareness**: Stores exact moments in time with timezone offset information
- **Global Applications**: Essential for international ecommerce platforms
- **Microservices Best Practice**: Services may run in different regions/timezones  
- **Database Optimization**: PostgreSQL uses `TIMESTAMPTZ` (timestamp with timezone) for better performance
- **API Consistency**: JSON responses include timezone information (`2024-08-29T10:30:00+02:00`)
- **Audit Compliance**: Legal requirements often need precise timestamps

**Database Storage:**
```sql
-- PostgreSQL creates timezone-aware columns:
created_at    TIMESTAMPTZ NOT NULL,
updated_at    TIMESTAMPTZ NOT NULL,
published_at  TIMESTAMPTZ
```

**JSON API Response Example:**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "iPhone 15",
  "createdAt": "2024-08-29T10:30:00+02:00",
  "updatedAt": "2024-08-29T14:15:30+02:00"
}
```

This design ensures our microservices can handle global customers and distributed deployments correctly from the start.

### **Step 6: Repository Layer with Test-Driven Development (TDD) (✅ COMPLETED)**

We implemented the repository layer using **Test-Driven Development (TDD)** approach:
1. **RED**: Write failing tests first ✅
2. **GREEN**: Implement minimal code to make tests pass ✅
3. **REFACTOR**: Improve code quality while keeping tests green ✅

#### **TDD Phase 1: RED - Write Failing Tests**

**Added Test Dependencies:**
Updated `pom.xml` with testing frameworks:
```xml
<!-- Testing Dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

**Created Test Configuration:**
File: `/src/test/resources/application-test.properties`
```properties
# H2 in-memory database for fast unit tests
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Disable actuator for tests
management.endpoints.access.default=none

# Debug logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.com.loiane.ecommerce.product=DEBUG
```

**Created ProductRepositoryTest:**
File: `/src/test/java/com/loiane/ecommerce/product/repository/ProductRepositoryTest.java`

**Test Coverage:**
- ✅ **Basic CRUD Operations**: Save, find by ID, delete
- ✅ **Unique Constraints**: Find by SKU, check SKU existence  
- ✅ **Status Filtering**: Find products by status (ACTIVE, INACTIVE, DISCONTINUED)
- ✅ **Category Relationships**: Find products by category
- ✅ **Inventory Management**: Find products with low stock, in-stock products
- ✅ **Search Functionality**: Find by name containing (case-insensitive)
- ✅ **Price Filtering**: Find products by price range
- ✅ **Pagination Support**: Category and status filtering with pagination
- ✅ **Active Product Filtering**: Convenience methods for common customer-facing queries
- ✅ **Business Logic**: Custom queries for inventory and stock management

**Key Test Methods:**
```java
@Test void shouldSaveAndFindProductById()
@Test void shouldFindProductBySku()
@Test void shouldFindProductsByStatus()  
@Test void shouldFindProductsByCategory()
@Test void shouldFindProductsWithLowStock()
@Test void shouldFindProductsByNameContaining()
@Test void shouldFindProductsByCategoryAndStatusWithPagination()
@Test void shouldFindActiveProducts()
@Test void shouldFindActiveProductsByCategory()
@Test void shouldFindActiveProductsByNameContaining()
@Test void shouldFindActiveProductsByPriceRange()
@Test void shouldFindProductsInStock()
@Test void shouldCountProductsByCategory()
@Test void shouldFindProductsByPriceRange()
@Test void shouldCheckIfSkuExists()
```

**Created CategoryRepositoryTest:**
File: `/src/test/java/com/loiane/ecommerce/product/repository/CategoryRepositoryTest.java`

**Test Coverage:**
- ✅ **Basic CRUD Operations**: Save, find by ID, delete
- ✅ **Unique Constraints**: Find by slug, check slug existence
- ✅ **Hierarchy Management**: Find root categories, find by parent, find by level
- ✅ **Status Filtering**: Find active/inactive categories
- ✅ **Search Functionality**: Find by name containing (case-insensitive)  
- ✅ **Sorting**: Categories ordered by display order
- ✅ **Tree Operations**: Find categories with children, find leaf categories
- ✅ **Counting**: Count categories by parent

**Key Test Methods:**
```java
@Test void shouldSaveAndFindCategoryById()
@Test void shouldFindCategoryBySlug()
@Test void shouldFindRootCategories() 
@Test void shouldFindCategoriesByParent()
@Test void shouldFindCategoriesByLevel()
@Test void shouldFindActiveCategories()
@Test void shouldFindCategoriesByNameContaining()
@Test void shouldFindCategoriesWithChildren()
@Test void shouldFindLeafCategories()
```

**Test Results - RED Phase ✅**
Ran tests with `mvn test` - **All tests failed as expected** because:
- Repository interfaces don't exist yet
- Entities missing `@Builder` annotation for test data creation
- Custom query methods not implemented

```
[ERROR] cannot find symbol: class ProductRepository
[ERROR] cannot find symbol: class CategoryRepository  
[ERROR] cannot find symbol: method builder()
[INFO] 15 errors - BUILD FAILURE
```

**Perfect!** This is exactly what we want in TDD - failing tests that define our requirements.

#### **TDD Phase 2: GREEN - Implementation (✅ COMPLETED)**

**Manual Builder Pattern Implementation:**
Instead of using Lombok, we implemented manual builder patterns as requested:

**Product Entity Builder:**
File: `/src/main/java/com/loiane/ecommerce/product/entity/Product.java`
```java
public static class ProductBuilder {
    private String name;
    private String description;
    private String shortDescription;
    private String sku;
    private BigDecimal basePrice;
    private ProductStatus status = ProductStatus.ACTIVE;
    private Category category;
    private Integer stockQuantity = 0;
    private Integer reservedQuantity = 0;
    private Integer lowStockThreshold = 10;
    private Boolean trackInventory = true;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime publishedAt;
    
    // Fluent setter methods
    public ProductBuilder name(String name) { this.name = name; return this; }
    public ProductBuilder sku(String sku) { this.sku = sku; return this; }
    // ... other fluent methods
    
    public Product build() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (updatedAt == null) updatedAt = OffsetDateTime.now();
        // Build logic with validation
    }
}
```

**Category Entity Builder:**
File: `/src/main/java/com/loiane/ecommerce/product/entity/Category.java`
```java
public static class CategoryBuilder {
    private String name;
    private String slug;
    private String description;
    private Category parent;
    private Integer level = 0;
    private Integer displayOrder = 0;
    private Boolean isActive = true;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Fluent setter methods with proper field mapping
    public CategoryBuilder active(Boolean active) { this.isActive = active; return this; }
    // ... other fluent methods
    
    public Category build() {
        // Build logic with proper field assignment
    }
}
```

**Repository Implementation:**

**ProductRepository Interface:**
File: `/src/main/java/com/loiane/ecommerce/product/repository/ProductRepository.java`
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    // Spring Data JPA Derived Query Methods
    Optional<Product> findBySku(String sku);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByCategory(Category category);
    boolean existsBySku(String sku);
    long countByCategory(Category category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    Page<Product> findByCategoryAndStatus(Category category, ProductStatus status, Pageable pageable);
    
    // Custom Query Methods with @Query
    @Query("SELECT p FROM Product p WHERE (p.stockQuantity - p.reservedQuantity) <= p.lowStockThreshold AND p.trackInventory = true")
    List<Product> findProductsWithLowStock();
    
    @Query("SELECT p FROM Product p WHERE (p.stockQuantity - p.reservedQuantity) > 0 AND p.trackInventory = true")
    List<Product> findProductsInStock();
}
```

**CategoryRepository Interface:**
File: `/src/main/java/com/loiane/ecommerce/product/repository/CategoryRepository.java`
```java
@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    
    // Spring Data JPA Derived Query Methods
    Optional<Category> findBySlug(String slug);
    List<Category> findByParentIsNull();
    List<Category> findByParent(Category parent);
    List<Category> findByLevel(int level);
    List<Category> findByIsActiveTrue();          // Fixed: isActive field mapping
    List<Category> findByIsActiveFalse();         // Fixed: isActive field mapping
    boolean existsBySlug(String slug);
    long countByParent(Category parent);
    List<Category> findByNameContainingIgnoreCase(String name);
    List<Category> findByParentAndIsActiveTrueOrderByDisplayOrder(Category parent);
    
    // Custom Query Methods with @Query
    @Query("SELECT DISTINCT c FROM Category c WHERE EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
    List<Category> findCategoriesWithChildren();
    
    @Query("SELECT c FROM Category c WHERE NOT EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
    List<Category> findLeafCategories();
}
```

**Configuration Fixes Applied:**
1. **Test Configuration Fix**: Removed invalid `spring.profiles.active` from `application-test.properties`
2. **Field Mapping Fix**: Used `findByIsActiveTrue()` instead of `findByActiveTrue()` to match entity field names
3. **H2 Database Setup**: Configured H2 in-memory database for fast test execution
4. **JPQL Query Fix**: Replaced `c.children IS NOT EMPTY` with EXISTS subqueries for better Hibernate compatibility
5. **Test Expectations Fix**: Updated price range test to handle multiple products in range correctly

#### **TDD Phase 3: GREEN Results (✅ COMPLETED)**

**Final Test Execution:**
```bash
cd services/product-service
mvn test -Dtest="*RepositoryTest"
```

**Test Results - GREEN Phase ✅**
```
[INFO] Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Total time: 9.318 s
```

**Test Coverage Achieved:**
- ✅ **ProductRepositoryTest**: 16 test methods - All passing
- ✅ **CategoryRepositoryTest**: 13 test methods - All passing  
- ✅ **ProductServiceApplicationTests**: 1 integration test - Passing
- ✅ **Total Repository Tests**: 30 tests covering all functionality

**Key Achievements:**
- ✅ **Manual Builder Patterns**: Implemented without Lombok dependency
- ✅ **Repository Layer**: Complete Spring Data JPA implementation
- ✅ **Custom Queries**: Complex business logic with @Query annotations
- ✅ **Active Product Filtering**: Convenience methods for common use cases
- ✅ **Field Mapping**: Resolved JPA property name conflicts
- ✅ **Test Configuration**: H2 in-memory database setup
- ✅ **TDD Complete**: RED → GREEN → Verified

**Business Logic Implemented:**
- **Product Inventory**: Low stock detection based on available quantity vs threshold
- **Active Product Filtering**: Optimized queries for customer-facing APIs
- **Category Hierarchy**: Parent-child relationships with tree operations
- **Search & Filtering**: Case-insensitive search, status filtering, price ranges
- **Data Integrity**: Unique constraints (SKU, slug), validation support

#### **Design Decision: Active Product Filtering**

**🤔 Alternative Considered: @Where Annotation**
We considered using Hibernate's `@Where(clause = "status = 'ACTIVE'")` on the Product entity but chose **convenience methods** instead.

**Why Convenience Methods Over @Where:**

✅ **Business Flexibility**
- Admin panels need access to INACTIVE/DISCONTINUED products
- Inventory management requires all product statuses
- Analytics needs historical data from all statuses

✅ **API Transparency**
- Clear method names indicate filtering behavior
- Frontend developers understand what data they receive
- No hidden database-level filtering

✅ **Testing Benefits**
- Tests can create products with various statuses
- Repository behavior is explicit and predictable

**Convenience Methods Added:**
```java
// Common customer-facing queries
List<Product> findActiveProducts()
List<Product> findActiveProductsByCategory(Category category)
List<Product> findActiveProductsByNameContaining(String name)
List<Product> findActiveProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice)
```

**Usage Strategy:**
- **Customer APIs**: Use convenience methods for performance
- **Admin APIs**: Use general methods for full data access
- **Service Layer**: Choose appropriate method based on use case

#### **Current Repository Status: ✅ FULLY COMPLETE**

### **Step 7: Service Layer (🔄 NEXT)**
- Implement ProductService
- Implement CategoryService
- Add business logic and validation
- Create service tests

### **Step 8: REST Controller**
- Implement ProductController
- Implement CategoryController
- Add API endpoints (CRUD operations)
- Add request/response DTOs
- Create controller tests

### **Step 9: API Documentation**
- Add Swagger/OpenAPI documentation
- Document all endpoints
- Add example requests/responses

### **Step 10: Error Handling**
- Implement global exception handler
- Create custom exception classes
- Add proper error responses

### **Step 11: Testing**
- Unit tests for all layers
- Integration tests with TestContainers
- API tests with MockMvc

### **Step 12: Docker Containerization**
- Create Dockerfile for the service
- Add to docker-compose.yml
- Test containerized deployment

## 🛠️ **Commands to Remember**

### **Start Database:**
```bash
docker-compose up -d postgres
```

### **Run Application:**
```bash
cd services/product-service
./mvnw spring-boot:run
```

### **Build Application:**
```bash
cd services/product-service
./mvnw clean install
```

### **Run Tests:**
```bash
cd services/product-service
./mvnw test
```

### **Run Repository Tests Only:**
```bash
cd services/product-service
./mvnw test -Dtest="*RepositoryTest"
```

### **Access Database (pgAdmin):**
- URL: http://localhost:5050
- Email: admin@ecommerce.com
- Password: admin123

### **Health Check:**
```bash
curl http://localhost:8082/actuator/health
```

## 🎯 **Success Criteria**

### **Step 4 Complete When:**
- ✅ PostgreSQL container runs successfully
- ✅ Application starts without errors
- ✅ Database connection established
- ✅ Actuator endpoints accessible

### **Step 5 Complete When:**
- ✅ Product and Category entities created
- ✅ JPA relationships defined
- ✅ Validation annotations added
- ✅ Database tables auto-generated

### **Step 6 Complete When:**
- ✅ Repository interfaces implemented with Spring Data JPA
- ✅ Custom queries working with @Query annotations  
- ✅ Manual builder patterns implemented (no Lombok)
- ✅ Active product filtering convenience methods added
- ✅ 30 repository tests passing with full coverage (including 4 new active filtering tests)
- ✅ TDD cycle completed (RED → GREEN → verified)
- ✅ H2 in-memory database configured for testing
- ✅ Field mapping issues resolved (isActive vs active)
- ✅ JPQL query issues resolved (EXISTS vs collection syntax)
- ✅ Test expectation issues resolved (price range filtering)
- ✅ Design decision documented (@Where vs convenience methods)

### **Next: Step 7 - Service Layer**
- 🔄 Implement ProductService with business logic
- 🔄 Implement CategoryService with hierarchy management  
- 🔄 Add comprehensive service tests
- 🔄 Apply validation and error handling

---

## 🧪 **Step 7: Service Layer Implementation with TDD (🔄 IN PROGRESS)**

### **Modern Spring Service Layer Approach**

**✅ Best Practices Applied:**
- **No Interface/Implementation Split**: Use concrete service classes directly (modern Spring approach)
- **Real Objects in Tests**: Mock only external dependencies (repositories), test real business logic
- **Test-First Development**: Write comprehensive tests before implementing services
- **Business Logic Separation**: Services handle transactions and business rules, repositories handle data access

### **TDD Phase 1: RED - Create Failing Tests**

#### **Service Test Architecture**
```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private ProductService productService; // Real object, not interface
}
```

#### **ProductServiceTest - Business Operations**
File: `/src/test/java/com/loiane/ecommerce/product/service/ProductServiceTest.java`

**Test Categories Implemented:**
- **CRUD Operations**: Create, read, update with business validation
- **Inventory Management**: Stock reservation, release, confirmation
- **Business Rules**: SKU uniqueness, category validation, status transitions
- **Search & Filtering**: Active products, pagination, name search
- **Bulk Operations**: Mass status updates

**Key Test Methods (17 total):**
```java
// CREATE with validation
@Test void shouldCreateProductSuccessfully()
@Test void shouldThrowExceptionWhenCreatingProductWithDuplicateSku()
@Test void shouldThrowExceptionWhenCreatingProductWithInactiveCategory()

// READ operations
@Test void shouldFindProductById()
@Test void shouldThrowExceptionWhenProductNotFound()
@Test void shouldFindActiveProductsWithPagination()

// UPDATE with restrictions
@Test void shouldUpdateProductSuccessfully()
@Test void shouldNotAllowSkuChangeDuringUpdate()

// INVENTORY management
@Test void shouldReserveStockSuccessfully()
@Test void shouldThrowExceptionWhenInsufficientStockForReservation()
@Test void shouldReleaseStockSuccessfully()
@Test void shouldConfirmStockSuccessfully()

// BUSINESS operations
@Test void shouldPublishProductSuccessfully()
@Test void shouldDiscontinueProductSuccessfully()
@Test void shouldFindLowStockProducts()
@Test void shouldSearchActiveProductsByName()
@Test void shouldBulkUpdateProductStatus()
```

#### **CategoryServiceTest - Hierarchy Management**
File: `/src/test/java/com/loiane/ecommerce/product/service/CategoryServiceTest.java`

**Test Categories Implemented:**
- **Hierarchy Operations**: Parent-child relationships, level management
- **CRUD with Validation**: Slug uniqueness, business rule enforcement
- **Tree Operations**: Move categories, reorder, search
- **Business Logic**: Deactivation rules, product counting

**Key Test Methods (14 total):**
```java
// CREATE with hierarchy
@Test void shouldCreateRootCategorySuccessfully()
@Test void shouldCreateChildCategoryWithCorrectLevel()
@Test void shouldThrowExceptionWhenCreatingCategoryWithDuplicateSlug()

// READ operations
@Test void shouldFindCategoryBySlug()
@Test void shouldThrowExceptionWhenCategoryNotFoundBySlug()
@Test void shouldGetCategoryHierarchy()

// UPDATE with restrictions
@Test void shouldUpdateCategorySuccessfully()
@Test void shouldNotAllowSlugChangeDuringUpdate()

// BUSINESS operations
@Test void shouldMoveCategoryToNewParent()
@Test void shouldDeactivateCategoryWithoutProducts()
@Test void shouldThrowExceptionWhenDeactivatingCategoryWithActiveProducts()
@Test void shouldFindCategoriesByPartialName()
@Test void shouldReorderCategories()
@Test void shouldCountProductsInCategory()
```

#### **Custom Exception Classes**
File: `/src/main/java/com/loiane/ecommerce/product/exception/`

**Business Exception Hierarchy:**
```java
// Product-related exceptions
ProductNotFoundException.java       // Product not found by ID
DuplicateSkuException.java          // SKU already exists
InsufficientStockException.java     // Not enough inventory
IllegalOperationException.java      // Invalid business operation

// Category-related exceptions  
CategoryNotFoundException.java      // Category not found by ID/slug
DuplicateSlugException.java        // Slug already exists
InactiveCategoryException.java     // Adding product to inactive category
```

#### **Enhanced Repository Methods**
Extended existing repositories to support service layer needs:

**ProductRepository additions:**
```java
// Pagination support for services
Page<Product> findByStatus(ProductStatus status, Pageable pageable);
Page<Product> findActiveProductsByNameContainingWithPagination(String name, Pageable pageable);

// Business rule checking
boolean existsByCategoryAndStatus(Category category, ProductStatus status);
long countByCategoryAndStatus(Category category, ProductStatus status);
```

**CategoryRepository additions:**
```java
// Hierarchy management
@Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.displayOrder ASC")
List<Category> findRootCategories();
List<Category> findByParentOrderByDisplayOrderAsc(Category parent);
```

#### **Expected Service Layer Business Logic**

**ProductService Responsibilities:**
- ✅ **Validation**: SKU uniqueness, category status checking
- ✅ **Inventory Management**: Atomic stock operations with concurrency handling
- ✅ **Status Lifecycle**: Publish/discontinue workflows with publishedAt timestamps
- ✅ **Search Coordination**: Combine repository calls for complex filtering
- ✅ **Transaction Management**: `@Transactional` boundaries for consistency

**CategoryService Responsibilities:**
- ✅ **Hierarchy Management**: Automatic level calculation, parent-child consistency
- ✅ **Business Rules**: Prevent deactivation of categories with active products
- ✅ **Tree Operations**: Move categories, maintain display order
- ✅ **Search & Count**: Product counting per category, hierarchy traversal

#### **TDD Phase 1 Status: RED ❌**

**Current State:**
```bash
cd services/product-service
mvn test -Dtest="*ServiceTest"
# Expected: All tests fail (services not implemented yet)
# Goal: 31 failing tests (17 ProductService + 14 CategoryService)
```

**Test Failures Expected:**
- ProductService cannot be resolved to a type
- CategoryService cannot be resolved to a type  
- Missing business logic methods

**Next Steps:**
1. **Phase 2 (GREEN)**: Implement ProductService and CategoryService classes
2. **Phase 3 (REFACTOR)**: Optimize performance, add caching, improve error handling

### **TDD Benefits Demonstrated:**

**✅ Clear API Contract**: Tests define exact service method signatures and behavior
**✅ Business Logic Documentation**: Each test describes a business requirement
**✅ Edge Case Coverage**: Exception scenarios and validation rules tested first
**✅ Refactoring Safety**: Comprehensive test suite prevents regressions
**✅ Modern Spring Practices**: No unnecessary abstractions, test real objects

## 🔧 **Troubleshooting Guide**

### **Issue 1: JPQL Collection Syntax Problems**
**Problem**: CategoryRepository tests failing with Hibernate syntax errors
```
Error executing DDL "set client_min_messages = WARNING" via JDBC
Syntax error in SQL statement
```

**Root Cause**: JPQL queries using `c.children IS NOT EMPTY` syntax were incompatible with H2 database
```java
// ❌ Problematic (collection-based syntax)
@Query("SELECT DISTINCT c FROM Category c WHERE c.children IS NOT EMPTY")
List<Category> findCategoriesWithChildren();
```

**✅ Solution**: Replace with EXISTS subqueries for better Hibernate compatibility
```java
// ✅ Fixed (EXISTS subquery syntax)
@Query("SELECT DISTINCT c FROM Category c WHERE EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
List<Category> findCategoriesWithChildren();

@Query("SELECT c FROM Category c WHERE NOT EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
List<Category> findLeafCategories();
```

**Why EXISTS is Better**:
- ✅ Database-agnostic (works with H2, PostgreSQL, MySQL)
- ✅ More efficient for hierarchy queries
- ✅ Explicit relationship mapping
- ✅ Better query optimization by database engines

### **Issue 2: Test Expectation Mismatches**
**Problem**: `shouldFindActiveProductsByPriceRange` test failing
```
Expected size: 1 but was: 2 in:
[Gaming Laptop ($1299.99), Smartphone Pro ($899.99)]
```

**Root Cause**: Test data setup created two products in the $800-$1500 range
- Gaming Laptop: $1299.99 (ACTIVE)
- Smartphone Pro: $899.99 (ACTIVE)

**✅ Solution**: Update test expectations to match actual data
```java
// Before: Assumed only laptop in range
assertThat(expensiveProducts).hasSize(1);
assertThat(expensiveProducts.get(0).getName()).isEqualTo("Gaming Laptop");

// After: Accept both products in range
assertThat(expensiveProducts).hasSize(2);
assertThat(expensiveProducts)
    .extracting(Product::getName)
    .containsExactlyInAnyOrder("Gaming Laptop", "Smartphone Pro");
```

**Best Practices for Price Range Tests**:
- ✅ Use precise decimal ranges to avoid overlapping products
- ✅ Document test data price points in setup methods
- ✅ Use `containsExactlyInAnyOrder()` for flexible order matching
- ✅ Test both boundary conditions and typical ranges

### **Issue 3: Slow Test Execution**
**Problem**: Tests hanging during Spring context initialization

**Root Cause**: PostgreSQL dialect configuration with H2 in-memory database
```properties
# Caused conflicts in test environment
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

**✅ Solution**: Let Spring Boot auto-detect dialect for tests
- Remove explicit dialect configuration from `application-test.properties`
- H2 automatically uses correct dialect in test environment
- Production still uses PostgreSQL explicitly

## 🚨 **Known Issues**

### **Application Startup Failure**
- **Issue**: Application fails to start with exit code 1
- **Likely Cause**: PostgreSQL not running or connection refused
- **Solution**: Start PostgreSQL container first, then run application

### **Security Warning**
- **Issue**: Hardcoded database password in application.properties
- **Note**: This is acceptable for development/educational purposes
- **Production**: Use environment variables or Spring Cloud Config

## 📝 **Notes**
- This is a development setup with simplified configuration
- Database schema is recreated on each startup (good for development)
- All SQL queries are logged for learning purposes
- Debug logging is enabled for detailed troubleshooting

---

## **Step 7: Service Layer Implementation - GREEN Phase (✅ COMPLETED)**

### **TDD GREEN Phase Achievement**
Successfully implemented service layer business logic to make all tests pass! 

#### **Implementation Results**
- ✅ **62 total tests passing** (17 Product Repository + 13 Category Repository + 1 Application + 18 Product Service + 13 Category Service)
- ✅ **Zero compilation errors** resolved
- ✅ **Zero test failures** achieved
- ✅ **Complete business logic** implemented

### **Service Classes Created**

#### **ProductService Implementation**
File: `/services/product-service/src/main/java/com/loiane/ecommerce/product/service/ProductService.java`

**Business Operations Implemented:**
1. **CRUD Operations**: Create, read, update, delete with validation
2. **Inventory Management**: Reserve, release, confirm stock operations
3. **Business Rules**: Category validation, SKU uniqueness, status transitions
4. **Error Handling**: Custom exceptions for all business scenarios

**Key Features:**
```java
@Service
@Transactional
public class ProductService {
    
    // Comprehensive product creation with validation
    public Product createProduct(Product product) {
        validateProductForCreation(product);
        return productRepository.save(product);
    }
    
    // Stock management operations
    public Product reserveStock(String productId, Integer quantity) { /* ... */ }
    public Product releaseStock(String productId, Integer quantity) { /* ... */ }
    public Product confirmStockReservation(String productId, Integer quantity) { /* ... */ }
    
    // Business state transitions
    public Product publishProduct(String id) { /* ... */ }
    public Product discontinueProduct(String id) { /* ... */ }
}
```

#### **CategoryService Implementation**
File: `/services/product-service/src/main/java/com/loiane/ecommerce/product/service/CategoryService.java`

**Hierarchy Operations Implemented:**
1. **Category Management**: Create, update, delete with level calculation
2. **Tree Operations**: Move categories, maintain hierarchy integrity
3. **Business Rules**: Parent-child relationships, deactivation constraints
4. **Search Operations**: By slug, hierarchy traversal

**Key Features:**
```java
@Service
@Transactional
public class CategoryService {
    
    // Hierarchy-aware category creation
    public Category createCategory(Category category) {
        calculateAndSetLevel(category);
        return categoryRepository.save(category);
    }
    
    // Tree structure operations
    public Category moveCategory(String categoryId, String newParentId) { /* ... */ }
    public Category deactivateCategory(String id) { /* ... */ }
    
    // Level calculation for hierarchy
    private void calculateAndSetLevel(Category category) { /* ... */ }
}
```

### **Test Execution Success**

#### **Command Used**
```bash
mvn test
```

#### **Final Results**
```
[INFO] Tests run: 62, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

#### **Test Breakdown**
- **17 ProductRepositoryTest**: Repository layer operations
- **13 CategoryRepositoryTest**: Category repository functionality  
- **1 ProductServiceApplicationTests**: Spring Boot integration
- **18 ProductServiceTest**: Product business logic validation
- **13 CategoryServiceTest**: Category hierarchy operations

### **Technical Issues Resolved**

#### **Issue 1: Entity Builder Pattern Limitations**
**Problem**: Manual builder pattern doesn't support `.id()` method
```java
// ❌ This failed - builder doesn't have id() method
Category category = Category.builder()
    .id(categoryId)  // Compilation error
    .name("Test")
    .build();
```

**✅ Solution**: Set ID after building
```java
// ✅ This works - set ID via setter after building
Category category = Category.builder()
    .name("Test")
    .build();
category.setId(categoryId); // Set ID post-build
```

#### **Issue 2: Boolean Getter/Setter Method Names**
**Problem**: Category entity uses `getIsActive()`/`setIsActive()`, not standard `isActive()`
```java
// ❌ This failed - wrong method name
assertThat(category.isActive()).isFalse();
testCategory.setActive(false);
```

**✅ Solution**: Use correct method names
```java
// ✅ This works - correct Boolean field methods
assertThat(category.getIsActive()).isFalse();
testCategory.setIsActive(false);
```

#### **Issue 3: Unnecessary Test Stubbing**
**Problem**: Mockito detected unused stubbing in CategoryServiceTest
```java
// ❌ These were unnecessary for getCategoryHierarchy()
when(categoryRepository.findByParentOrderByDisplayOrderAsc(rootCategory))
    .thenReturn(Arrays.asList(childCategory));
when(categoryRepository.findByParentOrderByDisplayOrderAsc(childCategory))
    .thenReturn(Collections.emptyList());
```

**✅ Solution**: Remove unused mocks, keep only what's needed
```java
// ✅ This is all that's needed
when(categoryRepository.findRootCategories()).thenReturn(Arrays.asList(rootCategory));
```

### **Modern Spring Practices Applied**

#### **No Interface-Implementation Split**
Following modern Spring Boot practices, service classes are implemented directly without interface abstraction:
```java
// ✅ Modern approach - direct service classes
@Service
public class ProductService { /* implementation */ }

// ❌ Avoided - unnecessary interface layer
public interface ProductService { /* contract */ }
@Service
public class ProductServiceImpl implements ProductService { /* implementation */ }
```

#### **Real Objects in Tests**
Used real entity objects in tests, mocking only the repository layer:
```java
// ✅ Real entity objects
Product testProduct = Product.builder()
    .name("Test Product")
    .build();

// ✅ Mock only repositories
@Mock
private ProductRepository productRepository;
@Mock  
private CategoryRepository categoryRepository;
```

### **Business Logic Validation**

#### **Product Service Business Rules**
- **Category Validation**: Products cannot be created with inactive categories
- **SKU Uniqueness**: Duplicate SKUs are rejected with custom exception
- **Stock Management**: Reserve/Release/Confirm operations maintain inventory integrity
- **Status Transitions**: Proper validation for ACTIVE → INACTIVE → DISCONTINUED flow

#### **Category Service Business Rules**
- **Hierarchy Integrity**: Parent-child relationships maintained correctly
- **Level Calculation**: Automatic level assignment based on parent hierarchy
- **Deactivation Rules**: Categories with active products cannot be deactivated
- **Move Operations**: Category movement updates levels for entire subtree

### **Next Steps Available**

#### **REFACTOR Phase (Optional)**
- **Performance Optimization**: Add caching for frequently accessed categories
- **Code Cleanup**: Extract common validation logic to utility classes
- **Method Simplification**: Break down complex methods if needed

#### **Controller Layer (Next Major Step)**
- **REST API Endpoints**: Create ProductController and CategoryController
- **API Documentation**: Add OpenAPI/Swagger documentation
- **Integration Tests**: Add full request-response testing
