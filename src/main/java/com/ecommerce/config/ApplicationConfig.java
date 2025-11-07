package com.ecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class.
 *
 * This class contains all application-wide bean definitions and configurations.
 * It provides beans that are used across different layers of the application.
 *
 * @author E-Commerce Development Team
 * @version 1.0.0
 * @since 2025-11-07
 */
@Configuration
public class ApplicationConfig {

  /**
   * Creates and configures ModelMapper bean for DTO to Entity mapping.
   *
   * ModelMapper is used to automatically map between DTOs and Entity objects,
   * reducing boilerplate code and maintaining clean separation of concerns.
   *
   * @return Configured ModelMapper instance
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    // Configure ModelMapper for strict matching to avoid unintended mappings
    modelMapper.getConfiguration()
      .setSkipNullEnabled(true)
      .setAmbiguityIgnored(true);
    return modelMapper;
  }
}