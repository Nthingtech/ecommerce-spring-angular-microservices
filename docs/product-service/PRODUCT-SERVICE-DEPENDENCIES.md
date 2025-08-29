# Product Service - Spring Dependencies Guide

## 🚀 **Core Dependencies**

### **Spring Boot Starters**
```xml
<!-- Web layer for REST APIs -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data persistence with JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Validation for request/response -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Production monitoring and health checks -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### **Database**
```xml
<!-- PostgreSQL driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Database migrations -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

### **Microservices (Optional for MVP)**
```xml
<!-- Service discovery client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- Configuration client (if using config server) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

### **Testing**
```xml
<!-- Comprehensive testing support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- In-memory database for testing -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>

<!-- Test containers for integration tests -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
```

## 🎯 **Recommended Starting Set**

For the **first Product Service implementation**, I recommend starting with these essentials:

### **Minimal MVP Dependencies:**
1. **spring-boot-starter-web** - REST API endpoints
2. **spring-boot-starter-data-jpa** - Database operations
3. **spring-boot-starter-validation** - Input validation
4. **postgresql** - Database driver
5. **spring-boot-starter-test** - Testing framework

### **Spring Initializr Command:**
```bash
curl https://start.spring.io/starter.zip \
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

### **Alternative: Spring Initializr Web UI**
Visit [start.spring.io](https://start.spring.io) and configure:
- **Project**: Maven
- **Language**: Java
- **Spring Boot**: 3.5.5
- **Group**: com.loiane.ecommerce
- **Artifact**: product-service
- **Name**: product-service
- **Description**: Product Service for Ecommerce Platform
- **Package name**: com.loiane.ecommerce.product
- **Packaging**: Jar
- **Java**: 24

**Dependencies to select:**
- Spring Web
- Spring Data JPA
- Validation
- PostgreSQL Driver
- Spring Boot Actuator

## ➡️ **Progressive Addition Strategy**

### **Phase 1: MVP (Week 1-2)**
```xml
<!-- Essential dependencies only -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### **Phase 2: Microservices Integration (Week 3-4)**
Add these dependencies:
```xml
<!-- Service Discovery -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- Health Monitoring -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### **Phase 3: Advanced Features (Week 5-6)**
Add these dependencies:
```xml
<!-- Database Migrations -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>

<!-- Caching -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<!-- Integration Testing -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <scope>test</scope>
</dependency>
```

## 🤔 **Why These Dependencies?**

### **Core Business Logic**
- **Spring Web**: RESTful API endpoints for product operations (CRUD, search, filtering)
- **Spring Data JPA**: Easy database operations with repository pattern, perfect for product catalog management
- **Validation**: Bean validation for product data integrity (required fields, format validation)

### **Data Management**
- **PostgreSQL**: Production-ready relational database, excellent for complex product relationships and querying
- **Flyway**: Database version control and migration management for schema evolution

### **Microservices Architecture**
- **Eureka Client**: Service discovery for inter-service communication
- **Actuator**: Health checks, metrics, and monitoring endpoints (essential for distributed systems)

### **Development & Testing**
- **Spring Boot Test**: Comprehensive testing framework with mocking and integration test support
- **H2 Database**: In-memory database for fast unit tests
- **Testcontainers**: Real database testing with Docker containers

### **Performance & Scalability**
- **Redis Cache**: Caching frequently accessed products and categories
- **Connection Pooling**: Built-in HikariCP for efficient database connections

## 📋 **Complete POM.xml Template**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.loiane.ecommerce</groupId>
    <artifactId>product-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <name>product-service</name>
    <description>Product Service for Ecommerce Platform</description>
    
    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
        <testcontainers.version>1.19.0</testcontainers.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>
        
        <!-- Spring Cloud (add when ready for microservices) -->
        <!--
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        -->
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>${testcontainers.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

## 🚀 **Next Steps**

1. **Generate the project** using Spring Initializr with the minimal MVP dependencies
2. **Set up the basic project structure** with domain-driven packages
3. **Create your first Product entity** and repository
4. **Add a simple REST controller** for basic CRUD operations
5. **Configure PostgreSQL connection** in application.yml
6. **Write your first test** to ensure everything works

Start simple with the MVP dependencies and gradually add more complexity as your understanding grows!
