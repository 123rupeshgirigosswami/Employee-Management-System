package com.primesoft.skills_service.service;

import java.util.Set;

import com.primesoft.skills_service.payloads.SkillDTO;

/**
 * Service interface for managing skills.
 */
public interface SkillService {

	/**
	 * Creates skills based on the provided skill DTOs.
	 *
	 * @param skillDTOs Set of skill DTOs
	 * @return Set of created skill DTOs
	 */
	// Set<SkillDTO> createSkill(Set<Object> skillDTOs);
	Set<SkillDTO> createSkill(Set<SkillDTO> skillDTOs);

	/**
	 * Retrieves all skills.
	 *
	 * @return Set of skill DTOs
	 */
	Set<SkillDTO> readSkills();

	/**
	 * Retrieves skills associated with a specific employee ID.
	 *
	 * @param employeeId The ID of the employee
	 * @return Set of skill DTOs associated with the employee
	 */
	Set<SkillDTO> readSkillsByEmployeeId(Long employeeId);

	SkillDTO getSkillBySkillId(Long skillId);

	/**
	 * Updates skills based on the provided updated skill DTOs.
	 *
	 * @param updatedSkillDTOs Set of updated skill DTOs
	 * @return Set of updated skill DTOs
	 */
	Set<SkillDTO> updateSkill(Set<SkillDTO> updatedSkillDTOs);

	/**
	 * Deletes a skill with the specified skill ID.
	 *
	 * @param skillId The ID of the skill to be deleted
	 */
	void deleteSkill(Long skillId);
}
