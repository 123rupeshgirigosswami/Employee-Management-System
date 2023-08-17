package com.primesoft.employee_service.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Entity class representing an employee. This class stores information about
 * employees and their associated tasks. The class includes annotations for JPA
 * mapping, Swagger documentation, and Lombok.
 *
 * @author Rupesh Giri
 */
@Entity
@Data
@Table(name = "employee")
@Schema(description = "Details about a Employee")

public class Employee {

	private static final Logger logger = LoggerFactory.getLogger(Employee.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "The unique ID of the employee")
	private Long id;

	@Schema(description = "The name of the employee")
	private String name;

	@Schema(description = "The designation of the employee")
	private String designation;

	@Schema(description = "The email address of the employee")
	private String email;

	@Schema(description = "The department of the employee")
	private String department;

	@Schema(description = "The hire date of the employee")
	private LocalDate hireDate;

	@Lob
	@Column(name = "upload_document")
	@Schema(description = "The uploaded document of the employee")
	private byte[] uploadDocument;

	@Schema(description = "The name of the uploaded file")
	private String fileName;

	@Schema(description = "The type of the uploaded file")
	private String fileType;

	@OneToMany(mappedBy = "employee", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, orphanRemoval = false)
	@Schema(description = "List of tasks associated with the employee")
	private List<Task> tasks = new ArrayList<>();

	/**
	 * Adds a task to the list of tasks associated with the employee. This method
	 * adds a task to the employee's task list and sets the employee reference in
	 * the task object.
	 *
	 * @param task The task to be added
	 */
	public void addTask(Task task) {
		tasks.add(task);
		task.setEmployee(this);
		logger.info("Added task to employee: {}", task);
	}

	/**
	 * Removes a task from the list of tasks associated with the employee. This
	 * method removes a task from the employee's task list and sets the employee
	 * reference in the task object to null.
	 *
	 * @param task The task to be removed
	 */
	public void removeTask(Task task) {
		tasks.remove(task);
		task.setEmployee(null);
		logger.info("Removed task from employee: {}", task);
	}

}
