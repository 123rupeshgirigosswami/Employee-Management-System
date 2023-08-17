package com.primesoft.skills_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.primesoft.skills_service.model.Skill;

/**
 * Repository interface for managing Skill entities.
 */
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

	/**
	 * Retrieves a list of skills associated with a specific employee ID.
	 *
	 * @param employeeId The ID of the employee
	 * @return List of skills associated with the employee
	 */
	List<Skill> findAllByEmployeeId(Long employeeId);

	// You can add more custom queries here if needed
}
