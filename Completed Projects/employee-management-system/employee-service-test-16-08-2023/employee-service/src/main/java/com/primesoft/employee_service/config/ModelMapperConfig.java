package com.primesoft.employee_service.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.Hidden;

@Configuration
@Hidden // Exclude this class from Swagger documentation
public class ModelMapperConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelMapperConfig.class);

	/**
	 * Initializes the ModelMapper configuration.
	 *
	 * @return The configured ModelMapper instance.
	 */
	@Bean
	public ModelMapper modelMapper() {
		LOGGER.info("Initializing ModelMapper configuration...");

		ModelMapper modelMapper = new ModelMapper();

		LOGGER.info("ModelMapper configuration initialized successfully.");
		return modelMapper;
	}
}
