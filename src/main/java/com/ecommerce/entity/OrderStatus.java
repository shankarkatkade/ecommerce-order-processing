package com.ecommerce.entity;

/**
 * Enumeration representing the lifecycle status of an order.
 *
 * Order Status Flow:
 * PENDING -> PROCESSING -> SHIPPED -> DELIVERED
 *
 * - PENDING: Order has been created but not yet processed
 * - PROCESSING: Order is being prepared for shipment
 * - SHIPPED: Order has been dispatched for delivery
 * - DELIVERED: Order has been successfully delivered to customer
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public enum OrderStatus {
  /**
   * Order has been created and is awaiting processing.
   */
  PENDING,

  /**
   * Order is being processed and prepared for shipment.
   */
  PROCESSING,

  /**
   * Order has been shipped and is in transit.
   */
  SHIPPED,

  /**
   * Order has been successfully delivered to the customer.
   */
  DELIVERED
}