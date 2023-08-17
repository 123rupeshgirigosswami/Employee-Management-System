package com.primesoft.employee_service.payloads;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for timesheets")
public class TimesheetDto {

	private static final Logger logger = LoggerFactory.getLogger(TimesheetDto.class);

	@Schema(description = "ID of the timesheet")
	private long id;

	@Schema(description = "Description of the timesheet")
	private String description;

	@Schema(description = "Number of hours in the timesheet")
	private double hours;

	@Schema(description = "Work status of the timesheet")
	private String workStatus;

	@Schema(description = "Name of the user who created the timesheet")
	private String createdBy;

	@Schema(description = "Name of the user who updated the timesheet")
	private String updatedBy;

	@Schema(description = "List of task IDs associated with the timesheet")
	private List<Long> taskIds;

	/**
	 * Adds a task ID to the list of task IDs.
	 *
	 * @param taskId The task ID to be added
	 */
	public void addTaskId(Long taskId) {
		taskIds.add(taskId);
	}

	/**
	 * Removes a task ID from the list of task IDs.
	 *
	 * @param taskId The task ID to be removed
	 */
	public void removeTaskId(Long taskId) {
		taskIds.remove(taskId);
	}

}
