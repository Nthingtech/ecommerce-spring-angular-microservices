package com.loiane.ecommerce.product.repository;

import com.loiane.ecommerce.product.entity.Category;
import com.loiane.ecommerce.product.entity.Product;
import com.loiane.ecommerce.product.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // Basic find methods
    Optional<Product> findBySku(String sku);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Existence and counting
    boolean existsBySku(String sku);

    long countByCategory(Category category);

    // Pagination support
    Page<Product> findByCategoryAndStatus(Category category, ProductStatus status, Pageable pageable);

    // Custom queries for inventory management
    @Query("SELECT p FROM Product p WHERE p.stockQuantity - p.reservedQuantity <= p.lowStockThreshold")
    List<Product> findProductsWithLowStock();

    @Query("SELECT p FROM Product p WHERE p.stockQuantity - p.reservedQuantity > 0")
    List<Product> findProductsInStock();
}
