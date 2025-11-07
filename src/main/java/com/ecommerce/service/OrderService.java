package com.ecommerce.service;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Order operations.
 *
 * Defines business logic methods for order management including
 * creation, retrieval, updates, and cancellation.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public interface OrderService {

  /**
   * Creates a new order with the provided details.
   *
   * @param request Order creation request containing customer and item details
   * @return Created order DTO with generated order number and ID
   * @throws com.ecommerce.exception.ValidationException if request validation fails
   */
  OrderDTO createOrder(CreateOrderRequest request);

  /**
   * Retrieves an order by its unique ID.
   *
   * @param orderId Order ID
   * @return Order DTO containing all order details
   * @throws com.ecommerce.exception.OrderNotFoundException if order not found
   */
  OrderDTO getOrderById(Long orderId);

  /**
   * Retrieves an order by its unique order number.
   *
   * @param orderNumber Unique order number
   * @return Order DTO containing all order details
   * @throws com.ecommerce.exception.OrderNotFoundException if order not found
   */
  OrderDTO getOrderByOrderNumber(String orderNumber);

  /**
   * Updates the status of an existing order.
   *
   * @param orderId Order ID
   * @param status New order status
   * @throws com.ecommerce.exception.OrderNotFoundException if order not found
   * @throws com.ecommerce.exception.InvalidOrderStatusException if status transition is invalid
   */
  void updateOrderStatus(Long orderId, OrderStatus status);

  /**
   * Cancels an order. Only PENDING orders can be cancelled.
   *
   * @param orderId Order ID
   * @throws com.ecommerce.exception.OrderNotFoundException if order not found
   * @throws com.ecommerce.exception.InvalidOrderStatusException if order cannot be cancelled
   */
  void cancelOrder(Long orderId);

  /**
   * Retrieves all orders with pagination.
   *
   * @param pageable Pagination parameters
   * @return Page of order DTOs
   */
  Page<OrderDTO> getAllOrders(Pageable pageable);

  /**
   * Retrieves orders filtered by status with pagination.
   *
   * @param status Order status filter
   * @param pageable Pagination parameters
   * @return Page of order DTOs matching the status
   */
  Page<OrderDTO> getAllOrdersByStatus(OrderStatus status, Pageable pageable);
}