package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Order entity.
 *
 * Provides data access methods for Order entities with support for
 * pagination, filtering, and custom queries.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * Finds all orders with the specified status.
   *
   * @param status Order status to filter by
   * @param pageable Pagination information
   * @return Page of orders matching the status
   */
  Page<Order> findAllByStatus(OrderStatus status, Pageable pageable);

  /**
   * Finds an order by its unique order number.
   *
   * @param orderNumber Unique order number
   * @return Optional containing the order if found
   */
  Optional<Order> findByOrderNumber(String orderNumber);

  /**
   * Checks if an order exists with the given order number.
   *
   * @param orderNumber Order number to check
   * @return true if order exists, false otherwise
   */
  boolean existsByOrderNumber(String orderNumber);

  /**
   * Finds orders by customer email with pagination.
   *
   * @param customerEmail Customer email address
   * @param pageable Pagination information
   * @return Page of orders for the customer
   */
  Page<Order> findByCustomerEmail(String customerEmail, Pageable pageable);

  /**
   * Custom query to find orders with filters.
   *
   * @param status Optional status filter
   * @param customerEmail Optional customer email filter
   * @param pageable Pagination information
   * @return Page of filtered orders
   */
  @Query("SELECT o FROM Order o WHERE " +
    "(:status IS NULL OR o.status = :status) AND " +
    "(:customerEmail IS NULL OR o.customerEmail LIKE %:customerEmail%)")
  Page<Order> findOrdersWithFilters(
    @Param("status") OrderStatus status,
    @Param("customerEmail") String customerEmail,
    Pageable pageable
  );
}