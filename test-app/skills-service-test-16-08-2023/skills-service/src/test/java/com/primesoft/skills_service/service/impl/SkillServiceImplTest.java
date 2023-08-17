package com.primesoft.skills_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.primesoft.skills_service.model.Skill;
import com.primesoft.skills_service.payloads.SkillDTO;
import com.primesoft.skills_service.repository.SkillRepository;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SkillServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SkillServiceImplTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private SkillRepository skillRepository;

    @Autowired
    private SkillServiceImpl skillServiceImpl;

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    void testCreateSkill() {
        assertTrue(skillServiceImpl.createSkill(new HashSet<>()).isEmpty());
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill2() {
    	// Create a set of SkillDTO objects
    			Set<SkillDTO> skills = new HashSet<>();
    			SkillDTO skill1 = new SkillDTO("Skill1");
    			SkillDTO skill2 = new SkillDTO("Skill2");
    			skills.add(skill1);
    			skills.add(skill2);
    	        Set<SkillDTO> skillDTOs = new HashSet<>();
    	        skillDTOs.add(skill1);
    	        skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill3() {
    	// Create a set of SkillDTO objects
    			Set<SkillDTO> skills = new HashSet<>();
    			SkillDTO skill1 = new SkillDTO("Skill1");
    			SkillDTO skill2 = new SkillDTO("Skill2");
    			skills.add(skill1);
    			skills.add(skill2);
       
    	Set<SkillDTO> skillDTOs = new HashSet<>();
        skillDTOs.add(skill1);
        skillDTOs.add(skill2);
        skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill4() {
    	// Create a set of SkillDTO objects
		Set<SkillDTO> skills = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Skill1");
		SkillDTO skill2 = new SkillDTO("Skill2");
		skills.add(skill1);
		skills.add(skill2);
        
		Set<SkillDTO> skillDTOs = new HashSet<>();
        skillDTOs.add(skill1);
        skillDTOs.add(skill2);
        skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill5() {
    	// Create a set of SkillDTO objects
		Set<SkillDTO> skills = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Skill1");
		SkillDTO skill2 = new SkillDTO("Skill2");
		skills.add(skill1);
		skills.add(skill2);
        Set<SkillDTO> skillDTOs = new HashSet<>();
        skillDTOs.add(skill1);
        skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill6() {
    	// Create a set of SkillDTO objects
    			Set<SkillDTO> skills = new HashSet<>();
    			SkillDTO skill1 = new SkillDTO("Skill1");
    			SkillDTO skill2 = new SkillDTO("Skill2");
    			skills.add(skill1);
    			skills.add(skill2);
    	        Set<SkillDTO> skillDTOs = new HashSet<>();
    	        skillDTOs.add(skill1);
    	        skillServiceImpl.createSkill(skillDTOs);
     
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill7() {
    	// Create a set of SkillDTO objects
    			Set<SkillDTO> skills = new HashSet<>();
    			SkillDTO skill1 = new SkillDTO("Skill1");
    			SkillDTO skill2 = new SkillDTO("Skill2");
    			skills.add(skill1);
    			skills.add(skill2);
    	        Set<SkillDTO> skillDTOs = new HashSet<>();
    	       
    	       
        
        skillDTOs.add(skill1);
        skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    void testCreateSkill8() {
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Skill>>any())).thenReturn(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);
     // Create a set of SkillDTO objects
     		Set<SkillDTO> skills = new HashSet<>();
     		SkillDTO skill1 = new SkillDTO("Skill1");
     		
            

        Set<SkillDTO> skillDTOs = new HashSet<>();
        skillDTOs.add(skill1);
        assertEquals(1, skillServiceImpl.createSkill(skillDTOs).size());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Skill>>any());
        verify(skillRepository).save(Mockito.<Skill>any());
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill9() {
                Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Skill>>any())).thenReturn(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);

     // Create a set of SkillDTO objects
     		Set<SkillDTO> skills = new HashSet<>();
     		SkillDTO skill1 = new SkillDTO("Skill1");
     		
     		skills.add(skill1);
     		
             Set<SkillDTO> skillDTOs = new HashSet<>();
             skillDTOs.add(skill1);
             skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#createSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateSkill10() {
     

        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Skill>>any())).thenReturn(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);

     // Create a set of SkillDTO objects
     		Set<SkillDTO> skills = new HashSet<>();
     		SkillDTO skill1 = new SkillDTO("Skill1");
     		
     		skills.add(skill1);
     		
             Set<SkillDTO> skillDTOs = new HashSet<>();
             skillDTOs.add(skill1);
             skillServiceImpl.createSkill(skillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#readSkills()}
     */
    @Test
    void testReadSkills() {
        when(skillRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(skillServiceImpl.readSkills().isEmpty());
        verify(skillRepository).findAll();
    }

    /**
     * Method under test: {@link SkillServiceImpl#readSkills()}
     */
    @Test
    void testReadSkills2() {
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");

        ArrayList<Skill> skillList = new ArrayList<>();
        skillList.add(skill);
        when(skillRepository.findAll()).thenReturn(skillList);
        assertEquals(1, skillServiceImpl.readSkills().size());
        verify(skillRepository).findAll();
    }

    /**
     * Method under test: {@link SkillServiceImpl#readSkillsByEmployeeId(Long)}
     */
    @Test
    void testReadSkillsByEmployeeId() {
        when(skillRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        assertTrue(skillServiceImpl.readSkillsByEmployeeId(1L).isEmpty());
        verify(skillRepository).findAllByEmployeeId(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SkillServiceImpl#readSkillsByEmployeeId(Long)}
     */
    @Test
    void testReadSkillsByEmployeeId2() {
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");

        ArrayList<Skill> skillList = new ArrayList<>();
        skillList.add(skill);
        when(skillRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(skillList);
        assertEquals(1, skillServiceImpl.readSkillsByEmployeeId(1L).size());
        verify(skillRepository).findAllByEmployeeId(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SkillServiceImpl#updateSkill(Set)}
     */
    @Test
    void testUpdateSkill() {
        assertTrue(skillServiceImpl.updateSkill(new HashSet<>()).isEmpty());
    }

    /**
     * Method under test: {@link SkillServiceImpl#updateSkill(Set)}
     */
    @Test
    void testUpdateSkill2() {
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        Optional<Skill> ofResult = Optional.of(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);
        when(skillRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HashSet<SkillDTO> updatedSkillDTOs = new HashSet<>();
        updatedSkillDTOs.add(new SkillDTO());
        assertEquals(1, skillServiceImpl.updateSkill(updatedSkillDTOs).size());
        verify(skillRepository).save(Mockito.<Skill>any());
        verify(skillRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SkillServiceImpl#updateSkill(Set)}
     */
    @Test
    void testUpdateSkill3() {
        when(skillRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());

        HashSet<SkillDTO> updatedSkillDTOs = new HashSet<>();
        updatedSkillDTOs.add(new SkillDTO());
        assertTrue(skillServiceImpl.updateSkill(updatedSkillDTOs).isEmpty());
        verify(skillRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link SkillServiceImpl#updateSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateSkill4() {
       
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        Optional<Skill> ofResult = Optional.of(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);
        when(skillRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HashSet<SkillDTO> updatedSkillDTOs = new HashSet<>();
        updatedSkillDTOs.add(null);
        skillServiceImpl.updateSkill(updatedSkillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#updateSkill(Set)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateSkill5() {
      
        Skill skill = new Skill();
        skill.setEmployeeId(1L);
        skill.setId(1L);
        skill.setSkillName("Skill Name");
        Optional<Skill> ofResult = Optional.of(skill);

        Skill skill2 = new Skill();
        skill2.setEmployeeId(1L);
        skill2.setId(1L);
        skill2.setSkillName("Skill Name");
        when(skillRepository.save(Mockito.<Skill>any())).thenReturn(skill2);
        when(skillRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        HashSet<SkillDTO> updatedSkillDTOs = new HashSet<>();
        updatedSkillDTOs.add(new SkillDTO());
        updatedSkillDTOs.add(null);
        skillServiceImpl.updateSkill(updatedSkillDTOs);
    }

    /**
     * Method under test: {@link SkillServiceImpl#deleteSkill(Long)}
     */
    @Test
    void testDeleteSkill() {
        doNothing().when(skillRepository).deleteById(Mockito.<Long>any());
        skillServiceImpl.deleteSkill(1L);
        verify(skillRepository).deleteById(Mockito.<Long>any());
    }
}

