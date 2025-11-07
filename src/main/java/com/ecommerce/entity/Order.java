package com.ecommerce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity class representing an Order in the e-commerce system.
 *
 * This class maps to the 'orders' table in the database and contains
 * all information about a customer's order including items, status, and timestamps.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Entity
@Table(name = "orders", indexes = {
  @Index(name = "idx_order_number", columnList = "order_number", unique = true),
  @Index(name = "idx_status", columnList = "status"),
  @Index(name = "idx_created_at", columnList = "created_at")
})
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_number", nullable = false, unique = true, length = 50)
  private String orderNumber;

  @Column(name = "customer_name", nullable = false, length = 100)
  private String customerName;

  @Column(name = "customer_email", nullable = false, length = 100)
  private String customerEmail;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private OrderStatus status;

  @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalAmount;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  /**
   * Default constructor.
   */
  public Order() {
  }

  /**
   * Constructor with essential fields.
   */
  public Order(String orderNumber, String customerName, String customerEmail, OrderStatus status, BigDecimal totalAmount) {
    this.orderNumber = orderNumber;
    this.customerName = customerName;
    this.customerEmail = customerEmail;
    this.status = status;
    this.totalAmount = totalAmount;
  }

  /**
   * JPA callback method executed before persisting the entity.
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * JPA callback method executed before updating the entity.
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  /**
   * Helper method to add an order item to this order.
   * Maintains bidirectional relationship.
   */
  public void addOrderItem(OrderItem item) {
    orderItems.add(item);
    item.setOrder(this);
  }

  /**
   * Helper method to remove an order item from this order.
   * Maintains bidirectional relationship.
   */
  public void removeOrderItem(OrderItem item) {
    orderItems.remove(item);
    item.setOrder(null);
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

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

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Order)) return false;
    Order order = (Order) o;
    return Objects.equals(id, order.id) && Objects.equals(orderNumber, order.orderNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, orderNumber);
  }

  @Override
  public String toString() {
    return "Order{" +
      "id=" + id +
      ", orderNumber='" + orderNumber + '\'' +
      ", customerName='" + customerName + '\'' +
      ", status=" + status +
      ", totalAmount=" + totalAmount +
      ", createdAt=" + createdAt +
      '}';
  }
}