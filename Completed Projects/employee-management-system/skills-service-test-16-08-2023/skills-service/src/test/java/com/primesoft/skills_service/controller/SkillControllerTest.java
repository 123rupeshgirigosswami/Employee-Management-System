package com.primesoft.skills_service.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primesoft.skills_service.payloads.SkillDTO;
import com.primesoft.skills_service.service.SkillService;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {SkillController.class})
@ExtendWith(SpringExtension.class)
class SkillControllerTest {
    @Autowired
    private SkillController skillController;

    @MockBean
    private SkillService skillService;

    /**
     * Method under test: {@link SkillController#createSkills(Set)}
     */
    @Test
    void testCreateSkills() throws Exception {
        when(skillService.createSkill(Mockito.<Set<SkillDTO>>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/skills/createSkills")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link SkillController#deleteSkill(Long)}
     */
    @Test
    void testDeleteSkill() throws Exception {
        doNothing().when(skillService).deleteSkill(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/skills/delete/{skillId}", 1L);
        MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link SkillController#deleteSkill(Long)}
     */
    @Test
    void testDeleteSkill2() throws Exception {
        doNothing().when(skillService).deleteSkill(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/skills/delete/{skillId}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link SkillController#getAllSkills()}
     */
    @Test
    void testGetAllSkills() throws Exception {
        when(skillService.readSkills()).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/skills/all");
        MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link SkillController#getSkillsByEmployeeId(Long)}
     */
    @Test
    void testGetSkillsByEmployeeId() throws Exception {
        when(skillService.readSkillsByEmployeeId(Mockito.<Long>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/skills/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link SkillController#updateSkills(Set)}
     */
    @Test
    void testUpdateSkills() throws Exception {
        when(skillService.updateSkill(Mockito.<Set<SkillDTO>>any())).thenReturn(new HashSet<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/skills/update")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashSet<>()));
        MockMvcBuilders.standaloneSetup(skillController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}

