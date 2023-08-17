package com.primesoft.employee_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.primesoft.employee_service.model.Timesheet;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repository interface for managing Timesheet entities.
 */
@Repository
@Tag(name = "Timesheet Repository")
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

	/**
	 * Retrieve timesheets created within a specific date range.
	 *
	 * @param fromDate The start date of the range.
	 * @param toDate   The end date of the range.
	 * @param pageable The pageable information for pagination.
	 * @return A page of timesheets created within the date range.
	 */
	Page<Timesheet> findByCreatedDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

	/**
	 * Retrieve all timesheets associated with a specific employee.
	 *
	 * @param employeeId The ID of the employee.
	 * @return A list of timesheets associated with the employee.
	 */
	List<Timesheet> findAllByEmployeeId(Long employeeId);
}
