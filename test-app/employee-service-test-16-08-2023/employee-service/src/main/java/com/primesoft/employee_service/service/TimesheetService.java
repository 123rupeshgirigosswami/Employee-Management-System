package com.primesoft.employee_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.TimesheetDto;

/**
 * Service interface for managing Timesheet operations.
 */
public interface TimesheetService {

	/**
	 * Create a new timesheet.
	 *
	 * @param employeeId   The ID of the employee.
	 * @param timesheetDto Timesheet data.
	 * @return Created timesheet details.
	 */
	TimesheetDto createTimesheet(Long employeeId, TimesheetDto timesheetDto);

	/**
	 * Get a timesheet by ID.
	 *
	 * @param timesheetId The ID of the timesheet.
	 * @return Timesheet details.
	 */
	TimesheetDto getTimesheetById(Long timesheetId);

	/**
	 * Update a timesheet.
	 *
	 * @param timesheetId  The ID of the timesheet.
	 * @param timesheetDto Timesheet data.
	 * @return Updated timesheet details.
	 */
	TimesheetDto updateTimesheet(Long timesheetId, TimesheetDto timesheetDto);

	/**
	 * Delete a timesheet.
	 *
	 * @param timesheetId The ID of the timesheet.
	 * @return Deleted timesheet.
	 */
	Timesheet deleteTimesheet(Long timesheetId);

	/**
	 * Get a page of all timesheets.
	 *
	 * @param pageable Pageable information for pagination.
	 * @return Page of timesheets.
	 */
	Page<TimesheetDto> getAllTimesheets(Pageable pageable);

	/**
	 * Get a page of timesheets within a date range.
	 *
	 * @param fromDate Start date of the range.
	 * @param toDate   End date of the range.
	 * @param pageable Pageable information for pagination.
	 * @return Page of timesheets within the date range.
	 */
	Page<TimesheetDto> getAllByFromDateAndToDate(LocalDate fromDate, LocalDate toDate, Pageable pageable);

	/**
	 * Get all timesheets for an employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @return List of timesheets for the employee.
	 */
	List<TimesheetDto> getAllTimesheetsByEmployeeId(Long employeeId);
}
