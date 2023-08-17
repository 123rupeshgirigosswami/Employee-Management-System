package com.primesoft.employee_service.payloads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Data Transfer Object for employee skill")
public class SkillDTO {

	private static final Logger logger = LoggerFactory.getLogger(SkillDTO.class);

	@Schema(description = "ID of the employee")
	private Long employeeId;

	@Schema(description = "Name of the skill")
	private String skillName;

	public SkillDTO(String skillName) {
		this.skillName = skillName;
		logger.info("Created SkillDTO instance with skillName: {}", skillName);
	}
}
