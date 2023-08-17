package com.primesoft.employee_service.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.Hidden;

@Configuration
@Hidden // Exclude this class from Swagger documentation
public class LoggingConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingConfig.class);

	/**
	 * Logs a message when the configuration is loaded.
	 */
	@PostConstruct
	public void logLoaded() {
		LOGGER.info("Logging configuration loaded successfully.");
	}
}
