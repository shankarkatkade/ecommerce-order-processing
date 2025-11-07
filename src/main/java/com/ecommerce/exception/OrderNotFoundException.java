package com.ecommerce.exception;

/**
 * Exception thrown when an order is not found.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class OrderNotFoundException extends BusinessException {

  /**
   * Constructs a new OrderNotFoundException with the specified detail message.
   *
   * @param message the detail message
   */
  public OrderNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new OrderNotFoundException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public OrderNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}