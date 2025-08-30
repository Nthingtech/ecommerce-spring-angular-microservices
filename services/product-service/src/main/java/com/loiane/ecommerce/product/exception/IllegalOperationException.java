package com.loiane.ecommerce.product.exception;

/**
 * Exception thrown when attempting to perform an illegal or invalid business operation.
 */
public class IllegalOperationException extends RuntimeException {
    
    public IllegalOperationException(String message) {
        super(message);
    }
    
    public IllegalOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
