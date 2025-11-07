package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for E-Commerce Order Processing System.
 * <p>
 * This is the entry point of the Spring Boot application. It enables:
 * - Component scanning for all packages under com.ecommerce
 * - Auto-configuration of Spring Boot features
 * - Scheduled task execution for background jobs
 * - Asynchronous method execution
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ECommerceOrderProcessingSystemApplication {

  /**
   * Main method to start the Spring Boot application.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(ECommerceOrderProcessingSystemApplication.class, args);
  }

}
