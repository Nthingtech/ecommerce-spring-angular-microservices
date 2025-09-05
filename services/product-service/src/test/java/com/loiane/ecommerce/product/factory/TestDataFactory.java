package com.loiane.ecommerce.product.factory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Base factory class providing common utilities for test data generation.
 * Ensures unique values and provides common test data patterns.
 */
public abstract class TestDataFactory {
    
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Generates a unique integer for use in test data.
     * Useful for creating unique SKUs, names, etc.
     */
    protected static int nextInt() {
        return counter.incrementAndGet();
    }
    
    /**
     * Generates a unique string with a prefix.
     * Example: prefix "SKU" returns "SKU-1", "SKU-2", etc.
     */
    protected static String nextString(String prefix) {
        return prefix + "-" + nextInt();
    }
    
    /**
     * Resets the counter. Useful for test isolation.
     * Should be called in @BeforeEach if needed.
     */
    public static void resetCounter() {
        counter.set(0);
    }
}
