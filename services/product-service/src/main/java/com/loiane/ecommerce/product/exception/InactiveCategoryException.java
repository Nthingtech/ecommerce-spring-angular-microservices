package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to add a product to an inactive category.
 */
public class InactiveCategoryException extends RuntimeException {
    
    public InactiveCategoryException(String message) {
        super(message);
    }
    
    public InactiveCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
