package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to create a category with a slug that already exists.
 */
public class DuplicateSlugException extends RuntimeException {
    
    public DuplicateSlugException(String message) {
        super(message);
    }
    
    public DuplicateSlugException(String message, Throwable cause) {
        super(message, cause);
    }
}
