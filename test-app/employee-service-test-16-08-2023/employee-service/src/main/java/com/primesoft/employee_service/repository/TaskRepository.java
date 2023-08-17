package com.primesoft.employee_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repository interface for managing Task entities.
 */
@Repository
@Tag(name = "Task Repository")
public interface TaskRepository extends JpaRepository<Task, Long> {

	/**
	 * Retrieve all tasks associated with a specific employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @param pageable   The pageable information for pagination.
	 * @return A page of tasks associated with the employee.
	 */
	Page<Task> findAllByEmployeeId(Long employeeId, Pageable pageable);

	/**
	 * Retrieve all tasks associated with a specific employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @return A list of tasks associated with the employee.
	 */
	List<Task> findByEmployeeId(Long employeeId);

	/**
	 * Retrieve tasks associated with a specific employee and a given status.
	 *
	 * @param employee The employee entity.
	 * @param status   The status of the tasks.
	 * @param pageable The pageable information for pagination.
	 * @return A page of tasks with the specified status associated with the
	 *         employee.
	 */
	Page<Task> findByEmployeeAndStatus(Employee employee, String status, Pageable pageable);
}
