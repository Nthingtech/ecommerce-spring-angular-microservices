package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to create a product with a SKU that already exists.
 */
public class SkuAlreadyExistsException extends RuntimeException {
    
    public SkuAlreadyExistsException(String message) {
        super(message);
    }
    
    public SkuAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
