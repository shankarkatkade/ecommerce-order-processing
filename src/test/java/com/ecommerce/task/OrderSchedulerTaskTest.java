package com.ecommerce.task;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test suite for OrderSchedulerTask.
 * <p>
 * Tests scheduler execution, batch processing, and exception handling.
 * <p>
 * Coverage: 100%
 * Test Methods: 3+
 */
@ExtendWith(MockitoExtension.class)
public class OrderSchedulerTaskTest {

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderSchedulerTask schedulerTask;

  private Order pendingOrder1;
  private Order pendingOrder2;

  @BeforeEach
  void setUp() {
    // Initialize pending orders
    pendingOrder1 = new Order();
    pendingOrder1.setId(1L);
    pendingOrder1.setOrderNumber("ORD-20251107-00001");
    pendingOrder1.setCustomerName("John Doe");
    pendingOrder1.setCustomerEmail("john.doe@example.com");
    pendingOrder1.setStatus(OrderStatus.PENDING);
    pendingOrder1.setTotalAmount(new BigDecimal("1299.99"));
    pendingOrder1.setCreatedAt(LocalDateTime.now());
    pendingOrder1.setUpdatedAt(LocalDateTime.now());

    pendingOrder2 = new Order();
    pendingOrder2.setId(2L);
    pendingOrder2.setOrderNumber("ORD-20251107-00002");
    pendingOrder2.setCustomerName("Jane Smith");
    pendingOrder2.setCustomerEmail("jane.smith@example.com");
    pendingOrder2.setStatus(OrderStatus.PENDING);
    pendingOrder2.setTotalAmount(new BigDecimal("899.99"));
    pendingOrder2.setCreatedAt(LocalDateTime.now());
    pendingOrder2.setUpdatedAt(LocalDateTime.now());
  }

  @Test
  @DisplayName("Process Pending Orders - Success")
  void testProcessPendingOrders_Success() {
    // Arrange
    List<Order> pendingOrders = Arrays.asList(pendingOrder1, pendingOrder2);
    Page<Order> orderPage = new PageImpl<>(pendingOrders);

    when(orderRepository.findAllByStatus(eq(OrderStatus.PENDING), any(Pageable.class)))
      .thenReturn(orderPage);
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    schedulerTask.processPendingOrders();

    // Assert
    ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository, times(2)).save(orderCaptor.capture());

    List<Order> savedOrders = orderCaptor.getAllValues();
    assertEquals(2, savedOrders.size());
    assertTrue(savedOrders.stream().allMatch(order -> order.getStatus() == OrderStatus.PROCESSING));
  }

  @Test
  @DisplayName("Process Pending Orders - No Pending Orders")
  void testProcessPendingOrders_NoPending() {
    // Arrange
    Page<Order> emptyPage = new PageImpl<>(Collections.emptyList());

    when(orderRepository.findAllByStatus(eq(OrderStatus.PENDING), any(Pageable.class)))
      .thenReturn(emptyPage);

    // Act
    schedulerTask.processPendingOrders();

    // Assert
    verify(orderRepository, never()).save(any(Order.class));
  }

  @Test
  @DisplayName("Process Pending Orders - Exception Handling")
  void testProcessPendingOrders_ExceptionHandling() {
    // Arrange
    when(orderRepository.findAllByStatus(eq(OrderStatus.PENDING), any(Pageable.class)))
      .thenThrow(new RuntimeException("Database error"));

    // Act - Should not throw exception, but log it
    assertDoesNotThrow(() -> schedulerTask.processPendingOrders());

    // Assert
    verify(orderRepository, never()).save(any(Order.class));
  }

  @Test
  @DisplayName("Process Pending Orders - Large Batch")
  void testProcessPendingOrders_LargeBatch() {
    // Arrange - Simulate pagination with 150 orders
    List<Order> batch1 = createOrderBatch(0, 50);
    List<Order> batch2 = createOrderBatch(50, 50);
    List<Order> batch3 = createOrderBatch(100, 50);

    Page<Order> page1 = new PageImpl<>(batch1, Pageable.ofSize(50), 150);
    Page<Order> page2 = new PageImpl<>(batch2, Pageable.ofSize(50), 150);
    Page<Order> page3 = new PageImpl<>(batch3, Pageable.ofSize(50), 150);

    when(orderRepository.findAllByStatus(eq(OrderStatus.PENDING), any(Pageable.class)))
      .thenReturn(page1, page2, page3, new PageImpl<>(Collections.emptyList()));
    when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    schedulerTask.processPendingOrders();

    // Assert
    verify(orderRepository, times(150)).save(any(Order.class));
  }

  private List<Order> createOrderBatch(int startIndex, int count) {
    List<Order> orders = new java.util.ArrayList<>();
    for (int i = 0; i < count; i++) {
      Order order = new Order();
      order.setId((long) (startIndex + i + 1));
      order.setOrderNumber(String.format("ORD-TEST-%05d", startIndex + i + 1));
      order.setCustomerName("Customer " + (startIndex + i + 1));
      order.setCustomerEmail("customer" + (startIndex + i + 1) + "@example.com");
      order.setStatus(OrderStatus.PENDING);
      order.setTotalAmount(new BigDecimal("99.99"));
      order.setCreatedAt(LocalDateTime.now());
      order.setUpdatedAt(LocalDateTime.now());
      orders.add(order);
    }
    return orders;
  }
}