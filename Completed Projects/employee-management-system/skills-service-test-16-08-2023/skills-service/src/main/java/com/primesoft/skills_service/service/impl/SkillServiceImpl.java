package com.primesoft.skills_service.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.primesoft.skills_service.exception.ResourceNotFoundException;
import com.primesoft.skills_service.model.Skill;
import com.primesoft.skills_service.payloads.SkillDTO;
import com.primesoft.skills_service.repository.SkillRepository;
import com.primesoft.skills_service.service.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service implementation for managing employee-related operations.
 *
 * @Author Rupesh Giri
 */
@Service
@Tag(name = "Skill Management")
public class SkillServiceImpl implements SkillService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SkillServiceImpl.class);

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional
	public Set<SkillDTO> createSkill(Set<SkillDTO> skillDTOs) {
		Set<SkillDTO> validSkillDTOs = new HashSet<>();
		Skill skillEntity = null;
		for (SkillDTO skillDTO : skillDTOs) {
			skillEntity = modelMapper.map(skillDTO, Skill.class);
			skillRepository.save(skillEntity);
			validSkillDTOs.add(skillDTO);
		}
		return validSkillDTOs;
	}

	@Override
	public Set<SkillDTO> readSkills() {
		LOGGER.info("Fetching all skills...");
		Set<SkillDTO> skillDTOs = new HashSet<>();
		for (Skill skill : skillRepository.findAll()) {
			skillDTOs.add(new SkillDTO(skill.getId(), skill.getEmployeeId(), skill.getSkillName()));
		}
		LOGGER.info("Fetched all skills successfully.");
		return skillDTOs;
	}

	@Override
	@Transactional
	public Set<SkillDTO> readSkillsByEmployeeId(Long employeeId) {
		LOGGER.info("Fetching skills by employee ID: {}", employeeId);
		Set<SkillDTO> skillDTOs = new HashSet<>();
		for (Skill skill : skillRepository.findAllByEmployeeId(employeeId)) {
			skillDTOs.add(new SkillDTO(skill.getId(), skill.getEmployeeId(), skill.getSkillName()));
		}
		LOGGER.info("Fetched skills by employee ID {} successfully.", employeeId);
		return skillDTOs;
	}

	@Override
	public SkillDTO getSkillBySkillId(Long skillId) {
		LOGGER.info("Fetching skill by skill ID: {}", skillId);

		Optional<Skill> skillOptional = skillRepository.findById(skillId);

		if (skillOptional.isPresent()) {
			SkillDTO skillDTO = modelMapper.map(skillOptional.get(), SkillDTO.class);
			return skillDTO;
		} else {
			throw new ResourceNotFoundException("Skill", "skillId", skillId);
		}
	}

	@Override
	@Operation(summary = "Update skills")
	public Set<SkillDTO> updateSkill(Set<SkillDTO> updatedSkillDTOs) {
		LOGGER.info("Updating skills...");
		Set<SkillDTO> updatedSkills = new HashSet<>();
		for (SkillDTO updatedSkillDTO : updatedSkillDTOs) {
			Optional<Skill> optionalSkill = skillRepository.findById(updatedSkillDTO.getId());
			if (optionalSkill.isPresent()) {
				Skill existingSkill = optionalSkill.get();
				existingSkill.setEmployeeId(updatedSkillDTO.getEmployeeId());
				existingSkill.setSkillName(updatedSkillDTO.getSkillName());

				Skill updatedSkill = skillRepository.save(existingSkill);
				updatedSkills.add(
						new SkillDTO(updatedSkill.getId(), updatedSkill.getEmployeeId(), updatedSkill.getSkillName()));
			}
		}
		LOGGER.info("Skills updated successfully.");
		return updatedSkills;
	}

	@Override
	@Operation(summary = "Delete a skill by ID")
	public void deleteSkill(Long skillId) {
		LOGGER.info("Deleting skill with ID: {}", skillId);
		skillRepository.deleteById(skillId);
		LOGGER.info("Skill with ID {} has been deleted.", skillId);
	}
}
