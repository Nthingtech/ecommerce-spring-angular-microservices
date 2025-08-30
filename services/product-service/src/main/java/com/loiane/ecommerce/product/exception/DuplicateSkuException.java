package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to create a product with a SKU that already exists.
 */
public class DuplicateSkuException extends RuntimeException {
    
    public DuplicateSkuException(String message) {
        super(message);
    }
    
    public DuplicateSkuException(String message, Throwable cause) {
        super(message, cause);
    }
}
