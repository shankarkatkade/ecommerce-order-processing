package com.ecommerce.service;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.exception.InvalidOrderStatusException;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.exception.ValidationException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.util.OrderNumberGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService interface.
 *
 * Provides business logic for order management with transaction support,
 * validation, and comprehensive logging.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;

  public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
    this.orderRepository = orderRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public OrderDTO createOrder(CreateOrderRequest request) {
    logger.debug("Creating order for customer: {}", request.getCustomerName());

    // Validate request
    validateCreateOrderRequest(request);

    // Generate unique order number
    String orderNumber = OrderNumberGenerator.generateOrderNumber();
    logger.debug("Generated order number: {}", orderNumber);

    // Calculate total amount
    BigDecimal totalAmount = calculateTotalAmount(request.getOrderItems());

    // Create order entity
    Order order = new Order(
      orderNumber,
      request.getCustomerName(),
      request.getCustomerEmail(),
      OrderStatus.PENDING,
      totalAmount
    );

    // Create and add order items
    for (CreateOrderRequest.OrderItemRequest itemRequest : request.getOrderItems()) {
      OrderItem item = new OrderItem(
        itemRequest.getProductId(),
        itemRequest.getProductName(),
        itemRequest.getQuantity(),
        itemRequest.getPrice()
      );
      order.addOrderItem(item);
    }

    // Save order
    Order savedOrder = orderRepository.save(order);
    logger.info("Order created successfully: {} for customer: {}",
      savedOrder.getOrderNumber(), savedOrder.getCustomerName());

    return convertToDTO(savedOrder);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDTO getOrderById(Long orderId) {
    logger.debug("Fetching order by ID: {}", orderId);

    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> {
        logger.error("Order not found with ID: {}", orderId);
        return new OrderNotFoundException("Order not found with id: " + orderId);
      });

    logger.debug("Order found: {}", order.getOrderNumber());
    return convertToDTO(order);
  }

  @Override
  @Transactional(readOnly = true)
  public OrderDTO getOrderByOrderNumber(String orderNumber) {
    logger.debug("Fetching order by order number: {}", orderNumber);

    Order order = orderRepository.findByOrderNumber(orderNumber)
      .orElseThrow(() -> {
        logger.error("Order not found with order number: {}", orderNumber);
        return new OrderNotFoundException("Order not found with order number: " + orderNumber);
      });

    logger.debug("Order found: {}", order.getOrderNumber());
    return convertToDTO(order);
  }

  @Override
  public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
    logger.debug("Updating order status for order ID: {} to {}", orderId, newStatus);

    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> {
        logger.error("Order not found with ID: {}", orderId);
        return new OrderNotFoundException("Order not found with id: " + orderId);
      });

    OrderStatus currentStatus = order.getStatus();

    // Validate status transition
    validateStatusTransition(currentStatus, newStatus);

    order.setStatus(newStatus);
    orderRepository.save(order);

    logger.info("Order {} status updated from {} to {}",
      order.getOrderNumber(), currentStatus, newStatus);
  }

  @Override
  public void cancelOrder(Long orderId) {
    logger.debug("Cancelling order with ID: {}", orderId);

    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> {
        logger.error("Order not found with ID: {}", orderId);
        return new OrderNotFoundException("Order not found with id: " + orderId);
      });

    // Only PENDING orders can be cancelled
    if (order.getStatus() != OrderStatus.PENDING) {
      logger.error("Cannot cancel order {} with status: {}",
        order.getOrderNumber(), order.getStatus());
      throw new InvalidOrderStatusException(
        "Cannot cancel order. Only PENDING orders can be cancelled. Current status: " +
          order.getStatus()
      );
    }

    orderRepository.delete(order);
    logger.info("Order {} cancelled successfully", order.getOrderNumber());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<OrderDTO> getAllOrders(Pageable pageable) {
    logger.debug("Fetching all orders with pagination: page={}, size={}",
      pageable.getPageNumber(), pageable.getPageSize());

    Page<Order> orders = orderRepository.findAll(pageable);
    logger.debug("Found {} orders", orders.getTotalElements());

    return orders.map(this::convertToDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<OrderDTO> getAllOrdersByStatus(OrderStatus status, Pageable pageable) {
    logger.debug("Fetching orders with status: {} and pagination: page={}, size={}",
      status, pageable.getPageNumber(), pageable.getPageSize());

    Page<Order> orders = orderRepository.findAllByStatus(status, pageable);
    logger.debug("Found {} orders with status: {}", orders.getTotalElements(), status);

    return orders.map(this::convertToDTO);
  }

  /**
   * Validates the order creation request.
   */
  private void validateCreateOrderRequest(CreateOrderRequest request) {
    if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
      throw new ValidationException("Order must contain at least one item");
    }

    for (CreateOrderRequest.OrderItemRequest item : request.getOrderItems()) {
      if (item.getQuantity() <= 0) {
        throw new ValidationException("Item quantity must be greater than 0");
      }
      if (item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ValidationException("Item price must be greater than 0");
      }
    }
  }

  /**
   * Calculates total amount from order items.
   */
  private BigDecimal calculateTotalAmount(List<CreateOrderRequest.OrderItemRequest> items) {
    return items.stream()
      .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
      .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Validates order status transition.
   */
  private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
    boolean isValidTransition = switch (currentStatus) {
      case PENDING -> newStatus == OrderStatus.PROCESSING;
      case PROCESSING -> newStatus == OrderStatus.SHIPPED;
      case SHIPPED -> newStatus == OrderStatus.DELIVERED;
      case DELIVERED -> false;
    };

    if (!isValidTransition) {
      throw new InvalidOrderStatusException(
        String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
      );
    }
  }

  /**
   * Converts Order entity to OrderDTO.
   */
  private OrderDTO convertToDTO(Order order) {
    OrderDTO dto = modelMapper.map(order, OrderDTO.class);

    // Map order items
    List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
      .map(item -> modelMapper.map(item, OrderItemDTO.class))
      .collect(Collectors.toList());
    dto.setOrderItems(itemDTOs);

    return dto;
  }
}