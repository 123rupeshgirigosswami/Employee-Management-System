package com.primesoft.employee_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for Employee Service exceptions. This exception is
 * used to represent unexpected errors in the Employee Service.
 *
 * @author Rupesh Giri
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EmployeeServiceException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceException.class);

	private static final long serialVersionUID = 1L;

	public EmployeeServiceException(String message) {
		super(message);
		logger.error(message);
	}

	public EmployeeServiceException(String message, Throwable cause) {
		super(message, cause);
		logger.error(message, cause);
	}
}
