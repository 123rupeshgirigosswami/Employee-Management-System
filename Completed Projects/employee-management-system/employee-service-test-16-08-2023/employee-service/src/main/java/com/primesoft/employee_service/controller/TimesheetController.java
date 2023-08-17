package com.primesoft.employee_service.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.primesoft.employee_service.exception.ErrorResponse;
import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.ApiResponse;
import com.primesoft.employee_service.payloads.TimesheetDto;
import com.primesoft.employee_service.service.TimesheetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/timesheets")
@Tag(name = "Timesheet Management")
public class TimesheetController {

	private static final Logger logger = LoggerFactory.getLogger(TimesheetController.class);

	private final TimesheetService timesheetService;

	@Autowired
	public TimesheetController(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@Operation(summary = "Create a new timesheet")
	@PostMapping("/{employeeId}")
	public ResponseEntity<TimesheetDto> createTimesheet(@PathVariable Long employeeId,
			@RequestBody TimesheetDto timesheetDto) {
		logger.info("Creating a new timesheet for Employee ID: {}", employeeId);
		TimesheetDto savedTimesheet = timesheetService.createTimesheet(employeeId, timesheetDto);
		return new ResponseEntity<>(savedTimesheet, HttpStatus.CREATED);
	}

	@Operation(summary = "Get a timesheet by ID")
	@GetMapping("/{timesheetId}")
	public ResponseEntity<?> getTimesheetById(@PathVariable Long timesheetId) {
		try {
			logger.info("Fetching timesheet with ID: {}", timesheetId);
			TimesheetDto timesheetDto = timesheetService.getTimesheetById(timesheetId);
			return new ResponseEntity<>(timesheetDto, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.error("Timesheet with ID {} not found.", timesheetId);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Update a timesheet")
	@PutMapping("/{timesheetId}")
	public ResponseEntity<?> updateTimesheet(@PathVariable Long timesheetId, @RequestBody TimesheetDto timesheetDto) {
		try {
			logger.info("Updating timesheet with ID: {}", timesheetId);
			TimesheetDto updatedTimesheet = timesheetService.updateTimesheet(timesheetId, timesheetDto);
			return new ResponseEntity<>(updatedTimesheet, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			logger.error("Timesheet with ID {} not found.", timesheetId);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Delete a timesheet by ID")
	@DeleteMapping("/{timesheetId}")
	public ResponseEntity<ApiResponse> deleteTimesheet(@PathVariable Long timesheetId) {
		try {
			logger.info("Deleting timesheet with ID: {}", timesheetId);
			Timesheet deletedTimesheet = timesheetService.deleteTimesheet(timesheetId);
			String message = String.format("Timesheet with ID %d of employee %d has been deleted.",
					deletedTimesheet.getId(), deletedTimesheet.getEmployee().getId());
			ApiResponse response = new ApiResponse(true, message);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

		} catch (ResourceNotFoundException e) {
			logger.error("Timesheet with ID {} not found.", timesheetId);
			ApiResponse response = new ApiResponse(false, e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get all timesheets")
	@GetMapping
	public ResponseEntity<Page<TimesheetDto>> getAllTimesheets(Pageable pageable) {
		logger.info("Fetching all timesheets");
		Page<TimesheetDto> timesheets = timesheetService.getAllTimesheets(pageable);
		return new ResponseEntity<>(timesheets, HttpStatus.OK);
	}

	@Operation(summary = "Get timesheets by date range")
	@GetMapping("/from/{fromDate}/to/{toDate}")
	public ResponseEntity<?> getAllByFromDateAndToDate(@PathVariable String fromDate, @PathVariable String toDate,
			Pageable pageable) {
		try {
			logger.info("Fetching timesheets from {} to {}", fromDate, toDate);
			LocalDate from = LocalDate.parse(fromDate);
			LocalDate to = LocalDate.parse(toDate);

			Page<TimesheetDto> timesheets = timesheetService.getAllByFromDateAndToDate(from, to, pageable);
			return ResponseEntity.ok(timesheets);
		} catch (DateTimeParseException e) {
			logger.error("Invalid date format. Dates must be in the format yyyy-MM-dd.");
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
					"Invalid date format. Dates must be in the format yyyy-MM-dd.", LocalDateTime.now());
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (Exception e) {
			logger.error("An unexpected error occurred while retrieving timesheets by date range.", e);
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"An unexpected error occurred", LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@Operation(summary = "Get timesheets by employee id")
	@GetMapping("/employee/{employeeId}")
	public ResponseEntity<?> getAllTimesheetsByEmployeeId(@PathVariable Long employeeId) {
		try {
			List<TimesheetDto> timesheets = timesheetService.getAllTimesheetsByEmployeeId(employeeId);
			return ResponseEntity.ok(timesheets);
		} catch (ResourceNotFoundException e) {
			logger.error("Timesheets not found for employee with ID {}", employeeId);
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
