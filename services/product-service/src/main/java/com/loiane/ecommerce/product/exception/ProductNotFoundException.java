package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to find a product that does not exist.
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
