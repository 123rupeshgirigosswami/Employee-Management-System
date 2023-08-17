package com.primesoft.employee_service.payloads;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for Employee")
public class EmployeeDTO {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDTO.class);

	@Schema(description = "ID of the employee")
	private Long id;

	@Schema(description = "Name of the employee")
	private String name;

	@Schema(description = "Designation of the employee")
	private String designation;

	@Schema(description = "Email of the employee")
	private String email;

	@Schema(description = "Department of the employee")
	private String department;

	@Schema(description = "Hire date of the employee")
	private String hireDate;

	@Schema(hidden = true) // Use Schema annotation to hide from Swagger
	@JsonIgnore
	private byte[] uploadDocument;

	@Schema(description = "Name of the uploaded document")
	@JsonIgnore
	private String fileName;

	@Schema(description = "Type of the uploaded document")
	@JsonIgnore
	private String fileType;

	@Schema(description = "List of tasks associated with the employee")
	private List<TaskDTO> tasks = new ArrayList<>();

	/**
	 * Add a task to the list of tasks associated with the employee.
	 *
	 * @param task The task to add
	 */
	public void addTask(TaskDTO task) {
		tasks.add(task);
		logger.info("Added task to employee: {}", task);
	}

	/**
	 * Remove a task from the list of tasks associated with the employee.
	 *
	 * @param task The task to remove
	 */
	public void removeTask(TaskDTO task) {
		tasks.remove(task);
		logger.info("Removed task from employee: {}", task);
	}

	/**
	 * Set the hire date as a formatted string.
	 *
	 * @param hireDate The hire date to set
	 */
	public void setParsedHireDate(LocalDate hireDate) {
		this.hireDate = hireDate.toString();
	}

	/**
	 * Get the parsed hire date.
	 *
	 * @return The parsed hire date
	 */
	public LocalDate getParsedHireDate() {
		if (hireDate == null) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(hireDate, formatter);
	}
}
