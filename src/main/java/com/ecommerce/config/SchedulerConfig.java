package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Scheduler configuration class.
 *
 * This class configures the thread pool for scheduled tasks in the application.
 * It ensures that background jobs run efficiently without blocking the main
 * application threads.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Configuration
public class SchedulerConfig {

  /**
   * Configures ThreadPoolTaskScheduler for executing scheduled tasks.
   *
   * The scheduler is configured with:
   * - Pool size of 5 threads to handle multiple scheduled tasks concurrently
   * - Named threads for easier debugging and monitoring
   * - Graceful shutdown with task completion wait
   *
   * @return Configured ThreadPoolTaskScheduler
   */
  @Bean
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5);
    scheduler.setThreadNamePrefix("order-scheduler-");
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    scheduler.setAwaitTerminationSeconds(20);
    scheduler.initialize();
    return scheduler;
  }
}