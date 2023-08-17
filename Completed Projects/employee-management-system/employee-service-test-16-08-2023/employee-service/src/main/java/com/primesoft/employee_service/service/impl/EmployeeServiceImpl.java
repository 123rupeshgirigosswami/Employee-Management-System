package com.primesoft.employee_service.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;
import com.primesoft.employee_service.payloads.EmployeeDTO;
import com.primesoft.employee_service.payloads.EmployeeDTOWithOutTasks;
import com.primesoft.employee_service.payloads.SkillDTO;
import com.primesoft.employee_service.payloads.TaskDTO;
import com.primesoft.employee_service.payloads.TaskDTOWithoutTaskID;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;
import com.primesoft.employee_service.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Service implementation for managing employee-related operations.
 *
 * @Author Rupesh Giri
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	private final EmployeeRepository employeeRepository;
	private final TaskRepository taskRepository;
	private final TimesheetRepository timesheetRepository;
	private final ModelMapper modelMapper;
	@Autowired
	private RestTemplate restTemplate;

	private static final String SKILL_SERVICE_URL = "http://localhost:9092/skills";

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, TaskRepository taskRepository,
			ModelMapper modelMapper, TimesheetRepository timesheetRepository) {
		this.employeeRepository = employeeRepository;
		this.taskRepository = taskRepository;
		this.modelMapper = modelMapper;
		this.timesheetRepository = timesheetRepository;
	}

	/**
	 * Adds a new employee.
	 *
	 * @param employeeDTO EmployeeDTO containing employee information.
	 * @param skills      Set of SkillDTOs associated with the employee.
	 * @param file        MultipartFile representing employee's document.
	 * @return Added EmployeeDTO.
	 * @throws IOException if there's an issue with file handling.
	 * 
	 */
	@Override
	@Operation(summary = "Add a new employee")
	@Transactional
	public EmployeeDTO addEmployee(EmployeeDTO employeeDTO, Set<SkillDTO> skills, MultipartFile file)
			throws IOException {
		LOGGER.info("Adding a new employee: {}", employeeDTO);

		LocalDate hireDate = employeeDTO.getParsedHireDate();

		System.out.println(1);
		System.out.println("enter into employee serviceimpl");
		System.out.println(skills);

		byte[] fileContent = null;
		String fileName = null;
		String fileType = null;

		if (file != null && !file.isEmpty()) {
			fileContent = file.getBytes();
			fileName = file.getOriginalFilename();
			fileType = file.getContentType();
		}

		Employee employee = modelMapper.map(employeeDTO, Employee.class);
		employee.setHireDate(hireDate);
		employee.getTasks().forEach(task -> task.setEmployee(employee));
		employee.setUploadDocument(fileContent);
		employee.setFileName(fileName);
		employee.setFileType(fileType);

		Employee addedEmployee = employeeRepository.save(employee);

		if (skills != null && !skills.isEmpty()) {
			try {

				for (SkillDTO skillDTO : skills) {
					skillDTO.setEmployeeId(addedEmployee.getId());
				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<Set<SkillDTO>> requestEntity = new HttpEntity<>(skills, headers);

				System.out.println(2);
				System.out.println("enter into employee serviceimpl send before");
				System.out.println(skills);

				/*
				 * ResponseEntity<SkillDTO> responseEntity =
				 * restTemplate.exchange(SKILL_SERVICE_URL + "/createSkills", HttpMethod.POST,
				 * requestEntity, SkillDTO.class);
				 */
				ResponseEntity<Set<SkillDTO>> responseEntity = restTemplate.exchange(
						SKILL_SERVICE_URL + "/createSkills", HttpMethod.POST, requestEntity,
						new ParameterizedTypeReference<Set<SkillDTO>>() {
						});

				System.out.println(2);
				System.out.println("enter into employee serviceimpl send before");
				System.out.println(skills);

				if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
					throw new RuntimeException("Failed to associate skills with employee");
				}
			} catch (Exception e) {
				throw new RuntimeException("An error occurred while associating skills with employee", e);
			}
		}

		// Logging the action
//		LOGGER.info("Added a new employee: {}", addedEmployee);
		LOGGER.info("Added a new employee with ID: {}, Name: {}", addedEmployee.getId(), addedEmployee.getName());

		return modelMapper.map(addedEmployee, EmployeeDTO.class);
	}

	/**
	 * Downloads the document content for an employee.
	 *
	 * @param employeeId Employee ID.
	 * @return Document content as a byte array.
	 */
	@Override
	public byte[] downloadEmployeeDocument(Long employeeId) {
		LOGGER.info("Fetching document content for employee with ID: {}", employeeId);

		Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

		if (employeeOptional.isPresent()) {
			Employee employee = employeeOptional.get();
			byte[] documentContent = employee.getUploadDocument();

			LOGGER.info("Document content retrieved successfully.");
			return documentContent;
		} else {
			LOGGER.warn("Employee not found with ID: {}", employeeId);
			return null;
		}
	}

	@Operation(summary = "Get all employees and their tasks with pagination")
	@Transactional(readOnly = true)
	@Override
	public Page<EmployeeDTO> getAllEmployeesAndTasksWithPagination(Integer page, Integer pageSize) {
		LOGGER.info("Fetching all employees and their tasks with pagination (Page: {}, PageSize: {})", page, pageSize);
		PageRequest pageRequest = PageRequest.of(page != null ? page : 0, pageSize != null ? pageSize : 10);
		Page<Employee> employeesPage = employeeRepository.findAll(pageRequest);
		return employeesPage.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
	}

	@Operation(summary = "Get all employees without tasks with pagination")
	@Transactional(readOnly = true)
	@Override
	public Page<EmployeeDTOWithOutTasks> getAllEmployeesWithoutTasksWithPagination(Integer page, Integer pageSize) {
		LOGGER.info("Fetching all employees without tasks with pagination (Page: {}, PageSize: {})", page, pageSize);
		PageRequest pageRequest = PageRequest.of(page != null ? page : 0, pageSize != null ? pageSize : 10);
		Page<Employee> employeesPage = employeeRepository.findAll(pageRequest);
		return employeesPage.map(employee -> {
			EmployeeDTOWithOutTasks employeeDTOWithOutTasks = modelMapper.map(employee, EmployeeDTOWithOutTasks.class);
			if (employeeDTOWithOutTasks != null) {
				employeeDTOWithOutTasks.setTasks(null);
			}
			return employeeDTOWithOutTasks;
		});
	}

	@Operation(summary = "Get an employee by ID")
	@Transactional(readOnly = true)
	@Override
	public EmployeeDTO getEmployeeById(Long id) {
		LOGGER.info("Fetching employee by ID: {}", id);
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
		return modelMapper.map(employee, EmployeeDTO.class);
	}

	@Operation(summary = "Update an employee with details")
	@Transactional
	@Override
	public EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO, Set<SkillDTO> skills, MultipartFile file)
			throws IOException {
		LOGGER.info("Updating employee with ID {}: {}", id, updatedEmployeeDTO);

		LocalDate hireDate = updatedEmployeeDTO.getParsedHireDate();

		Employee existingEmployee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
		existingEmployee.setName(updatedEmployeeDTO.getName());
		existingEmployee.setDesignation(updatedEmployeeDTO.getDesignation());
		existingEmployee.setEmail(updatedEmployeeDTO.getEmail());
		existingEmployee.setDepartment(updatedEmployeeDTO.getDepartment());
		existingEmployee.setHireDate(hireDate);

		byte[] fileContent = null;
		String fileName = null;
		String fileType = null;
		if (file != null && !file.isEmpty()) {
			fileContent = file.getBytes();
			fileName = file.getOriginalFilename();
			fileType = file.getContentType();
		}
		existingEmployee.setUploadDocument(fileContent);
		existingEmployee.setFileName(fileName);
		existingEmployee.setFileType(fileType);

		List<TaskDTO> updatedTasks = updatedEmployeeDTO.getTasks();
		if (updatedTasks != null) {
			for (TaskDTO updatedTaskDTO : updatedTasks) {
				Task existingTask = taskRepository.findById(updatedTaskDTO.getId())
						.orElseThrow(() -> new ResourceNotFoundException("Task", "id", updatedTaskDTO.getId()));
				existingTask.setStatus(updatedTaskDTO.getStatus());
				taskRepository.save(existingTask);
			}
		}

		Employee updatedEmployee = employeeRepository.save(existingEmployee);

		if (skills != null && !skills.isEmpty()) {
			try {
				for (SkillDTO skillDTO : skills) {
					skillDTO.setEmployeeId(updatedEmployee.getId());
				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<Set<SkillDTO>> requestEntity = new HttpEntity<>(skills, headers);

				ResponseEntity<Object> responseEntity = restTemplate.exchange(SKILL_SERVICE_URL + "/createSkills",
						HttpMethod.POST, requestEntity, Object.class);

				if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
					throw new RuntimeException("Failed to associate skills with employee");
				}
			} catch (Exception e) {
				throw new RuntimeException("An error occurred while associating skills with employee", e);
			}
		}

		return modelMapper.map(updatedEmployee, EmployeeDTO.class);
	}

	@Operation(summary = "Delete an employee by ID")
	@Transactional
	@Override
	public void deleteEmployeeById(Long id) {
		LOGGER.info("Deleting employee with ID: {}", id);

		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

		// Delete associated tasks first
		List<Task> tasks = taskRepository.findByEmployeeId(employee.getId());
		taskRepository.deleteAll(tasks);

		// Then delete the employee
		employeeRepository.delete(employee);

		LOGGER.info("Employee with ID {} has been deleted.", id);
	}

	@Operation(summary = "Add a task for an employee")
	@Transactional
	@Override
	public TaskDTO addTaskForEmployee(Long employeeId, TaskDTO taskDTO) {
		if (employeeId == null) {
			throw new IllegalArgumentException("Employee ID cannot be null");
		}

		LOGGER.info("Adding a new task for Employee ID {}: {}", employeeId, taskDTO);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
		Task task = modelMapper.map(taskDTO, Task.class);
		task.setEmployee(employee);
		Task addedTask = taskRepository.save(task);
		return modelMapper.map(addedTask, TaskDTO.class);
	}

	@Operation(summary = "Get all tasks for an employee with pagination")
	@Transactional(readOnly = true)
	@Override
	public Page<TaskDTO> getAllTaskForEmployeeById(Long employeeId, Pageable pageable) {
		if (employeeId == null || employeeId <= 0) {
			return Page.empty();
		}

		if (pageable.getPageNumber() < 0 || pageable.getPageSize() <= 0) {
			pageable = PageRequest.of(0, 10);
		}

		LOGGER.info("Fetching all tasks for Employee ID {} with pagination: {}", employeeId, pageable);
		Page<Task> tasksPage = taskRepository.findAllByEmployeeId(employeeId, pageable);
		List<TaskDTO> taskDTOs = tasksPage.getContent().stream().map(task -> modelMapper.map(task, TaskDTO.class))
				.collect(Collectors.toList());

		return new PageImpl<>(taskDTOs, pageable, tasksPage.getTotalElements());
	}

	@Operation(summary = "Get tasks by status for an employee")
	@Transactional(readOnly = true)
	@Override
	public Page<TaskDTO> getTasksByStatus(Long employeeId, String status, int page, int pageSize) {
		LOGGER.info("Fetching tasks with status {} for Employee ID: {}", status, employeeId);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
		List<Task> tasks = employee.getTasks().stream().filter(task -> status.equalsIgnoreCase(task.getStatus()))
				.collect(Collectors.toList());
		int start = Math.min(page * pageSize, tasks.size());
		int end = Math.min((page + 1) * pageSize, tasks.size());
		List<TaskDTO> taskDTOs = tasks.subList(start, end).stream().map(task -> modelMapper.map(task, TaskDTO.class))
				.collect(Collectors.toList());
		return new PageImpl<>(taskDTOs);
	}

	@Operation(summary = "Update tasks for an employee by Employee ID")
	@Transactional
	@Override
	public EmployeeDTO updateEmployeeTasksByEmployeeId(Long employeeId, List<TaskDTO> taskDTOList) {
		LOGGER.info("Updating tasks for Employee ID {}: {}", employeeId, taskDTOList);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
		List<Task> existingTasks = employee.getTasks();
		for (TaskDTO taskDTO : taskDTOList) {
			Task existingTask = existingTasks.stream().filter(task -> Objects.equals(task.getId(), taskDTO.getId()))
					.findFirst().orElse(null);

			if (existingTask != null) {
				modelMapper.map(taskDTO, existingTask);
			} else {
				Task newTask = modelMapper.map(taskDTO, Task.class);
				newTask.setEmployee(employee);
				existingTasks.add(newTask);
			}
		}
		Employee updatedEmployee = employeeRepository.save(employee);
		return modelMapper.map(updatedEmployee, EmployeeDTO.class);
	}

	/**
	 * Get all tasks for an employee without task IDs.
	 *
	 * @param employeeId Employee ID.
	 * @param pageable   Pageable object for pagination.
	 * @return Page of TaskDTOWithoutTaskID.
	 */
	@Override
	@Operation(summary = "Get all tasks for an employee without task IDs")
	@Transactional(readOnly = true)
	public Page<TaskDTOWithoutTaskID> getAllTasksForEmployeeWithoutIds(Long employeeId, Pageable pageable) {
		LOGGER.info("Fetching all tasks for Employee ID {} without task IDs", employeeId);
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

		List<TaskDTOWithoutTaskID> taskDTOWithoutTaskIDs = employee.getTasks().stream()
				.map(this::convertToTaskDTOWithoutId).collect(Collectors.toList());

		return new PageImpl<>(taskDTOWithoutTaskIDs, pageable, taskDTOWithoutTaskIDs.size());
	}

	private TaskDTOWithoutTaskID convertToTaskDTOWithoutId(Task task) {
		TaskDTOWithoutTaskID taskDTOWithoutTaskID = modelMapper.map(task, TaskDTOWithoutTaskID.class);
		taskDTOWithoutTaskID.setId(null);
		return taskDTOWithoutTaskID;
	}
}
