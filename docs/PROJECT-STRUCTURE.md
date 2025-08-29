# Project Structure - Monorepo Organization

## 🏗️ **Recommended Monorepo Structure**

```
ecommerce-spring-angular-microservices/
├── README.md- **Shared Resources**
- Common utilities managed within each service
- Centralized documentation in `docs/`
- Shared development scripts
- Common Docker Compose for local developmentROADMAP.md
├── GETTING-STARTED.md
├── PROJECT-STRUCTURE.md
├── PRODUCT-SERVICE-FEATURES.md
├── docker-compose.yml                    # Development environment
├── docker-compose.prod.yml               # Production environment
├── .gitignore
├── .github/
│   └── workflows/
│       ├── ci-backend.yml
│       ├── ci-frontend.yml
│       └── deploy.yml
│
├── docs/                                 # Shared documentation
│   ├── api/                             # API documentation
│   ├── architecture/                    # System architecture
│   └── deployment/                      # Deployment guides
│
├── scripts/                             # Utility scripts
│   ├── build-all.sh
│   ├── start-dev.sh
│   ├── run-tests.sh
│   └── database/
│       ├── init.sql
│       └── migrations/
│
├── infrastructure/                      # Infrastructure as code
│   ├── eureka-server/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/main/
│   │       ├── java/com/ecommerce/eureka/
│   │       └── resources/
│   │           └── application.yml
│   │
│   ├── api-gateway/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/main/
│   │       ├── java/com/ecommerce/gateway/
│   │       └── resources/
│   │
│   └── config-server/                   # Optional: centralized config
│       ├── pom.xml
│       ├── Dockerfile
│       └── src/main/
│
├── services/                           # Business microservices
│   ├── user-service/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   ├── README.md
│   │   └── src/
│   │       ├── main/
│   │       │   ├── java/com/ecommerce/user/
│   │       │   │   ├── UserServiceApplication.java
│   │       │   │   ├── controller/
│   │       │   │   ├── service/
│   │       │   │   ├── repository/
│   │       │   │   ├── entity/
│   │       │   │   ├── dto/
│   │       │   │   └── config/
│   │       │   └── resources/
│   │       │       ├── application.yml
│   │       │       └── db/migration/    # Flyway migrations
│   │       └── test/
│   │           ├── java/com/ecommerce/user/
│   │           └── resources/
│   │
│   ├── product-service/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   ├── README.md
│   │   └── src/
│   │       ├── main/
│   │       │   ├── java/com/ecommerce/product/
│   │       │   │   ├── ProductServiceApplication.java
│   │       │   │   ├── controller/
│   │       │   │   ├── service/
│   │       │   │   ├── repository/
│   │       │   │   ├── entity/
│   │       │   │   ├── dto/
│   │       │   │   └── config/
│   │       │   └── resources/
│   │       └── test/
│   │
│   ├── order-service/
│   │   └── [similar structure]
│   │
│   ├── payment-service/
│   │   └── [similar structure]
│   │
│   └── notification-service/
│       └── [similar structure]
│
├── frontend/                           # Angular application
│   ├── package.json
│   ├── angular.json
│   ├── tsconfig.json
│   ├── Dockerfile
│   ├── README.md
│   └── src/
│       ├── app/
│       │   ├── core/                   # Singletons, guards, interceptors
│       │   │   ├── services/
│       │   │   ├── guards/
│       │   │   ├── interceptors/
│       │   │   └── models/
│       │   ├── shared/                 # Shared components, pipes, directives
│       │   │   ├── components/
│       │   │   ├── pipes/
│       │   │   └── directives/
│       │   ├── features/               # Feature modules
│       │   │   ├── auth/
│       │   │   ├── products/
│       │   │   ├── orders/
│       │   │   └── profile/
│       │   └── layout/                 # App shell components
│       ├── assets/
│       └── environments/
│
└── pom.xml                            # Parent POM for all Java projects
```

## 🎯 **Structure Benefits**

### **1. Clear Separation of Concerns**
- **`infrastructure/`**: Service discovery, gateway, configuration
- **`services/`**: Business logic microservices
- **`frontend/`**: Client application
- **`scripts/`**: Development and deployment automation

### **2. Independent Development**
- Each service can be developed independently
- Separate Docker containers for each service
- Individual CI/CD pipelines possible
- Team ownership of specific directories

### **3. Shared Resources**
- Common utilities in `shared/` folder
- Centralized documentation in `docs/`
- Shared development scripts
- Common Docker Compose for local development

## 📦 **Parent POM Configuration**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.ecommerce</groupId>
    <artifactId>ecommerce-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    
    <name>Ecommerce Microservices Parent</name>
    <description>Parent POM for ecommerce microservices project</description>
    
    <modules>
        <module>infrastructure/eureka-server</module>
        <module>infrastructure/api-gateway</module>
        <module>infrastructure/config-server</module>
        <module>services/user-service</module>
        <module>services/product-service</module>
        <module>services/order-service</module>
        <module>services/payment-service</module>
        <module>services/notification-service</module>
    </modules>
    
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.0</spring-boot.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    
    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

## 🐳 **Docker Compose Strategy**

### **Development Environment**
```yaml
# docker-compose.yml
version: '3.8'
services:
  # Infrastructure
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: ecommerce_dev
      POSTGRES_USER: ecommerce
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  # Service Discovery
  eureka-server:
    build: ./infrastructure/eureka-server
    ports:
      - "8761:8761"
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  # API Gateway
  api-gateway:
    build: ./infrastructure/api-gateway
    ports:
      - "8080:8080"
    depends_on:
      - eureka-server
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8761/eureka

  # Business Services
  user-service:
    build: ./services/user-service
    ports:
      - "8081:8081"
    depends_on:
      - postgres
      - eureka-server
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  product-service:
    build: ./services/product-service
    ports:
      - "8082:8082"
    depends_on:
      - postgres
      - eureka-server
    environment:
      - SPRING_PROFILES_ACTIVE=docker

  # Frontend
  frontend:
    build: ./frontend
    ports:
      - "4200:4200"
    volumes:
      - ./frontend:/app
      - /app/node_modules
    command: ng serve --host 0.0.0.0

volumes:
  postgres_data:
```

## 🚀 **Development Workflow**

### **1. Build Everything**
```bash
# Build all services
./scripts/build-all.sh

# Or build individually
mvn clean install                    # All Java services
cd frontend && npm install         # Angular app
```

### **2. Start Development Environment**
```bash
# Start infrastructure services
docker-compose up postgres redis eureka-server

# Start individual services in development mode
cd services/user-service && mvn spring-boot:run
cd services/product-service && mvn spring-boot:run

# Start frontend
cd frontend && ng serve
```

### **3. Testing Strategy**
```bash
# Test all services
mvn test                            # All Java services
cd frontend && npm test            # Angular tests

# Integration tests
./scripts/run-integration-tests.sh
```

## 🔧 **IDE Configuration**

### **IntelliJ IDEA**
- Import as Maven project (root pom.xml)
- Each service appears as separate module
- Shared run configurations in `.idea/runConfigurations/`
- Code style and inspection profiles shared

### **VS Code**
- Workspace configuration in `.vscode/`
- Extensions recommendations for Java and Angular
- Shared debug configurations
- Multi-root workspace setup