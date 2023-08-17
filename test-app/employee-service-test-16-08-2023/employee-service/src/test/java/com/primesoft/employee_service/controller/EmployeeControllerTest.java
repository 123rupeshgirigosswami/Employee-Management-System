package com.primesoft.employee_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.EmployeeDTO;
import com.primesoft.employee_service.payloads.SkillDTO;
import com.primesoft.employee_service.payloads.TaskDTO;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;
import com.primesoft.employee_service.service.EmployeeService;
import com.primesoft.employee_service.service.impl.EmployeeServiceImpl;

@ContextConfiguration(classes = { EmployeeController.class })
@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {
	@Autowired
	private EmployeeController employeeController;

	@MockBean
	private EmployeeService employeeService;

	/**
     * Method under test: {@link EmployeeController#addEmployee(EmployeeDTO, String, MultipartFile)}
     */
    @Test
    void testAddEmployee2() throws Exception {
        when(employeeService.getAllEmployeesAndTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	@Test
	void testAddEmployee1() throws Exception {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		// Set employeeDTO properties as needed

		Set<SkillDTO> skillSet = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Java");
		SkillDTO skill2 = new SkillDTO("Spring");
		skillSet.add(skill1);
		skillSet.add(skill2);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename.txt", MediaType.TEXT_PLAIN_VALUE,
				"file content".getBytes());

		EmployeeDTO addedEmployee = new EmployeeDTO();
		// Set addedEmployee properties as needed

		when(employeeService.addEmployee(employeeDTO, skillSet, mockMultipartFile)).thenReturn(addedEmployee);
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
		mockMvc.perform(MockMvcRequestBuilders.multipart("/employees").file(mockMultipartFile)
				.param("skills", "Java,Spring").contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
		// Add more assertions if needed
	}

	// downloadEmployeeDocument Start
	@Test
    void testDownloadEmployeeDocument() throws Exception {
        when(employeeService.downloadEmployeeDocument(Mockito.<Long>any())).thenReturn(null);
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(new EmployeeDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/document/{employeeId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	@Test
	void testDownloadEmployeeDocument1() throws Exception {
		Long employeeId = 1L;
		String fileName = "document.pdf";
		String fileType = "application/pdf";

		byte[] documentContent = new byte[] { 1, 2, 3 }; // Mock document content

		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setId(employeeId);
		employeeDTO.setFileName(fileName);
		employeeDTO.setFileType(fileType);

		when(employeeService.downloadEmployeeDocument(employeeId)).thenReturn(documentContent);
		when(employeeService.getEmployeeById(employeeId)).thenReturn(employeeDTO);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, fileType, documentContent);

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/document/{employeeId}", employeeId)
				.contentType(MediaType.APPLICATION_JSON).content(documentContent))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType(fileType)))
				.andExpect(MockMvcResultMatchers.content().bytes(documentContent));
	}

	// downloadEmployeeDocument End

	// getAllEmployeesWithoutTasks Start

	/**
     * Method under test: {@link EmployeeController#getAllEmployeesWithoutTasks(Integer, Integer)}
     */
    @Test
    void testGetAllEmployeesWithoutTasks() throws Exception {
        when(employeeService.getAllEmployeesWithoutTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/without-tasks");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getAllEmployeesWithoutTasks(Integer, Integer)}
     */
    @Test
    void testGetAllEmployeesWithoutTasks2() throws Exception {
        when(employeeService.getAllEmployeesWithoutTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/without-tasks");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	// getAllEmployeesWithoutTasks End

	// getEmployeeById Start

	/**
     * Method under test: {@link EmployeeController#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(new EmployeeDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/{id}", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"name\":null,\"designation\":null,\"email\":null,\"department\":null,\"hireDate\":null,\"fileName\""
                                        + ":null,\"fileType\":null,\"tasks\":[],\"parsedHireDate\":null}"));
    }

	/**
     * Method under test: {@link EmployeeController#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById2() throws Exception {
        when(employeeService.getEmployeeById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	/**
     * Method under test: {@link EmployeeController#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById3() throws Exception {
        when(employeeService.getAllEmployeesAndTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(new EmployeeDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/{id}", "", "Uri Variables");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getEmployeeById(Long)}
     */
    @Test
    void testGetEmployeeById4() throws Exception {
        when(employeeService.getAllEmployeesAndTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        when(employeeService.getEmployeeById(Mockito.<Long>any())).thenReturn(new EmployeeDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/{id}", "", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	/**
     * Method under test: {@link EmployeeController#addEmployee(EmployeeDTO, String, MultipartFile)}
     */
    @Test
    void testAddEmployee() throws Exception {
        when(employeeService.getAllEmployeesAndTasksWithPagination(Mockito.<Integer>any(), Mockito.<Integer>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	// getEmployeeById End

	// updateEmployee Start

	/**
	 * Method under test:
	 * {@link EmployeeController#updateEmployee(Long, EmployeeDTO, MultipartFile)}
	 */
	@Test
	void testUpdateEmployee() throws IOException {

		Employee employee = new Employee();
		employee.setDepartment("Department");
		employee.setDesignation("Designation");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("File Type");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Name");
		ArrayList<Task> tasks = new ArrayList<>();
		employee.setTasks(tasks);
		employee.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee);

		Employee employee2 = new Employee();
		employee2.setDepartment("Department");
		employee2.setDesignation("Designation");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("File Type");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Name");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeController employeeController = new EmployeeController(new EmployeeServiceImpl(employeeRepository,
				taskRepository, new ModelMapper(), mock(TimesheetRepository.class)));
		EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
		ResponseEntity<EmployeeDTO> actualUpdateEmployeeResult = employeeController.updateEmployee(1L,
				updatedEmployeeDTO, "Skill1,Skill2",
				new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
		assertTrue(actualUpdateEmployeeResult.hasBody());
		assertTrue(actualUpdateEmployeeResult.getHeaders().isEmpty());
		assertEquals(HttpStatus.OK, actualUpdateEmployeeResult.getStatusCode());
		EmployeeDTO body = actualUpdateEmployeeResult.getBody();
		assertEquals("Name", body.getName());
		assertEquals(1L, body.getId().longValue());
		assertEquals("1970-01-01", body.getHireDate());
		assertEquals("File Type", body.getFileType());
		assertEquals("foo.txt", body.getFileName());
		assertEquals("jane.doe@example.org", body.getEmail());
		assertEquals("Designation", body.getDesignation());
		assertEquals("Department", body.getDepartment());
		assertEquals(8, body.getUploadDocument().length);
		assertEquals(tasks, body.getTasks());
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	// updateEmployee End

	// deleteEmployee Start

	/**
	 * Method under test: {@link EmployeeController#deleteEmployee(Long)}
	 */
	@Test
	void testDeleteEmployee() throws Exception {
		doNothing().when(employeeService).deleteEmployeeById(Mockito.<Long>any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/{id}", 1L);
		MockMvcBuilders.standaloneSetup(employeeController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Employee with ID 1 has been successfully deleted."));
	}

	/**
	 * Method under test: {@link EmployeeController#deleteEmployee(Long)}
	 */
	@Test
	void testDeleteEmployee2() throws Exception {
		doThrow(new ResourceNotFoundException("?", "?", "Field Value")).when(employeeService)
				.deleteEmployeeById(Mockito.<Long>any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/{id}", 1L);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	/**
	 * Method under test: {@link EmployeeController#deleteEmployee(Long)}
	 */
	@Test
	void testDeleteEmployee3() throws Exception {
		doNothing().when(employeeService).deleteEmployeeById(Mockito.<Long>any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/{id}", 1L);
		requestBuilder.characterEncoding("Encoding");
		MockMvcBuilders.standaloneSetup(employeeController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
				.andExpect(MockMvcResultMatchers.content().string("Employee with ID 1 has been successfully deleted."));
	}

	// deleteEmployee End

	// addTaskForEmployee Start

	/**
	 * Method under test:
	 * {@link EmployeeController#addTaskForEmployee(Long, TaskDTO)}
	 */
	@Test
	void testAddTaskForEmployee() throws UnsupportedEncodingException {

		Employee employee = new Employee();
		employee.setDepartment("Department");
		employee.setDesignation("Designation");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("File Type");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Name");
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.of(employee));

		Employee employee2 = new Employee();
		employee2.setDepartment("Department");
		employee2.setDesignation("Designation");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("File Type");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Name");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		Employee employee3 = new Employee();
		employee3.setDepartment("Department");
		employee3.setDesignation("Designation");
		employee3.setEmail("jane.doe@example.org");
		employee3.setFileName("foo.txt");
		employee3.setFileType("File Type");
		employee3.setHireDate(LocalDate.of(1970, 1, 1));
		employee3.setId(1L);
		employee3.setName("Name");
		employee3.setTasks(new ArrayList<>());
		employee3.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		Timesheet timesheet = new Timesheet();
		timesheet.preUpdate();
		timesheet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setDescription("The characteristics of someone or something");
		timesheet.setEmployee(employee3);
		timesheet.setHours(10.0d);
		timesheet.setId(1L);
		timesheet.setTasks(new ArrayList<>());
		timesheet.setUpdatedBy("2020-03-01");
		timesheet.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setWorkStatus("Work Status");

		Task task = new Task();
		task.setDescriptions("Descriptions");
		task.setDueDate(LocalDate.of(1970, 1, 1));
		task.setEmployee(employee2);
		task.setId(1L);
		task.setStatus("Status");
		task.setTimesheet(timesheet);
		TaskRepository taskRepository = mock(TaskRepository.class);
		when(taskRepository.save(Mockito.<Task>any())).thenReturn(task);
		EmployeeController employeeController = new EmployeeController(new EmployeeServiceImpl(employeeRepository,
				taskRepository, new ModelMapper(), mock(TimesheetRepository.class)));

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setDescriptions("Descriptions");
		taskDTO.setDueDate("2020-03-01");
		taskDTO.setId(1L);
		taskDTO.setStatus("Status");
		ResponseEntity<TaskDTO> actualAddTaskForEmployeeResult = employeeController.addTaskForEmployee(1L, taskDTO);
		assertTrue(actualAddTaskForEmployeeResult.hasBody());
		assertTrue(actualAddTaskForEmployeeResult.getHeaders().isEmpty());
		assertEquals(HttpStatus.CREATED, actualAddTaskForEmployeeResult.getStatusCode());
		TaskDTO body = actualAddTaskForEmployeeResult.getBody();
		assertEquals("1970-01-01", body.getDueDate());
		assertEquals("Descriptions", body.getDescriptions());
		assertEquals("Status", body.getStatus());
		assertEquals(1L, body.getId().longValue());
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(taskRepository).save(Mockito.<Task>any());
	}

	// addTaskForEmployee End

	// getTasksByStatus Start

	/**
     * Method under test: {@link EmployeeController#getTasksByStatus(Long, String, Integer, Integer)}
     */
    @Test
    void testGetTasksByStatus() throws Exception {
        when(employeeService.getTasksByStatus(Mockito.<Long>any(), Mockito.<String>any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/employees/tasks/{employeeId}/{status}", 1L, "Status");
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	@Test
    void testGetTasksByStatus2() throws Exception {
        when(employeeService.getTasksByStatus(Mockito.<Long>any(), Mockito.<String>any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}/{status}", 1L,
                "Status");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getTasksByStatus(Long, String, Integer, Integer)}
     */
    @Test
    void testGetTasksByStatus3() throws Exception {
        when(employeeService.getTasksByStatus(Mockito.<Long>any(), Mockito.<String>any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}/{status}", 1L,
                "Status");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("pageSize", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getTasksByStatus(Long, String, Integer, Integer)}
     */
    @Test
    void testGetTasksByStatus4() throws Exception {
        when(employeeService.getTasksByStatus(Mockito.<Long>any(), Mockito.<String>any(), anyInt(), anyInt()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/employees/tasks/{employeeId}/{status}", 1L, "Status");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	// getTasksByStatus Start

	// updateEmployeeTasks Start

	/**
	 * Method under test: {@link EmployeeController#updateEmployeeTasks(Long, List)}
	 */
	@Test
	void testUpdateEmployeeTasks() throws UnsupportedEncodingException {

		Employee employee = new Employee();
		employee.setDepartment("Department");
		employee.setDesignation("Designation");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("File Type");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Name");
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee);

		Employee employee2 = new Employee();
		employee2.setDepartment("Department");
		employee2.setDesignation("Designation");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("File Type");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Name");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeController employeeController = new EmployeeController(new EmployeeServiceImpl(employeeRepository,
				taskRepository, new ModelMapper(), mock(TimesheetRepository.class)));
		ArrayList<TaskDTO> taskDTOList = new ArrayList<>();
		ResponseEntity<EmployeeDTO> actualUpdateEmployeeTasksResult = employeeController.updateEmployeeTasks(1L,
				taskDTOList);
		assertTrue(actualUpdateEmployeeTasksResult.hasBody());
		assertTrue(actualUpdateEmployeeTasksResult.getHeaders().isEmpty());
		assertEquals(HttpStatus.OK, actualUpdateEmployeeTasksResult.getStatusCode());
		EmployeeDTO body = actualUpdateEmployeeTasksResult.getBody();
		assertEquals("Name", body.getName());
		assertEquals(1L, body.getId().longValue());
		assertEquals("1970-01-01", body.getHireDate());
		assertEquals("File Type", body.getFileType());
		assertEquals("foo.txt", body.getFileName());
		assertEquals("jane.doe@example.org", body.getEmail());
		assertEquals("Designation", body.getDesignation());
		assertEquals("Department", body.getDepartment());
		assertEquals(8, body.getUploadDocument().length);
		assertEquals(taskDTOList, body.getTasks());
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
		assertEquals(actualUpdateEmployeeTasksResult, employeeController.getEmployeeById(1L));
	}

	/**
	 * Method under test: {@link EmployeeController#updateEmployeeTasks(Long, List)}
	 */
	@Test
	void testUpdateEmployeeTasks3() throws UnsupportedEncodingException {

		Employee employee = new Employee();
		employee.setDepartment("Department");
		employee.setDesignation("Designation");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("File Type");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Name");
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee);

		Employee employee2 = new Employee();
		employee2.setDepartment("Department");
		employee2.setDesignation("Designation");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("File Type");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Name");
		ArrayList<Task> tasks = new ArrayList<>();
		employee2.setTasks(tasks);
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeController employeeController = new EmployeeController(new EmployeeServiceImpl(employeeRepository,
				taskRepository, new ModelMapper(), mock(TimesheetRepository.class)));

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setDescriptions("Updating tasks for Employee ID {}: {}");
		taskDTO.setDueDate("2020-03-01");
		taskDTO.setId(1L);
		taskDTO.setStatus("Updating tasks for Employee ID {}: {}");

		ArrayList<TaskDTO> taskDTOList = new ArrayList<>();
		taskDTOList.add(taskDTO);
		ResponseEntity<EmployeeDTO> actualUpdateEmployeeTasksResult = employeeController.updateEmployeeTasks(1L,
				taskDTOList);
		assertTrue(actualUpdateEmployeeTasksResult.hasBody());
		assertTrue(actualUpdateEmployeeTasksResult.getHeaders().isEmpty());
		assertEquals(HttpStatus.OK, actualUpdateEmployeeTasksResult.getStatusCode());
		EmployeeDTO body = actualUpdateEmployeeTasksResult.getBody();
		assertEquals("Name", body.getName());
		assertEquals(1L, body.getId().longValue());
		assertEquals("1970-01-01", body.getHireDate());
		assertEquals("File Type", body.getFileType());
		assertEquals("foo.txt", body.getFileName());
		assertEquals("jane.doe@example.org", body.getEmail());
		assertEquals("Designation", body.getDesignation());
		assertEquals("Department", body.getDepartment());
		assertEquals(8, body.getUploadDocument().length);
		assertEquals(tasks, body.getTasks());
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	// updateEmployeeTasks End

	// getAllTaskForEmployeeById Start

	/**
     * Method under test: {@link EmployeeController#getAllTaskForEmployeeById(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTaskForEmployeeById() throws Exception {
        when(employeeService.getAllTaskForEmployeeById(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getAllTaskForEmployeeById(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTaskForEmployeeById2() throws Exception {
        when(employeeService.getAllTaskForEmployeeById(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getAllTaskForEmployeeById(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTaskForEmployeeById3() throws Exception {
        when(employeeService.getAllTaskForEmployeeById(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = getResult.param("pageSize", String.valueOf(1));
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	/**
     * Method under test: {@link EmployeeController#getAllTaskForEmployeeById(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTaskForEmployeeById4() throws Exception {
        when(employeeService.getAllTaskForEmployeeById(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/tasks/{employeeId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee with ID 1 not found."));
    }

	// getAllTaskForEmployeeById End

	// getAllTasksForEmployeeWithoutIds Start

	/**
     * Method under test: {@link EmployeeController#getAllTasksForEmployeeWithoutIds(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTasksForEmployeeWithoutIds2() throws Exception {
        when(employeeService.getAllTasksForEmployeeWithoutIds(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/employees/tasks-without-ids/{employeeId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee with ID 1 not found."));
    }

	/**
     * Method under test: {@link EmployeeController#getAllTasksForEmployeeWithoutIds(Long, Integer, Integer)}
     */
    @Test
    void testGetAllTasksForEmployeeWithoutIds() throws Exception {
        when(employeeService.getAllTasksForEmployeeWithoutIds(Mockito.<Long>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/employees/tasks-without-ids/{employeeId}", 1L);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":[],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":0,\"size\":0,\"number"
                                        + "\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty"
                                        + "\":true}"));
    }

	// getAllTasksForEmployeeWithoutIds End

}
