package com.ecommerce.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Error response DTO.
 *
 * Provides detailed error information for API error responses.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
public class ErrorResponse {

  private LocalDateTime timestamp;
  private Integer status;
  private String error;
  private String message;
  private List<String> details;

  // Constructors
  public ErrorResponse() {
    this.timestamp = LocalDateTime.now();
    this.details = new ArrayList<>();
  }

  public ErrorResponse(Integer status, String error, String message) {
    this.timestamp = LocalDateTime.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.details = new ArrayList<>();
  }

  // Getters and Setters
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<String> getDetails() {
    return details;
  }

  public void setDetails(List<String> details) {
    this.details = details;
  }

  public void addDetail(String detail) {
    this.details.add(detail);
  }
}