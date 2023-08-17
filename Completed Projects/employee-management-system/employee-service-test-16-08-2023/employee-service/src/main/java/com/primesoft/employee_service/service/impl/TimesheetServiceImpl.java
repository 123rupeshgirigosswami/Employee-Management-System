package com.primesoft.employee_service.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.TimesheetDto;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;
import com.primesoft.employee_service.service.TimesheetService;

/**
 * Service implementation for managing employee-related operations.
 *
 * @Author Rupesh Giri
 */
@Service
public class TimesheetServiceImpl implements TimesheetService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimesheetServiceImpl.class);

	private final TimesheetRepository timesheetRepository;
	private final TaskRepository taskRepository;
	private final EmployeeRepository employeeRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public TimesheetServiceImpl(TimesheetRepository timesheetRepository, TaskRepository taskRepository,
			EmployeeRepository employeeRepository, ModelMapper modelMapper) {
		this.timesheetRepository = timesheetRepository;
		this.taskRepository = taskRepository;
		this.employeeRepository = employeeRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TimesheetDto createTimesheet(Long employeeId, TimesheetDto timesheetDto) {
		Timesheet timesheet = modelMapper.map(timesheetDto, Timesheet.class);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
		timesheet.setEmployee(employee);
		if (timesheetDto.getTaskIds() != null) {
			List<Task> tasks = new ArrayList<>();
			for (Long taskId : timesheetDto.getTaskIds()) {
				Task task = taskRepository.findById(taskId)
						.orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
				tasks.add(task);
				task.setTimesheet(timesheet);
			}
			timesheet.setTasks(tasks);
		}

		Timesheet savedTimesheet = timesheetRepository.save(timesheet);
		return modelMapper.map(savedTimesheet, TimesheetDto.class);
	}

	@Override
	public TimesheetDto getTimesheetById(Long timesheetId) {
		Timesheet timesheet = timesheetRepository.findById(timesheetId)
				.orElseThrow(() -> new ResourceNotFoundException("Timesheet", "id", timesheetId));
		return modelMapper.map(timesheet, TimesheetDto.class);
	}

	@Override
	public TimesheetDto updateTimesheet(Long timesheetId, TimesheetDto timesheetDto) {
		Timesheet timesheet = timesheetRepository.findById(timesheetId)
				.orElseThrow(() -> new ResourceNotFoundException("Timesheet", "id", timesheetId));
		timesheet.setDescription(timesheetDto.getDescription());
		timesheet.setHours(timesheetDto.getHours());
		timesheet.setWorkStatus(timesheetDto.getWorkStatus());
		timesheet.setCreatedBy(timesheetDto.getCreatedBy());
		timesheet.setUpdatedBy(timesheetDto.getUpdatedBy());
		if (timesheetDto.getTaskIds() != null) {
			List<Task> tasks = new ArrayList<>();
			for (Long taskId : timesheetDto.getTaskIds()) {
				Task task = taskRepository.findById(taskId)
						.orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));
				tasks.add(task);
			}
			timesheet.setTasks(tasks);
		}

		Timesheet updatedTimesheet = timesheetRepository.save(timesheet);
		for (Task task : updatedTimesheet.getTasks()) {
			task.setTimesheet(updatedTimesheet);
		}

		return modelMapper.map(updatedTimesheet, TimesheetDto.class);
	}

	@Override
	public Timesheet deleteTimesheet(Long timesheetId) {
		Timesheet timesheet = timesheetRepository.findById(timesheetId)
				.orElseThrow(() -> new ResourceNotFoundException("Timesheet", "id", timesheetId));
		LOGGER.info("Deleting timesheet with ID: {}", timesheetId);
		for (Task task : timesheet.getTasks()) {
			task.setTimesheet(null);
		}

		timesheetRepository.delete(timesheet);

		LOGGER.info("Timesheet with ID {} and its associated tasks have been deleted successfully.", timesheetId);

		return timesheet;
	}

	@Override
	public Page<TimesheetDto> getAllTimesheets(Pageable pageable) {
		Page<Timesheet> timesheets = timesheetRepository.findAll(pageable);
		return timesheets.map(timesheet -> modelMapper.map(timesheet, TimesheetDto.class));
	}

	@Override
	public Page<TimesheetDto> getAllByFromDateAndToDate(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
		LocalDateTime fromDateTime = fromDate.atStartOfDay();
		LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

		Page<Timesheet> timesheets = timesheetRepository.findByCreatedDateBetween(fromDateTime, toDateTime, pageable);
		return timesheets.map(timesheet -> modelMapper.map(timesheet, TimesheetDto.class));
	}

	@Override
	public List<TimesheetDto> getAllTimesheetsByEmployeeId(Long employeeId) {
		List<Timesheet> timesheets = timesheetRepository.findAllByEmployeeId(employeeId);

		if (timesheets.isEmpty()) {
			throw new ResourceNotFoundException("Timesheets", "employeeId", employeeId);
		}

		return timesheets.stream().map(timesheet -> modelMapper.map(timesheet, TimesheetDto.class))
				.collect(Collectors.toList());
	}

}
