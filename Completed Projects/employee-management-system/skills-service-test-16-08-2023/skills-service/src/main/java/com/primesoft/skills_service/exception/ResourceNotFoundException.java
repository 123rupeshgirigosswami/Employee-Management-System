package com.primesoft.skills_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for resource not found cases. This exception is thrown when
 * a requested resource is not found. It includes information about the resource
 * name, field name, and field value.
 * 
 * @author Rupesh Giri
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ResourceNotFoundException with the provided parameters.
	 *
	 * @param resourceName The name of the resource
	 * @param fieldName    The name of the field that caused the exception
	 * @param fieldValue   The value of the field that caused the exception
	 */
	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
		logger.error("ResourceNotFoundException: {}", getMessage());
	}
}
