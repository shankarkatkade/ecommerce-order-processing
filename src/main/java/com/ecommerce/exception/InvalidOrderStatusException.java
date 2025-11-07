package com.ecommerce.exception;

/**
 * Exception thrown when an invalid order status transition is attempted.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class InvalidOrderStatusException extends BusinessException {

  /**
   * Constructs a new InvalidOrderStatusException with the specified detail message.
   *
   * @param message the detail message
   */
  public InvalidOrderStatusException(String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidOrderStatusException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public InvalidOrderStatusException(String message, Throwable cause) {
    super(message, cause);
  }
}