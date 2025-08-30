package com.loiane.ecommerce.product.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Exception Tests")
class ExceptionTests {

    @Test
    @DisplayName("Should create ProductNotFoundException with message")
    void shouldCreateProductNotFoundExceptionWithMessage() {
        // Given
        String message = "Product not found with ID: 123";

        // When
        ProductNotFoundException exception = new ProductNotFoundException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create ProductNotFoundException with message and cause")
    void shouldCreateProductNotFoundExceptionWithMessageAndCause() {
        // Given
        String message = "Product not found";
        Throwable cause = new RuntimeException("Database error");

        // When
        ProductNotFoundException exception = new ProductNotFoundException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create CategoryNotFoundException with message")
    void shouldCreateCategoryNotFoundExceptionWithMessage() {
        // Given
        String message = "Category not found with ID: 123";

        // When
        CategoryNotFoundException exception = new CategoryNotFoundException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create CategoryNotFoundException with message and cause")
    void shouldCreateCategoryNotFoundExceptionWithMessageAndCause() {
        // Given
        String message = "Category not found";
        Throwable cause = new RuntimeException("Database error");

        // When
        CategoryNotFoundException exception = new CategoryNotFoundException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create DuplicateSkuException with message")
    void shouldCreateDuplicateSkuExceptionWithMessage() {
        // Given
        String message = "Duplicate SKU: TEST-001";

        // When
        DuplicateSkuException exception = new DuplicateSkuException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create DuplicateSkuException with message and cause")
    void shouldCreateDuplicateSkuExceptionWithMessageAndCause() {
        // Given
        String message = "Duplicate SKU";
        Throwable cause = new RuntimeException("Database constraint violation");

        // When
        DuplicateSkuException exception = new DuplicateSkuException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create DuplicateSlugException with message")
    void shouldCreateDuplicateSlugExceptionWithMessage() {
        // Given
        String message = "Duplicate slug: electronics";

        // When
        DuplicateSlugException exception = new DuplicateSlugException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create DuplicateSlugException with message and cause")
    void shouldCreateDuplicateSlugExceptionWithMessageAndCause() {
        // Given
        String message = "Duplicate slug";
        Throwable cause = new RuntimeException("Database constraint violation");

        // When
        DuplicateSlugException exception = new DuplicateSlugException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create InsufficientStockException with message")
    void shouldCreateInsufficientStockExceptionWithMessage() {
        // Given
        String message = "Insufficient stock for product TEST-001";

        // When
        InsufficientStockException exception = new InsufficientStockException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create InsufficientStockException with message and cause")
    void shouldCreateInsufficientStockExceptionWithMessageAndCause() {
        // Given
        String message = "Insufficient stock";
        Throwable cause = new RuntimeException("Stock validation failed");

        // When
        InsufficientStockException exception = new InsufficientStockException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create InactiveCategoryException with message")
    void shouldCreateInactiveCategoryExceptionWithMessage() {
        // Given
        String message = "Category is inactive: electronics";

        // When
        InactiveCategoryException exception = new InactiveCategoryException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create InactiveCategoryException with message and cause")
    void shouldCreateInactiveCategoryExceptionWithMessageAndCause() {
        // Given
        String message = "Category is inactive";
        Throwable cause = new RuntimeException("Category validation failed");

        // When
        InactiveCategoryException exception = new InactiveCategoryException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("Should create IllegalOperationException with message")
    void shouldCreateIllegalOperationExceptionWithMessage() {
        // Given
        String message = "Illegal operation on product";

        // When
        IllegalOperationException exception = new IllegalOperationException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create IllegalOperationException with message and cause")
    void shouldCreateIllegalOperationExceptionWithMessageAndCause() {
        // Given
        String message = "Illegal operation";
        Throwable cause = new RuntimeException("Business rule violation");

        // When
        IllegalOperationException exception = new IllegalOperationException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
