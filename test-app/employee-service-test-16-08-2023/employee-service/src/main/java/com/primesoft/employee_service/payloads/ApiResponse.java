package com.primesoft.employee_service.payloads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response indicating the result of an operation")
public class ApiResponse {

	private static final Logger logger = LoggerFactory.getLogger(ApiResponse.class);

	@Schema(description = "Indicates if the operation was successful")
	private boolean success;

	@Schema(description = "Message providing additional information about the operation")
	private String message;

	/**
	 * Constructor to create an instance of ApiResponse.
	 *
	 * @param success Indicates if the operation was successful
	 */
	public ApiResponse(boolean success) {
		this.success = success;
		this.message = success ? "Operation successful" : "Operation failed";
		logger.info("ApiResponse created with success: {}", success);
	}
}
