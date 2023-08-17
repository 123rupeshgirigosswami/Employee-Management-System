package com.primesoft.employee_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.primesoft.employee_service.model.Employee;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Repository interface for managing Employee entities.
 */
@Repository
@Tag(name = "Employee Repository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
