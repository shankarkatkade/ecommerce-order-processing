package com.ecommerce.task;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled task for automatic order processing.
 *
 * This task runs periodically to update PENDING orders to PROCESSING status.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Component
public class OrderSchedulerTask {

  private static final Logger logger = LoggerFactory.getLogger(OrderSchedulerTask.class);

  private final OrderRepository orderRepository;

  public OrderSchedulerTask(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  /**
   * Processes pending orders every 5 minutes (300,000 milliseconds).
   *
   * Updates all PENDING orders to PROCESSING status automatically.
   * Uses pagination to handle large datasets efficiently.
   */
  @Scheduled(fixedRate = 300000) // 5 minutes
  @Transactional
  public void processPendingOrders() {
    logger.info("Starting scheduled task: Process pending orders");

    try {
      int pageSize = 50;
      int pageNumber = 0;
      long totalProcessed = 0;

      Page<Order> pendingOrders;

      do {
        // Fetch pending orders in batches
        pendingOrders = orderRepository.findAllByStatus(
          OrderStatus.PENDING,
          PageRequest.of(pageNumber, pageSize)
        );

        // Update each order status
        for (Order order : pendingOrders.getContent()) {
          order.setStatus(OrderStatus.PROCESSING);
          orderRepository.save(order);
          totalProcessed++;

          logger.debug("Order {} updated from PENDING to PROCESSING",
            order.getOrderNumber());
        }

        pageNumber++;
      } while (pendingOrders.hasNext());

      if (totalProcessed > 0) {
        logger.info("Scheduled task completed: {} orders updated from PENDING to PROCESSING",
          totalProcessed);
      } else {
        logger.debug("Scheduled task completed: No pending orders to process");
      }

    } catch (Exception e) {
      logger.error("Error occurred during scheduled order processing: ", e);
    }
  }
}