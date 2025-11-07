package com.ecommerce.exception;

/**
 * Base exception class for business logic errors.
 *
 * All custom business exceptions should extend this class.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class BusinessException extends RuntimeException {

  /**
   * Constructs a new business exception with the specified detail message.
   *
   * @param message the detail message
   */
  public BusinessException(String message) {
    super(message);
  }

  /**
   * Constructs a new business exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }
}