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

#### **TDD Phase 3: GREEN Results (✅ COMPLETED)**

**Final Test Execution:**
```bash
cd services/product-service
mvn test -Dtest="*RepositoryTest"
```

**Test Results - GREEN Phase ✅**
```
[INFO] Tests run: 29, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] Total time: 9.318 s
```

**Test Coverage Achieved:**
- ✅ **ProductRepositoryTest**: 16 test methods - All passing
- ✅ **CategoryRepositoryTest**: 13 test methods - All passing  
- ✅ **Total Repository Tests**: 29 tests covering all functionality

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
- ✅ 29 repository tests passing with full coverage (including 4 new active filtering tests)
- ✅ TDD cycle completed (RED → GREEN → verified)
- ✅ H2 in-memory database configured for testing
- ✅ Field mapping issues resolved (isActive vs active)
- ✅ Design decision documented (@Where vs convenience methods)

### **Next: Step 7 - Service Layer**
- 🔄 Implement ProductService with business logic
- 🔄 Implement CategoryService with hierarchy management  
- 🔄 Add comprehensive service tests
- 🔄 Apply validation and error handling

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
