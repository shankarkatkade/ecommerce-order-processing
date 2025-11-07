package com.ecommerce.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating unique order numbers.
 *
 * Format: ORD-YYYYMMDD-XXXXX
 * Example: ORD-20251107-00001
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class OrderNumberGenerator {

  private static final String PREFIX = "ORD";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final AtomicInteger counter = new AtomicInteger(0);
  private static String currentDate = "";

  /**
   * Generates a unique order number.
   *
   * Thread-safe implementation using AtomicInteger.
   * Counter resets daily.
   *
   * @return Unique order number in format ORD-YYYYMMDD-XXXXX
   */
  public static synchronized String generateOrderNumber() {
    LocalDateTime now = LocalDateTime.now();
    String dateStr = now.format(DATE_FORMATTER);

    // Reset counter if date has changed
    if (!dateStr.equals(currentDate)) {
      currentDate = dateStr;
      counter.set(0);
    }

    // Increment counter and format with leading zeros
    int orderNumber = counter.incrementAndGet();
    String formattedNumber = String.format("%05d", orderNumber);

    return String.format("%s-%s-%s", PREFIX, dateStr, formattedNumber);
  }

  /**
   * Private constructor to prevent instantiation.
   */
  private OrderNumberGenerator() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }
}