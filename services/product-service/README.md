# Product Service

This is the Product Service for the E-commerce Microservices platform. It handles product and category management.

## Environment Configuration

This service uses environment variables for sensitive configuration. To set up your development environment:

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

2. Update the `.env` file with your actual database credentials:
   ```bash
   # Database Configuration
   DB_URL=jdbc:postgresql://localhost:5432/ecommerce_product
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   ```

3. The application will automatically load these environment variables at startup.

## Required Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_URL` | Database connection URL | `jdbc:postgresql://localhost:5432/ecommerce_product` |
| `DB_USERNAME` | Database username | `ecommerce` |
| `DB_PASSWORD` | Database password | `password123` |

## Running the Application

### Prerequisites
- Java 24
- PostgreSQL database
- Maven 3.6+

### Local Development
1. Ensure PostgreSQL is running
2. Create the database: `ecommerce_product`
3. Configure your `.env` file (see above)
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Using Docker Compose (Recommended for Development)
```bash
# Start the database and service
docker-compose up -d

# View logs
docker-compose logs -f product-service

# Stop services
docker-compose down
```

### Using Docker
```bash
# Build the application
./mvnw clean package

# Run with Docker (if you have a Dockerfile)
docker run -p 8082:8082 --env-file .env product-service
```

## API Documentation

The service exposes REST APIs for:
- Product management (CRUD operations)
- Category management (CRUD operations)

API documentation is available at: `http://localhost:8082/actuator/health`

## Testing

Run tests with:
```bash
./mvnw test
```

The tests use an in-memory H2 database and do not require the PostgreSQL setup.

## Security Notes

- Never commit the `.env` file to version control
- Use different environment variables for different environments (dev, staging, prod)
- Consider using a secrets management system for production deployments
