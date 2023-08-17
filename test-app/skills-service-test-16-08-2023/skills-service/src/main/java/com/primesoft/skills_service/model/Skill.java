package com.primesoft.skills_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Entity
@Data
public class Skill {

    private static final Logger LOGGER = LoggerFactory.getLogger(Skill.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The ID of the skill")
    private Long id;

    @Schema(description = "The employee ID associated with the skill")
    private Long employeeId;

    @Schema(description = "The name of the skill")
    private String skillName;

    public Skill() {
        LOGGER.info("Creating a new Skill instance.");
    }

    public Skill(Long employeeId, String skillName) {
        this.employeeId = employeeId;
        this.skillName = skillName;
        LOGGER.info("Creating a new Skill instance with employeeId: {} and skillName: {}", employeeId, skillName);
    }
}
