package com.primesoft.employee_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primesoft.employee_service.exception.ErrorResponse;
import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.TimesheetDto;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;
import com.primesoft.employee_service.service.TimesheetService;
import com.primesoft.employee_service.service.impl.TimesheetServiceImpl;

@ContextConfiguration(classes = { TimesheetController.class })
@ExtendWith(SpringExtension.class)
class TimesheetControllerTest {
	@Autowired
	private TimesheetController timesheetController;

	@MockBean
	private TimesheetService timesheetService;

	/**
	 * Method under test:
	 * {@link TimesheetController#createTimesheet(Long, TimesheetDto)}
	 */
	@Test
	void testCreateTimesheet() throws Exception {
		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(timesheetService.createTimesheet(Mockito.<Long>any(), Mockito.<TimesheetDto>any()))
				.thenReturn(timesheetDto);

		TimesheetDto timesheetDto2 = new TimesheetDto();
		timesheetDto2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto2.setDescription("The characteristics of someone or something");
		timesheetDto2.setHours(10.0d);
		timesheetDto2.setId(1L);
		timesheetDto2.setTaskIds(new ArrayList<>());
		timesheetDto2.setUpdatedBy("2020-03-01");
		timesheetDto2.setWorkStatus("Work Status");
		String content = (new ObjectMapper()).writeValueAsString(timesheetDto2);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/timesheets/{employeeId}", 1L)
				.contentType(MediaType.APPLICATION_JSON).content(content);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"id\":1,\"description\":\"The characteristics of someone or something\",\"hours\":10.0,\"workStatus\":\"Work"
								+ " Status\",\"createdBy\":\"Jan 1, 2020 8:00am GMT+0100\",\"updatedBy\":\"2020-03-01\"}"));
	}

	/**
     * Method under test: {@link TimesheetController#createTimesheet(Long, TimesheetDto)}
     */
    @Test
    void testCreateTimesheet2() throws Exception {
        when(timesheetService.createTimesheet(Mockito.<Long>any(), Mockito.<TimesheetDto>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));

        TimesheetDto timesheetDto = new TimesheetDto();
        timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        timesheetDto.setDescription("The characteristics of someone or something");
        timesheetDto.setHours(10.0d);
        timesheetDto.setId(1L);
        timesheetDto.setTaskIds(new ArrayList<>());
        timesheetDto.setUpdatedBy("2020-03-01");
        timesheetDto.setWorkStatus("Work Status");
        String content = (new ObjectMapper()).writeValueAsString(timesheetDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/timesheets/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

	/**
	 * Method under test: {@link TimesheetController#getTimesheetById(Long)}
	 */
	@Test
	void testGetTimesheetById() throws Exception {
		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(timesheetService.getTimesheetById(Mockito.<Long>any())).thenReturn(timesheetDto);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/timesheets/{timesheetId}", 1L);
		MockMvcBuilders.standaloneSetup(timesheetController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"id\":1,\"description\":\"The characteristics of someone or something\",\"hours\":10.0,\"workStatus\":\"Work"
								+ " Status\",\"createdBy\":\"Jan 1, 2020 8:00am GMT+0100\",\"updatedBy\":\"2020-03-01\"}"));
	}

	/**
     * Method under test: {@link TimesheetController#getTimesheetById(Long)}
     */
    @Test
    void testGetTimesheetById2() throws Exception {
        when(timesheetService.getTimesheetById(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/timesheets/{timesheetId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("? not found with ?: 'Field Value'"));
    }

	/**
	 * Method under test:
	 * {@link TimesheetController#updateTimesheet(Long, TimesheetDto)}
	 */
	@Test
	void testUpdateTimesheet() throws Exception {
		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(timesheetService.updateTimesheet(Mockito.<Long>any(), Mockito.<TimesheetDto>any()))
				.thenReturn(timesheetDto);

		TimesheetDto timesheetDto2 = new TimesheetDto();
		timesheetDto2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto2.setDescription("The characteristics of someone or something");
		timesheetDto2.setHours(10.0d);
		timesheetDto2.setId(1L);
		timesheetDto2.setTaskIds(new ArrayList<>());
		timesheetDto2.setUpdatedBy("2020-03-01");
		timesheetDto2.setWorkStatus("Work Status");
		String content = (new ObjectMapper()).writeValueAsString(timesheetDto2);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/timesheets/{timesheetId}", 1L)
				.contentType(MediaType.APPLICATION_JSON).content(content);
		MockMvcBuilders.standaloneSetup(timesheetController).build().perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"id\":1,\"description\":\"The characteristics of someone or something\",\"hours\":10.0,\"workStatus\":\"Work"
								+ " Status\",\"createdBy\":\"Jan 1, 2020 8:00am GMT+0100\",\"updatedBy\":\"2020-03-01\"}"));
	}

	/**
     * Method under test: {@link TimesheetController#updateTimesheet(Long, TimesheetDto)}
     */
    @Test
    void testUpdateTimesheet2() throws Exception {
        when(timesheetService.updateTimesheet(Mockito.<Long>any(), Mockito.<TimesheetDto>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));

        TimesheetDto timesheetDto = new TimesheetDto();
        timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        timesheetDto.setDescription("The characteristics of someone or something");
        timesheetDto.setHours(10.0d);
        timesheetDto.setId(1L);
        timesheetDto.setTaskIds(new ArrayList<>());
        timesheetDto.setUpdatedBy("2020-03-01");
        timesheetDto.setWorkStatus("Work Status");
        String content = (new ObjectMapper()).writeValueAsString(timesheetDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/timesheets/{timesheetId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("? not found with ?: 'Field Value'"));
    }

	/**
	 * Method under test: {@link TimesheetController#deleteTimesheet(Long)}
	 */
	@Test
	void testDeleteTimesheet() throws Exception {
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

		Timesheet timesheet = new Timesheet();
		timesheet.preUpdate();
		timesheet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setDescription("The characteristics of someone or something");
		timesheet.setEmployee(employee);
		timesheet.setHours(10.0d);
		timesheet.setId(1L);
		timesheet.setTasks(new ArrayList<>());
		timesheet.setUpdatedBy("2020-03-01");
		timesheet.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setWorkStatus("Work Status");
		when(timesheetService.deleteTimesheet(Mockito.<Long>any())).thenReturn(timesheet);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/timesheets/{timesheetId}",
				1L);
		ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController).build()
				.perform(requestBuilder);
		actualPerformResult.andExpect(MockMvcResultMatchers.status().isAccepted())
				.andExpect(MockMvcResultMatchers.content().contentType("application/json"))
				.andExpect(MockMvcResultMatchers.content().string(
						"{\"success\":true,\"message\":\"Timesheet with ID 1 of employee 1 has been deleted.\"}"));
	}

	/**
     * Method under test: {@link TimesheetController#deleteTimesheet(Long)}
     */
    @Test
    void testDeleteTimesheet2() throws Exception {
        when(timesheetService.deleteTimesheet(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/timesheets/{timesheetId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"success\":false,\"message\":\"? not found with ?: 'Field Value'\"}"));
    }

	/**
	 * Method under test: {@link TimesheetController#getAllTimesheets(Pageable)}
	 */
	@Test
	void testGetAllTimesheets() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetRepository timesheetRepository = mock(TimesheetRepository.class);
		when(timesheetRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		ResponseEntity<Page<TimesheetDto>> actualAllTimesheets = (new TimesheetController(
				new TimesheetServiceImpl(timesheetRepository, taskRepository, employeeRepository, new ModelMapper())))
				.getAllTimesheets(null);
		assertTrue(actualAllTimesheets.hasBody());
		assertTrue(actualAllTimesheets.getBody().toList().isEmpty());
		assertEquals(HttpStatus.OK, actualAllTimesheets.getStatusCode());
		assertTrue(actualAllTimesheets.getHeaders().isEmpty());
		verify(timesheetRepository).findAll(Mockito.<Pageable>any());
	}

	/**
	 * Method under test: {@link TimesheetController#getAllTimesheets(Pageable)}
	 */
	@Test
	@Disabled("TODO: Complete this test")
	void testGetAllTimesheets2() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		// TODO: Complete this test.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.NullPointerException
		// at
		// com.primesoft.employee_service.service.impl.TimesheetServiceImpl.getAllTimesheets(TimesheetServiceImpl.java:118)
		// at
		// com.primesoft.employee_service.controller.TimesheetController.getAllTimesheets(TimesheetController.java:87)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetRepository timesheetRepository = mock(TimesheetRepository.class);
		when(timesheetRepository.findAll(Mockito.<Pageable>any())).thenReturn(null);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		(new TimesheetController(
				new TimesheetServiceImpl(timesheetRepository, taskRepository, employeeRepository, new ModelMapper())))
				.getAllTimesheets(null);
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetRepository timesheetRepository = mock(TimesheetRepository.class);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(
				new TimesheetServiceImpl(timesheetRepository, taskRepository, employeeRepository, new ModelMapper())))
				.getAllByFromDateAndToDate("foo", "foo", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		assertEquals(HttpStatus.BAD_REQUEST, actualAllByFromDateAndToDate.getStatusCode());
		assertEquals("Invalid date format. Dates must be in the format yyyy-MM-dd.",
				((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getMessage());
		assertEquals(400, ((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getStatus());
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate2() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetRepository timesheetRepository = mock(TimesheetRepository.class);
		when(timesheetRepository.findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(
				new TimesheetServiceImpl(timesheetRepository, taskRepository, employeeRepository, new ModelMapper())))
				.getAllByFromDateAndToDate("2020-03-01", "2020-03-01", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(((PageImpl<Object>) actualAllByFromDateAndToDate.getBody()).toList().isEmpty());
		assertEquals(HttpStatus.OK, actualAllByFromDateAndToDate.getStatusCode());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		verify(timesheetRepository).findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate3() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetRepository timesheetRepository = mock(TimesheetRepository.class);
		when(timesheetRepository.findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any())).thenReturn(null);
		TaskRepository taskRepository = mock(TaskRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(
				new TimesheetServiceImpl(timesheetRepository, taskRepository, employeeRepository, new ModelMapper())))
				.getAllByFromDateAndToDate("2020-03-01", "2020-03-01", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualAllByFromDateAndToDate.getStatusCode());
		assertEquals("An unexpected error occurred",
				((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getMessage());
		assertEquals(500, ((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getStatus());
		verify(timesheetRepository).findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate4() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(null))
				.getAllByFromDateAndToDate("2020-03-01", "2020-03-01", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualAllByFromDateAndToDate.getStatusCode());
		assertEquals("An unexpected error occurred",
				((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getMessage());
		assertEquals(500, ((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getStatus());
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate5() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		TimesheetService timesheetService = mock(TimesheetService.class);
		when(timesheetService.getAllByFromDateAndToDate(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(),
				Mockito.<Pageable>any())).thenThrow(new DateTimeParseException("foo", System.lineSeparator(), 1));
		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(timesheetService))
				.getAllByFromDateAndToDate("2020-03-01", "2020-03-01", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		assertEquals(HttpStatus.BAD_REQUEST, actualAllByFromDateAndToDate.getStatusCode());
		assertEquals("Invalid date format. Dates must be in the format yyyy-MM-dd.",
				((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getMessage());
		assertEquals(400, ((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getStatus());
		verify(timesheetService).getAllByFromDateAndToDate(Mockito.<LocalDate>any(), Mockito.<LocalDate>any(),
				Mockito.<Pageable>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetController#getAllByFromDateAndToDate(String, String, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate6() {
		// Diffblue Cover was unable to write a Spring test,
		// so wrote a non-Spring test instead.
		// Reason: R013 No inputs found that don't throw a trivial exception.
		// Diffblue Cover tried to run the arrange/act section, but the method under
		// test threw
		// java.lang.IllegalStateException: No primary or single unique constructor
		// found for interface org.springframework.data.domain.Pageable
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:529)
		// at javax.servlet.http.HttpServlet.service(HttpServlet.java:623)
		// See https://diff.blue/R013 to resolve this issue.

		ResponseEntity<?> actualAllByFromDateAndToDate = (new TimesheetController(mock(TimesheetService.class)))
				.getAllByFromDateAndToDate("2020-03-01", "2020/03/01", null);
		assertTrue(actualAllByFromDateAndToDate.hasBody());
		assertTrue(actualAllByFromDateAndToDate.getHeaders().isEmpty());
		assertEquals(HttpStatus.BAD_REQUEST, actualAllByFromDateAndToDate.getStatusCode());
		assertEquals("Invalid date format. Dates must be in the format yyyy-MM-dd.",
				((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getMessage());
		assertEquals(400, ((ErrorResponse) actualAllByFromDateAndToDate.getBody()).getStatus());
	}

	/**
     * Method under test: {@link TimesheetController#getAllTimesheetsByEmployeeId(Long)}
     */
    @Test
    void testGetAllTimesheetsByEmployeeId() throws Exception {
        when(timesheetService.getAllTimesheetsByEmployeeId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/timesheets/employee/{employeeId}",
                1L);
        MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

	/**
     * Method under test: {@link TimesheetController#getAllTimesheetsByEmployeeId(Long)}
     */
    @Test
    void testGetAllTimesheetsByEmployeeId2() throws Exception {
        when(timesheetService.getAllTimesheetsByEmployeeId(Mockito.<Long>any()))
                .thenThrow(new ResourceNotFoundException("?", "?", "Field Value"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/timesheets/employee/{employeeId}",
                1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(timesheetController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
