package com.primesoft.employee_service.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.primesoft.employee_service.payloads.EmployeeDTO;
import com.primesoft.employee_service.payloads.EmployeeDTOWithOutTasks;
import com.primesoft.employee_service.payloads.SkillDTO;
import com.primesoft.employee_service.payloads.TaskDTO;
import com.primesoft.employee_service.payloads.TaskDTOWithoutTaskID;

/**
 * Service interface for managing Employee operations.
 */
public interface EmployeeService {

	/**
	 * Add a new employee.
	 *
	 * @param employeeDTO Employee data.
	 * @param skills      Set of skills associated with the employee.
	 * @param file        Employee document file.
	 * @return Created employee details.
	 * @throws IOException If there's an issue with the file.
	 */
	EmployeeDTO addEmployee(EmployeeDTO employeeDTO, Set<SkillDTO> skills, MultipartFile file) throws IOException;

	/**
	 * Download the document of an employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @return Employee document data.
	 */
	byte[] downloadEmployeeDocument(Long employeeId);

	/**
	 * Get a page of employees with their tasks.
	 *
	 * @param page     Page number.
	 * @param pageSize Page size.
	 * @return Page of employees with tasks.
	 */
	Page<EmployeeDTO> getAllEmployeesAndTasksWithPagination(Integer page, Integer pageSize);

	Page<EmployeeDTOWithOutTasks> getAllEmployeesWithoutTasksWithPagination(Integer page, Integer pageSize);

	EmployeeDTO getEmployeeById(Long id);

	EmployeeDTO updateEmployee(Long id, EmployeeDTO updatedEmployeeDTO, Set<SkillDTO> skills, MultipartFile file)
			throws IOException;

	void deleteEmployeeById(Long id);

	TaskDTO addTaskForEmployee(Long employeeId, TaskDTO taskDTO);

	Page<TaskDTO> getAllTaskForEmployeeById(Long employeeId, Pageable pageable);

	Page<TaskDTO> getTasksByStatus(Long employeeId, String status, int page, int pageSize);

	/**
	 * Update tasks for an employee by ID.
	 *
	 * @param employeeId  The ID of the employee.
	 * @param taskDTOList List of tasks to be updated.
	 * @return Updated employee details.
	 */
	EmployeeDTO updateEmployeeTasksByEmployeeId(Long employeeId, List<TaskDTO> taskDTOList);

	/**
	 * Get a page of tasks for an employee without task IDs.
	 *
	 * @param employeeId The ID of the employee.
	 * @param pageable   Pageable information for pagination.
	 * @return Page of tasks for the employee without task IDs.
	 */
	Page<TaskDTOWithoutTaskID> getAllTasksForEmployeeWithoutIds(Long employeeId, Pageable pageable);
}
