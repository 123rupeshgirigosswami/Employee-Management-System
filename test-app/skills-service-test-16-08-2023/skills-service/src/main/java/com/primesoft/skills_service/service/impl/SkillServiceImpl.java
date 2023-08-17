package com.primesoft.skills_service.service.impl;

import com.google.gson.Gson;

import com.primesoft.skills_service.model.Skill;
import com.primesoft.skills_service.payloads.SkillDTO;
import com.primesoft.skills_service.repository.SkillRepository;
import com.primesoft.skills_service.service.SkillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;


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

//    @Override
//    @Transactional
//    @Operation(summary ="Create skills")
//    public Set<SkillDTO> createSkill(Set<Object> skillDTOs) {
//        LOGGER.info("Creating skills...");
//        Set<SkillDTO> validSkillDTOs = new HashSet<>();
//        Skill skillEntity = null;
//        Gson gson = new Gson();
//
//        for (Object skillDTO : skillDTOs) {
//            SkillDTO rt = gson.fromJson(skillDTO.toString(), SkillDTO.class);
//
//            skillEntity = modelMapper.map(rt, Skill.class);
//            skillRepository.save(skillEntity);
//            validSkillDTOs.add(rt);
//        }
//
//        LOGGER.info("Skills created successfully.");
//        return validSkillDTOs;
//    }
    
  @Override
  @Transactional
  public Set<SkillDTO> createSkill(Set<SkillDTO> skillDTOs) {
      Set<SkillDTO> validSkillDTOs = new HashSet<>();
      Skill skillEntity = null;
    //  Gson gson = new Gson();
      System.out.println(1);
  	System.out.println("enter into skill service");
  	System.out.println(skillDTOs);

      for (SkillDTO skillDTO : skillDTOs) {
      	//SkillDTO rt =gson.fromJson(skillDTO.toString(), SkillDTO.class);
      	//System.out.println(rt);
      	System.out.println();
           
          	
             // SkillDTO validSkillDTO = (SkillDTO) skillDTO;
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
    @Operation(summary="Update skills")
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
    @Operation(summary="Delete a skill by ID")
    public void deleteSkill(Long skillId) {
        LOGGER.info("Deleting skill with ID: {}", skillId);
        skillRepository.deleteById(skillId);
        LOGGER.info("Skill with ID {} has been deleted.", skillId);
    }
}
