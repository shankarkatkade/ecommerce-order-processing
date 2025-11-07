package com.ecommerce.exception;

/**
 * Exception thrown when validation fails.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class ValidationException extends BusinessException {

  /**
   * Constructs a new ValidationException with the specified detail message.
   *
   * @param message the detail message
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * Constructs a new ValidationException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}