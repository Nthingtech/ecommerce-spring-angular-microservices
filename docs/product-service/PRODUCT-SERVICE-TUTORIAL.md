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
  -d dependencies=web,data-jpa,validation,postgresql,actuator \
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
- ❌ **Application startup failed** (likely due to missing database connection)

## 📋 **Next Steps (TO BE IMPLEMENTED)**

### **Step 4: Test Database Connection**
- Start PostgreSQL with Docker Compose
- Run the Spring Boot application
- Verify successful startup and database connection

### **Step 5: Domain Model Implementation**
- Create Product entity
- Create Category entity
- Define relationships and constraints
- Add validation annotations

### **Step 6: Repository Layer**
- Implement ProductRepository interface
- Implement CategoryRepository interface
- Add custom query methods
- Create repository tests

### **Step 7: Service Layer**
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
- ✅ Repository interfaces implemented
- ✅ Custom queries working
- ✅ Repository tests passing

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
