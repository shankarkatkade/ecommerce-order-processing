package com.ecommerce.exception;

import com.ecommerce.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for the application.
 *
 * Handles all exceptions and returns appropriate error responses.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Handles OrderNotFoundException.
   *
   * @param ex the exception
   * @param request the web request
   * @return Error response with HTTP 404 status
   */
  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleOrderNotFoundException(
    OrderNotFoundException ex, WebRequest request) {

    logger.error("Order not found: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      "Not Found",
      ex.getMessage()
    );

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * Handles InvalidOrderStatusException.
   *
   * @param ex the exception
   * @param request the web request
   * @return Error response with HTTP 400 status
   */
  @ExceptionHandler(InvalidOrderStatusException.class)
  public ResponseEntity<ErrorResponse> handleInvalidOrderStatusException(
    InvalidOrderStatusException ex, WebRequest request) {

    logger.error("Invalid order status: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Bad Request",
      ex.getMessage()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles ValidationException.
   *
   * @param ex the exception
   * @param request the web request
   * @return Error response with HTTP 400 status
   */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
    ValidationException ex, WebRequest request) {

    logger.error("Validation error: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation Error",
      ex.getMessage()
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles MethodArgumentNotValidException (Bean Validation errors).
   *
   * @param ex the exception
   * @param request the web request
   * @return Error response with HTTP 400 status and field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, WebRequest request) {

    logger.error("Validation failed: {}", ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation Failed",
      "Input validation failed"
    );

    List<String> details = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      details.add(error.getField() + ": " + error.getDefaultMessage());
    }
    errorResponse.setDetails(details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handles all other exceptions.
   *
   * @param ex the exception
   * @param request the web request
   * @return Error response with HTTP 500 status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
    Exception ex, WebRequest request) {

    logger.error("Unexpected error occurred: ", ex);

    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "Internal Server Error",
      "An unexpected error occurred. Please try again later."
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}