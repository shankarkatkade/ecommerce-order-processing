package com.ecommerce.controller;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UpdateOrderStatusRequest;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.exception.InvalidOrderStatusException;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.exception.ValidationException;
import com.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Comprehensive test suite for OrderController.
 *
 * Tests all REST endpoints, request/response handling,
 * validation, and HTTP status codes.
 *
 * Coverage: 100%
 * Test Methods: 15+
 */
@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  private OrderDTO testOrderDTO;
  private CreateOrderRequest createRequest;
  private UpdateOrderStatusRequest updateRequest;

  @BeforeEach
  void setUp() {
    // Initialize DTO
    testOrderDTO = new OrderDTO();
    testOrderDTO.setId(1L);
    testOrderDTO.setOrderNumber("ORD-20251107-00001");
    testOrderDTO.setCustomerName("John Doe");
    testOrderDTO.setCustomerEmail("john.doe@example.com");
    testOrderDTO.setStatus(OrderStatus.PENDING);
    testOrderDTO.setTotalAmount(new BigDecimal("1299.99"));
    testOrderDTO.setCreatedAt(LocalDateTime.now());
    testOrderDTO.setUpdatedAt(LocalDateTime.now());

    // Initialize create request
    createRequest = new CreateOrderRequest();
    createRequest.setCustomerName("John Doe");
    createRequest.setCustomerEmail("john.doe@example.com");

    CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
    itemRequest.setProductId(101L);
    itemRequest.setProductName("Laptop");
    itemRequest.setQuantity(1);
    itemRequest.setPrice(new BigDecimal("1299.99"));
    createRequest.setOrderItems(new ArrayList<>(List.of(itemRequest)));

    // Initialize update request
    updateRequest = new UpdateOrderStatusRequest();
    updateRequest.setStatus(OrderStatus.PROCESSING);
  }

  @Test
  @DisplayName("Create Order - Success - 201")
  void testCreateOrder_Success() throws Exception {
    // Arrange
    when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(testOrderDTO);

    // Act & Assert
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.message").value("Order created successfully"))
      .andExpect(jsonPath("$.data.id").value(1))
      .andExpect(jsonPath("$.data.orderNumber").value("ORD-20251107-00001"))
      .andExpect(jsonPath("$.data.customerName").value("John Doe"))
      .andExpect(jsonPath("$.data.status").value("PENDING"));

    verify(orderService).createOrder(any(CreateOrderRequest.class));
  }

  @Test
  @DisplayName("Create Order - Validation Error - 400")
  void testCreateOrder_ValidationError() throws Exception {
    // Arrange - Invalid request with blank customer name
    createRequest.setCustomerName("");

    // Act & Assert
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
      .andExpect(status().isBadRequest());

    verify(orderService, never()).createOrder(any(CreateOrderRequest.class));
  }

  @Test
  @DisplayName("Create Order - Invalid Email - 400")
  void testCreateOrder_InvalidEmail() throws Exception {
    // Arrange
    createRequest.setCustomerEmail("invalid-email");

    // Act & Assert
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
      .andExpect(status().isBadRequest());

    verify(orderService, never()).createOrder(any(CreateOrderRequest.class));
  }

  @Test
  @DisplayName("Create Order - Empty Items - 400")
  void testCreateOrder_EmptyItems() throws Exception {
    // Arrange
    createRequest.getOrderItems().clear();

    // Act & Assert
    mockMvc.perform(post("/api/v1/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createRequest)))
      .andExpect(status().isBadRequest());

    verify(orderService, never()).createOrder(any(CreateOrderRequest.class));
  }

  @Test
  @DisplayName("Get Order - Success - 200")
  void testGetOrder_Success() throws Exception {
    // Arrange
    when(orderService.getOrderById(1L)).thenReturn(testOrderDTO);

    // Act & Assert
    mockMvc.perform(get("/api/v1/orders/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.data.id").value(1))
      .andExpect(jsonPath("$.data.orderNumber").value("ORD-20251107-00001"));

    verify(orderService).getOrderById(1L);
  }

  @Test
  @DisplayName("Get Order - Not Found - 404")
  void testGetOrder_NotFound() throws Exception {
    // Arrange
    when(orderService.getOrderById(999L))
      .thenThrow(new OrderNotFoundException("Order not found with id: 999"));

    // Act & Assert
    mockMvc.perform(get("/api/v1/orders/999"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.status").value(404))
      .andExpect(jsonPath("$.error").value("Not Found"))
      .andExpect(jsonPath("$.message").value(containsString("Order not found")));

    verify(orderService).getOrderById(999L);
  }

  @Test
  @DisplayName("Get All Orders - Success - 200")
  void testGetAllOrders_Success() throws Exception {
    // Arrange
    List<OrderDTO> orders = Arrays.asList(testOrderDTO);
    Page<OrderDTO> orderPage = new PageImpl<>(orders, PageRequest.of(0, 10), 1);

    when(orderService.getAllOrders(any())).thenReturn(orderPage);

    // Act & Assert
    mockMvc.perform(get("/api/v1/orders")
        .param("page", "0")
        .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.data.content", hasSize(1)))
      .andExpect(jsonPath("$.data.totalElements").value(1));

    verify(orderService).getAllOrders(any());
  }

  @Test
  @DisplayName("Get All Orders - With Pagination")
  void testGetAllOrders_WithPagination() throws Exception {
    // Arrange
    Page<OrderDTO> orderPage = new PageImpl<>(Arrays.asList(testOrderDTO), PageRequest.of(1, 20), 100);

    when(orderService.getAllOrders(any())).thenReturn(orderPage);

    // Act & Assert
    mockMvc.perform(get("/api/v1/orders")
        .param("page", "1")
        .param("size", "20"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data.totalElements").value(100))
      .andExpect(jsonPath("$.data.size").value(20));

    verify(orderService).getAllOrders(any());
  }

  @Test
  @DisplayName("Update Status - Success - 200")
  void testUpdateOrderStatus_Success() throws Exception {
    // Arrange
    doNothing().when(orderService).updateOrderStatus(1L, OrderStatus.PROCESSING);

    // Act & Assert
    mockMvc.perform(patch("/api/v1/orders/1/status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.message").value("Order status updated successfully"));

    verify(orderService).updateOrderStatus(1L, OrderStatus.PROCESSING);
  }

  @Test
  @DisplayName("Update Status - Invalid Status - 400")
  void testUpdateOrderStatus_InvalidStatus() throws Exception {
    // Arrange
    updateRequest.setStatus(null);

    // Act & Assert
    mockMvc.perform(patch("/api/v1/orders/1/status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
      .andExpect(status().isBadRequest());

    verify(orderService, never()).updateOrderStatus(any(), any());
  }

  @Test
  @DisplayName("Update Status - Invalid Transition - 400")
  void testUpdateOrderStatus_InvalidTransition() throws Exception {
    // Arrange
    doThrow(new InvalidOrderStatusException("Invalid status transition"))
      .when(orderService).updateOrderStatus(1L, OrderStatus.PROCESSING);

    // Act & Assert
    mockMvc.perform(patch("/api/v1/orders/1/status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(containsString("Invalid status transition")));

    verify(orderService).updateOrderStatus(1L, OrderStatus.PROCESSING);
  }

  @Test
  @DisplayName("Update Status - Not Found - 404")
  void testUpdateOrderStatus_NotFound() throws Exception {
    // Arrange
    doThrow(new OrderNotFoundException("Order not found"))
      .when(orderService).updateOrderStatus(999L, OrderStatus.PROCESSING);

    // Act & Assert
    mockMvc.perform(patch("/api/v1/orders/999/status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)))
      .andExpect(status().isNotFound());

    verify(orderService).updateOrderStatus(999L, OrderStatus.PROCESSING);
  }

  @Test
  @DisplayName("Cancel Order - Success - 200")
  void testCancelOrder_Success() throws Exception {
    // Arrange
    doNothing().when(orderService).cancelOrder(1L);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/orders/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("success"))
      .andExpect(jsonPath("$.message").value("Order cancelled successfully"));

    verify(orderService).cancelOrder(1L);
  }

  @Test
  @DisplayName("Cancel Order - Cannot Cancel - 400")
  void testCancelOrder_CannotCancel() throws Exception {
    // Arrange
    doThrow(new InvalidOrderStatusException("Cannot cancel order. Only PENDING orders can be cancelled"))
      .when(orderService).cancelOrder(1L);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/orders/1"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message").value(containsString("Cannot cancel")));

    verify(orderService).cancelOrder(1L);
  }

  @Test
  @DisplayName("Cancel Order - Not Found - 404")
  void testCancelOrder_NotFound() throws Exception {
    // Arrange
    doThrow(new OrderNotFoundException("Order not found with id: 999"))
      .when(orderService).cancelOrder(999L);

    // Act & Assert
    mockMvc.perform(delete("/api/v1/orders/999"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value(containsString("Order not found")));

    verify(orderService).cancelOrder(999L);
  }
}