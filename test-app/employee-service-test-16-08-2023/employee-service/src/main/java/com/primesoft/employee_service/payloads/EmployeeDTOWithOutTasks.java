package com.primesoft.employee_service.payloads;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for Employee without tasks")
public class EmployeeDTOWithOutTasks {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDTOWithOutTasks.class);

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

	@JsonIgnore
	private byte[] uploadDocument;

	@Schema(description = "Name of the uploaded document")
	@JsonIgnore
	private String fileName;

	@Schema(description = "Type of the uploaded document")
	@JsonIgnore
	private String fileType;

	@JsonIgnore
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
	 * Get the parsed hire date.
	 *
	 * @return The parsed hire date
	 */
	public LocalDate getParsedHireDate() {
		return LocalDate.parse(hireDate);
	}
}
