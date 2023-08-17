package com.primesoft.employee_service.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Data
@Table(name = "tasks")
@Schema(description = "Details about a Task")
public class Task {

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "The unique ID of the task")
	private Long id;

	@Schema(description = "The description of the task")
	private String descriptions;

	@Schema(description = "The due date of the task")
	private LocalDate dueDate;

	@Schema(description = "The status of the task")
	private String status;

	@ManyToOne
	@JoinColumn(name = "employee_ids")
	@Schema(description = "The employee associated with the timesheet")
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.DETACH })
	@JoinColumn(name = "timesheet_id")
	@Schema(description = "The timesheet associated with the task")
	private Timesheet timesheet;

	/**
	 * Assigns the task to an employee. This method sets the employee for whom the
	 * task is assigned.
	 *
	 * @param employee The employee to whom the task is assigned
	 */
	public void assignToEmployee(Employee employee) {
		this.employee = employee;
		logger.info("Task {} assigned to employee {}", id, employee.getId());
	}

	/**
	 * Removes the assignment of the task from an employee. This method removes the
	 * employee assignment for the task.
	 */
	public void unassignFromEmployee() {
		this.employee = null;
		logger.info("Task {} unassigned from employee", id);
	}
}
