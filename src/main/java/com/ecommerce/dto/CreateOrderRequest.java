package com.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Request DTO for creating a new order.
 *
 * Contains validation annotations to ensure data integrity.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class CreateOrderRequest {

  @NotBlank(message = "Customer name is required")
  @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
  private String customerName;

  @NotBlank(message = "Customer email is required")
  @Email(message = "Invalid email format")
  private String customerEmail;

  @NotEmpty(message = "Order must contain at least one item")
  @Valid
  private List<OrderItemRequest> orderItems = new ArrayList<>();

  // Constructors
  public CreateOrderRequest() {
  }

  // Getters and Setters
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public List<OrderItemRequest> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItemRequest> orderItems) {
    this.orderItems = orderItems;
  }

  /**
   * Inner class for order item request.
   */
  public static class OrderItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    // Constructors
    public OrderItemRequest() {
    }

    // Getters and Setters
    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public String getProductName() {
      return productName;
    }

    public void setProductName(String productName) {
      this.productName = productName;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public void setQuantity(Integer quantity) {
      this.quantity = quantity;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public void setPrice(BigDecimal price) {
      this.price = price;
    }
  }
}