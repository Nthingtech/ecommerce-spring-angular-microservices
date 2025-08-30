package com.loiane.ecommerce.product.entity;

/**
 * Enumeration representing the status of a product in the catalog.
 */
public enum ProductStatus {
    /**
     * Product is active and available for sale
     */
    ACTIVE,
    
    /**
     * Product is inactive/hidden from catalog but not removed
     */
    INACTIVE,
    
    /**
     * Product is discontinued and no longer available
     */
    DISCONTINUED
}
