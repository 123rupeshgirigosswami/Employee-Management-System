package com.primesoft.employee_service.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.payloads.EmployeeDTO;
import com.primesoft.employee_service.payloads.EmployeeDTOWithOutTasks;
import com.primesoft.employee_service.payloads.SkillDTO;
import com.primesoft.employee_service.payloads.TaskDTO;
import com.primesoft.employee_service.payloads.TaskDTOWithoutTaskID;
import com.primesoft.employee_service.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/employees")
@Tag(name = "Employee Management")
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeService employeeService;

	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Operation(summary = "Add a new employee")
	@PostMapping
	public ResponseEntity<EmployeeDTO> addEmployee(@ModelAttribute EmployeeDTO employeeDTO,
			@RequestParam(value = "skills", required = false) String skills,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
		Set<SkillDTO> skillSet = new HashSet<>();

		System.out.println(1);
		System.out.println("enter into employee controller");

		if (skills != null && !skills.isEmpty()) {
			String[] skillNames = skills.split(",");
			for (String skillName : skillNames) {
				skillSet.add(new SkillDTO(skillName));
			}
			LOGGER.info("Adding employee with skills: {}", skillSet);
		}

		EmployeeDTO addedEmployee = employeeService.addEmployee(employeeDTO, skillSet, file);
		return new ResponseEntity<>(addedEmployee, HttpStatus.CREATED);
	}

	@Operation(summary = "Download employee document")
	@GetMapping("/document/{employeeId}")
	public ResponseEntity<Resource> downloadEmployeeDocument(@PathVariable Long employeeId) {
		LOGGER.info("Downloading document for employee with ID: {}", employeeId);

		byte[] documentContent = employeeService.downloadEmployeeDocument(employeeId);
		EmployeeDTO employee = employeeService.getEmployeeById(employeeId);

		if (documentContent != null) {
			ByteArrayResource resource = new ByteArrayResource(documentContent);

			LOGGER.info("Document found. Initiating download.");

			HttpHeaders headers = new HttpHeaders();
			headers.setContentDispositionFormData("attachment", employee.getFileName());
			headers.setContentType(MediaType.parseMediaType(employee.getFileType()));

			return ResponseEntity.ok().headers(headers).contentLength(documentContent.length).body(resource);
		} else {
			LOGGER.warn("Document not found for employee with ID: {}", employeeId);
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Get all employees with tasks")
	@GetMapping
	public ResponseEntity<Page<EmployeeDTO>> getAllEmployeesAndTasks(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		Page<EmployeeDTO> employees = employeeService.getAllEmployeesAndTasksWithPagination(page, pageSize);
		return ResponseEntity.ok(employees);
	}

	@Operation(summary = "Get all employees without tasks")
	@GetMapping("/without-tasks")
	public ResponseEntity<Page<EmployeeDTOWithOutTasks>> getAllEmployeesWithoutTasks(
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
		Page<EmployeeDTOWithOutTasks> employeesPage = employeeService.getAllEmployeesWithoutTasksWithPagination(page,
				pageSize);
		return new ResponseEntity<>(employeesPage, HttpStatus.OK);
	}

	@Operation(summary = "Get employee by ID")
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
		LOGGER.info("Fetching employee by ID: {}", id);
		EmployeeDTO employee = employeeService.getEmployeeById(id);
		return ResponseEntity.ok(employee);
	}

	@Operation(summary = "Update an employee with details")
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id,
			@ModelAttribute EmployeeDTO updatedEmployeeDTO,
			@RequestParam(value = "skills", required = false) String skills,
			@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

		Set<SkillDTO> skillSet = new HashSet<>();

		if (skills != null && !skills.isEmpty()) {
			String[] skillNames = skills.split(",");
			for (String skillName : skillNames) {
				skillSet.add(new SkillDTO(skillName));
			}
			LOGGER.info("Updating employee with skills: {}", skillSet);
		}

		EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, updatedEmployeeDTO, skillSet, file);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@Operation(summary = "Delete employee by ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
		LOGGER.info("Deleting employee with ID: {}", id);
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.ok("Employee with ID " + id + " has been successfully deleted.");
	}

	@Operation(summary = "Add a task for an employee")
	@PostMapping("/{employeeId}/tasks")
	public ResponseEntity<TaskDTO> addTaskForEmployee(@PathVariable Long employeeId, @RequestBody TaskDTO taskDTO) {
		LOGGER.info("Adding a new task for Employee ID {}: {}", employeeId, taskDTO);
		TaskDTO addedTask = employeeService.addTaskForEmployee(employeeId, taskDTO);
		return new ResponseEntity<>(addedTask, HttpStatus.CREATED);
	}

	@Operation(summary = "Get all tasks for an employee")
	@GetMapping("/tasks/{employeeId}")
	public ResponseEntity<Page<TaskDTO>> getAllTaskForEmployeeById(@PathVariable Long employeeId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(page != null ? page : 0, pageSize != null ? pageSize : 10);
			LOGGER.info("Fetching all tasks for Employee ID {} with pagination: {}", employeeId, pageable);
			Page<TaskDTO> tasks = employeeService.getAllTaskForEmployeeById(employeeId, pageable);
			return ResponseEntity.ok(tasks);
		} catch (ResourceNotFoundException ex) {
			String errorMessage = "Employee with ID " + employeeId + " not found.";
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or you can return an empty Page<TaskDTO>
		} catch (Exception e) {
			String errorMessage = "An error occurred while processing the request.";
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Get tasks by status for an employee")
	@GetMapping("/tasks/{employeeId}/{status}")
	public ResponseEntity<Page<TaskDTO>> getTasksByStatus(@PathVariable Long employeeId, @PathVariable String status,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		LOGGER.info("Fetching tasks with status {} for Employee ID: {}", status, employeeId);
		Page<TaskDTO> tasks = employeeService.getTasksByStatus(employeeId, status, page != null ? page : 0,
				pageSize != null ? pageSize : 10);
		return ResponseEntity.ok(tasks);
	}

	@Operation(summary = "Update tasks for an employee")
	@PutMapping("/{employeeId}/tasks")
	public ResponseEntity<EmployeeDTO> updateEmployeeTasks(@PathVariable Long employeeId,
			@RequestBody List<TaskDTO> taskDTOList) {
		LOGGER.info("Updating tasks for Employee ID {}: {}", employeeId, taskDTOList);
		EmployeeDTO updatedEmployee = employeeService.updateEmployeeTasksByEmployeeId(employeeId, taskDTOList);
		return ResponseEntity.ok(updatedEmployee);
	}

	@Operation(summary = "Get all tasks for an employee without IDs")
	@GetMapping("/tasks-without-ids/{employeeId}")
	public ResponseEntity<Page<TaskDTOWithoutTaskID>> getAllTasksForEmployeeWithoutIds(@PathVariable Long employeeId,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
		try {
			Pageable pageable = PageRequest.of(page, pageSize);
			LOGGER.info("Fetching all tasks for Employee ID {} with pagination: {}", employeeId, pageable);
			Page<TaskDTOWithoutTaskID> taskDTOWithoutTaskID = employeeService
					.getAllTasksForEmployeeWithoutIds(employeeId, pageable);
			return ResponseEntity.ok(taskDTOWithoutTaskID);
		} catch (ResourceNotFoundException ex) {
			String errorMessage = "Employee with ID " + employeeId + " not found.";
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or you can return an empty
																			// Page<TaskDTOWithoutTaskID>
		} catch (Exception e) {
			String errorMessage = "An error occurred while processing the request.";
			LOGGER.error(errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
