package com.primesoft.skills_service.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private static final Logger logger = LoggerFactory.getLogger(ErrorResponse.class);

	private int status;
	private String message;
	private LocalDateTime timestamp;

	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now();
		logger.error("Error Response: Status={}, Message={}", status, message);
	}
}
