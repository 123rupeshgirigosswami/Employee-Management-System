package com.primesoft.employee_service.payloads;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for tasks without ID")
public class TaskDTOWithoutTaskID {

	private static final Logger logger = LoggerFactory.getLogger(TaskDTOWithoutTaskID.class);

	@JsonIgnore
	private Long id;

	@Schema(description = "Description of the task")
	private String descriptions;

	@Schema(description = "Due date of the task (formatted as yyyy-MM-dd)")
	private String dueDate;

	@Schema(description = "Status of the task")
	private String status;

	/**
	 * Parses the dueDate string into a LocalDate object.
	 *
	 * @return Parsed LocalDate or null if dueDate is null
	 */
	public LocalDate getParsedDueDate() {
		if (dueDate == null) {
			return null;
		}
		try {
			return LocalDate.parse(dueDate);
		} catch (Exception e) {
			logger.error("Error parsing dueDate: {}", e.getMessage());
			return null;
		}
	}
}
