package com.loiane.ecommerce.product.repository;

import com.loiane.ecommerce.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    // Basic find methods
    Optional<Category> findBySlug(String slug);

    List<Category> findByParentIsNull();

    List<Category> findByParent(Category parent);

    List<Category> findByLevel(int level);

    List<Category> findByIsActiveTrue();

    List<Category> findByIsActiveFalse();

    List<Category> findByNameContainingIgnoreCase(String name);

    // Sorting and filtering
    List<Category> findByParentAndIsActiveTrueOrderByDisplayOrder(Category parent);
    
    // Additional methods for service layer
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL ORDER BY c.displayOrder ASC")
    List<Category> findRootCategories();
    
    List<Category> findByParentOrderByDisplayOrderAsc(Category parent);

    // Existence and counting
    boolean existsBySlug(String slug);

    long countByParent(Category parent);

    // Custom queries for hierarchy management
    @Query("SELECT DISTINCT c FROM Category c WHERE EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
    List<Category> findCategoriesWithChildren();

    @Query("SELECT c FROM Category c WHERE NOT EXISTS (SELECT 1 FROM Category child WHERE child.parent = c)")
    List<Category> findLeafCategories();
}
