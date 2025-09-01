package com.loiane.ecommerce.product.controller;

import com.loiane.ecommerce.product.dto.category.*;
import com.loiane.ecommerce.product.exception.CategoryNotFoundException;
import com.loiane.ecommerce.product.exception.DuplicateSlugException;
import com.loiane.ecommerce.product.mapper.CategoryMapper;
import com.loiane.ecommerce.product.repository.CategoryRepository;
import com.loiane.ecommerce.product.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        var categories = categoryRepository.findAll(); // Use repository directly since service method returns hierarchy
        var response = categoryMapper.toResponseList(categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        try {
            var category = categoryService.findBySlug(slug);
            var response = categoryMapper.toResponse(category);
            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        try {
            var entity = categoryMapper.toEntity(request);
            var savedEntity = categoryService.createCategory(entity);
            var response = categoryMapper.toResponse(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateSlugException _) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id, 
            @Valid @RequestBody UpdateCategoryRequest request) {
        
        try {
            var existingCategory = categoryService.findById(id);
            categoryMapper.updateEntity(existingCategory, request);
            var updatedEntity = categoryService.updateCategory(id, existingCategory);
            var response = categoryMapper.toResponse(updatedEntity);
            return ResponseEntity.ok(response);
        } catch (CategoryNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        try {
            var category = categoryService.findById(id);
            // Check if category has active products
            long productCount = categoryService.countActiveProductsInCategory(id);
            if (productCount > 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            
            categoryRepository.delete(category); // Direct repository call for deletion
            return ResponseEntity.noContent().build();
        } catch (CategoryNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }
}
