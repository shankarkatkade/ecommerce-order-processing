package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UpdateOrderStatusRequest;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order operations.
 *
 * Provides endpoints for creating, retrieving, updating, and cancelling orders.
 * All endpoints return standardized ApiResponse wrapper.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order Management", description = "APIs for managing e-commerce orders")
public class OrderController {

  private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Creates a new order.
   *
   * @param request Order creation request
   * @return Created order with HTTP 201 status
   */
  @PostMapping
  @Operation(summary = "Create a new order", description = "Creates a new order with items")
  public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
    logger.info("Received request to create order for customer: {}", request.getCustomerName());

    OrderDTO orderDTO = orderService.createOrder(request);
    ApiResponse<OrderDTO> response = ApiResponse.success("Order created successfully", orderDTO);

    logger.info("Order created successfully: {}", orderDTO.getOrderNumber());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  /**
   * Retrieves an order by ID.
   *
   * @param id Order ID
   * @return Order details with HTTP 200 status
   */
  @GetMapping("/{id}")
  @Operation(summary = "Get order by ID", description = "Retrieves order details by ID")
  public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long id) {
    logger.info("Received request to fetch order by ID: {}", id);

    OrderDTO orderDTO = orderService.getOrderById(id);
    ApiResponse<OrderDTO> response = ApiResponse.success("Order retrieved successfully", orderDTO);

    logger.debug("Order retrieved: {}", orderDTO.getOrderNumber());
    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves all orders with optional status filter and pagination.
   *
   * @param status Optional status filter
   * @param pageable Pagination parameters
   * @return Page of orders with HTTP 200 status
   */
  @GetMapping
  @Operation(summary = "Get all orders", description = "Retrieves all orders with pagination and optional status filter")
  public ResponseEntity<ApiResponse<Page<OrderDTO>>> getAllOrders(
    @RequestParam(required = false) OrderStatus status,
    @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

    logger.info("Received request to fetch orders - status: {}, page: {}, size: {}",
      status, pageable.getPageNumber(), pageable.getPageSize());

    Page<OrderDTO> orders;
    if (status != null) {
      orders = orderService.getAllOrdersByStatus(status, pageable);
    } else {
      orders = orderService.getAllOrders(pageable);
    }

    ApiResponse<Page<OrderDTO>> response = ApiResponse.success(
      "Orders retrieved successfully", orders
    );

    logger.debug("Retrieved {} orders", orders.getTotalElements());
    return ResponseEntity.ok(response);
  }

  /**
   * Updates the status of an order.
   *
   * @param id Order ID
   * @param request Status update request
   * @return Success message with HTTP 200 status
   */
  @PatchMapping("/{id}/status")
  @Operation(summary = "Update order status", description = "Updates the status of an existing order")
  public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
    @PathVariable Long id,
    @Valid @RequestBody UpdateOrderStatusRequest request) {

    logger.info("Received request to update order {} status to: {}", id, request.getStatus());

    orderService.updateOrderStatus(id, request.getStatus());
    ApiResponse<Void> response = ApiResponse.success("Order status updated successfully", null);

    logger.info("Order {} status updated to: {}", id, request.getStatus());
    return ResponseEntity.ok(response);
  }

  /**
   * Cancels an order.
   *
   * @param id Order ID
   * @return Success message with HTTP 200 status
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Cancel order", description = "Cancels a pending order")
  public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
    logger.info("Received request to cancel order: {}", id);

    orderService.cancelOrder(id);
    ApiResponse<Void> response = ApiResponse.success("Order cancelled successfully", null);

    logger.info("Order {} cancelled successfully", id);
    return ResponseEntity.ok(response);
  }
}