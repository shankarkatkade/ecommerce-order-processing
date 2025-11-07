package com.ecommerce.dto;

import com.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating order status.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class UpdateOrderStatusRequest {

  @NotNull(message = "Status is required")
  private OrderStatus status;

  // Constructors
  public UpdateOrderStatusRequest() {
  }

  public UpdateOrderStatusRequest(OrderStatus status) {
    this.status = status;
  }

  // Getters and Setters
  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}