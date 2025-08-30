package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to find a category that does not exist.
 */
public class CategoryNotFoundException extends RuntimeException {
    
    public CategoryNotFoundException(String message) {
        super(message);
    }
    
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
