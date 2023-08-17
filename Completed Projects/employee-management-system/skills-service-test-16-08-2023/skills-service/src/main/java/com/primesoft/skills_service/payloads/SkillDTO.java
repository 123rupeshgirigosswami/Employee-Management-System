package com.primesoft.skills_service.payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Details about a skill")
public class SkillDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillDTO.class);
    
    private Long id;

    private Long employeeId;
	private String skillName;

    public SkillDTO(Long employeeId, String skillName) {
        this.employeeId = employeeId;
        this.skillName = skillName;
        LOGGER.info("Creating a new SkillDTO instance with employeeId: {} and skillName: {}", employeeId, skillName);
    }
    public SkillDTO(String skillName) {
		this.skillName = skillName;
		LOGGER.info("Created SkillDTO instance with skillName: {}", skillName);
	}
}
