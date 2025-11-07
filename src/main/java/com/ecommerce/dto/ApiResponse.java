package com.ecommerce.dto;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper.
 *
 * Provides a consistent response structure for all API endpoints.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 * @param <T> Type of data in the response
 */
public class ApiResponse<T> {

  private String status;
  private String message;
  private T data;
  private LocalDateTime timestamp;

  // Constructors
  public ApiResponse() {
    this.timestamp = LocalDateTime.now();
  }

  public ApiResponse(String status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.timestamp = LocalDateTime.now();
  }

  /**
   * Creates a success response.
   */
  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>("success", message, data);
  }

  /**
   * Creates an error response.
   */
  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>("error", message, null);
  }

  // Getters and Setters
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}