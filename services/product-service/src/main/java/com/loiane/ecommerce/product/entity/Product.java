package com.loiane.ecommerce.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 500, message = "Short description must not exceed 500 characters")
    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    @Column(unique = true, nullable = false, length = 100)
    private String sku;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Base price must have at most 10 integer digits and 2 fraction digits")
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    // Category relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Inventory fields
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Min(value = 0, message = "Low stock threshold cannot be negative")
    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 10;

    @Column(name = "track_inventory", nullable = false)
    private Boolean trackInventory = true;

    // Timestamps
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    // Constructors
    public Product() {
    }

    public Product(String name, String sku, BigDecimal basePrice) {
        this.name = name;
        this.sku = sku;
        this.basePrice = basePrice;
    }

    // Business methods
    public Integer getAvailableQuantity() {
        return stockQuantity - reservedQuantity;
    }

    public boolean isInStock() {
        return !trackInventory || getAvailableQuantity() > 0;
    }

    public boolean isLowStock() {
        return trackInventory && getAvailableQuantity() <= lowStockThreshold;
    }

    public boolean isPublished() {
        return status == ProductStatus.ACTIVE && publishedAt != null;
    }

    public void publish() {
        this.status = ProductStatus.ACTIVE;
        this.publishedAt = OffsetDateTime.now();
    }

    public void unpublish() {
        this.status = ProductStatus.INACTIVE;
        this.publishedAt = null;
    }

    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public Boolean getTrackInventory() {
        return trackInventory;
    }

    public boolean isTrackInventory() {
        return Boolean.TRUE.equals(trackInventory);
    }

    public void setTrackInventory(Boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(OffsetDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(sku, product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku);
    }

    // toString
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", basePrice=" + basePrice +
                ", status=" + status +
                ", stockQuantity=" + stockQuantity +
                '}';
    }

    // Manual Builder Pattern
    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

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

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder shortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public ProductBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public ProductBuilder basePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public ProductBuilder status(ProductStatus status) {
            this.status = status;
            return this;
        }

        public ProductBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public ProductBuilder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ProductBuilder reservedQuantity(Integer reservedQuantity) {
            this.reservedQuantity = reservedQuantity;
            return this;
        }

        public ProductBuilder lowStockThreshold(Integer lowStockThreshold) {
            this.lowStockThreshold = lowStockThreshold;
            return this;
        }

        public ProductBuilder trackInventory(Boolean trackInventory) {
            this.trackInventory = trackInventory;
            return this;
        }

        public ProductBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductBuilder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProductBuilder publishedAt(OffsetDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.name = this.name;
            product.description = this.description;
            product.shortDescription = this.shortDescription;
            product.sku = this.sku;
            product.basePrice = this.basePrice;
            product.status = this.status != null ? this.status : ProductStatus.ACTIVE;
            product.category = this.category;
            product.stockQuantity = this.stockQuantity != null ? this.stockQuantity : 0;
            product.reservedQuantity = this.reservedQuantity != null ? this.reservedQuantity : 0;
            product.lowStockThreshold = this.lowStockThreshold != null ? this.lowStockThreshold : 10;
            product.trackInventory = this.trackInventory != null ? this.trackInventory : Boolean.TRUE;
            product.createdAt = this.createdAt;
            product.updatedAt = this.updatedAt;
            product.publishedAt = this.publishedAt;
            return product;
        }
    }
}
