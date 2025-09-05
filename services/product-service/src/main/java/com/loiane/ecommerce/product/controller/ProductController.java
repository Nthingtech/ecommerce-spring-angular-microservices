package com.loiane.ecommerce.product.controller;

import com.loiane.ecommerce.product.dto.product.*;
import com.loiane.ecommerce.product.exception.*;
import com.loiane.ecommerce.product.mapper.ProductMapper;
import com.loiane.ecommerce.product.repository.CategoryRepository;
import com.loiane.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductService productService, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable String id) {
        try {
            var product = productService.findById(id);
            var response = productMapper.toResponse(product);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findActiveProducts(
            @PageableDefault(size = 20) Pageable pageable) {
        var products = productService.findActiveProducts(pageable);
        var response = products.map(productMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchActiveProducts(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {
        var products = productService.searchActiveProducts(q, pageable);
        var response = products.map(productMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> findLowStockProducts() {
        var products = productService.findLowStockProducts();
        var response = productMapper.toResponseList(products);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        try {
            var entity = productMapper.toEntity(request);
            
            // Set category from repository
            var category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            entity.setCategory(category);
            
            var savedEntity = productService.createProduct(entity);
            var response = productMapper.toResponse(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateSkuException _) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (InactiveCategoryException _) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalArgumentException _) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductRequest request) {
        try {
            var existingProduct = productService.findById(id);
            productMapper.updateEntity(existingProduct, request);
            var updatedEntity = productService.updateProduct(id, existingProduct);
            var response = productMapper.toResponse(updatedEntity);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateSkuException _) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<ProductResponse> publishProduct(@PathVariable String id) {
        try {
            var updatedProduct = productService.publishProduct(id);
            var response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        } catch (IllegalOperationException _) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/discontinue")
    public ResponseEntity<ProductResponse> discontinueProduct(@PathVariable String id) {
        try {
            var updatedProduct = productService.discontinueProduct(id);
            var response = productMapper.toResponse(updatedProduct);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/stock/reserve")
    public ResponseEntity<Void> reserveStock(
            @PathVariable String id,
            @RequestParam int quantity) {
        try {
            productService.reserveStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientStockException _) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException _) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock/release")
    public ResponseEntity<Void> releaseStock(
            @PathVariable String id,
            @RequestParam int quantity) {
        try {
            productService.releaseStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException _) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock/confirm")
    public ResponseEntity<Void> confirmStock(
            @PathVariable String id,
            @RequestParam int quantity) {
        try {
            productService.confirmStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (ProductNotFoundException _) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException _) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/bulk/status")
    public ResponseEntity<Void> bulkUpdateStatus(@Valid @RequestBody BulkUpdateStatusRequest request) {
        try {
            int updatedCount = productService.bulkUpdateStatus(request.productIds(), request.status());
            if (updatedCount > 0) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception _) {
            return ResponseEntity.badRequest().build();
        }
    }
}
