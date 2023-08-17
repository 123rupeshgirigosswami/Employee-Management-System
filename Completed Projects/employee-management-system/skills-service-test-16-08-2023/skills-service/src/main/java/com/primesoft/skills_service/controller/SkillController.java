package com.primesoft.skills_service.controller;

import com.primesoft.skills_service.payloads.SkillDTO;
import com.primesoft.skills_service.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/skills")
@Tag(name = "Skill Management")
public class SkillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);

    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/createSkills")
    @Operation(summary = "Create skills")
    public ResponseEntity<Set<SkillDTO>> createSkills(@RequestBody Set<SkillDTO> skillDTOs) {
    	
    	System.out.println(1);
    	System.out.println("enter into skill controller");
    	System.out.println(skillDTOs);
        LOGGER.info("Creating skills...");
        Set<SkillDTO> addedSkills = skillService.createSkill(skillDTOs);
        LOGGER.info("Skills created successfully.");
        return new ResponseEntity<>(addedSkills, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all skills")
    public Set<SkillDTO> getAllSkills() {
        LOGGER.info("Fetching all skills...");
        return skillService.readSkills();
    }

    @GetMapping("/{employeeId}")
    @Operation(summary = "Get skills by employee ID")
    public ResponseEntity<Set<SkillDTO>> getSkillsByEmployeeId(@PathVariable Long employeeId) {
        LOGGER.info("Fetching skills for employee ID: {}", employeeId);
        Set<SkillDTO> skillDTOs = skillService.readSkillsByEmployeeId(employeeId);
        return ResponseEntity.ok(skillDTOs);
    }

    @PutMapping("/update")
    @Operation(summary = "Update skills")
    public Set<SkillDTO> updateSkills(@RequestBody Set<SkillDTO> updatedSkillDTOs) {
        LOGGER.info("Updating skills...");
        return skillService.updateSkill(updatedSkillDTOs);
    }

    @DeleteMapping("/delete/{skillId}")
    @Operation(summary = "Delete skill by ID")
    public void deleteSkill(@PathVariable Long skillId) {
        LOGGER.info("Deleting skill with ID: {}", skillId);
        skillService.deleteSkill(skillId);
        LOGGER.info("Skill with ID {} deleted successfully.", skillId);
    }
}
