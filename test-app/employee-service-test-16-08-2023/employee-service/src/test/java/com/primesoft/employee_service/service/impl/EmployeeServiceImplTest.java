package com.primesoft.employee_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.EmployeeDTO;
import com.primesoft.employee_service.payloads.EmployeeDTOWithOutTasks;
import com.primesoft.employee_service.payloads.SkillDTO;
import com.primesoft.employee_service.payloads.TaskDTO;
import com.primesoft.employee_service.payloads.TaskDTOWithoutTaskID;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;

@ContextConfiguration(classes = { EmployeeServiceImpl.class })
@ExtendWith(SpringExtension.class)
class EmployeeServiceImplTest {
	@MockBean
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeServiceImpl employeeServiceImpl;

	@MockBean
	private ModelMapper modelMapper;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private TaskRepository taskRepository;

	@MockBean
	private TimesheetRepository timesheetRepository;

	private static final String SKILL_SERVICE_URL = "http://localhost:9092/skills";

	// AddEmployee Start

	/**
     * Method under test: {@link EmployeeServiceImpl#addEmployee(EmployeeDTO, Set, MultipartFile)}
     */
    @Test
    void testAddEmployee() throws IOException {
        when(employeeRepository.save(Mockito.<Employee>any())).thenThrow(new RuntimeException("Adding a new employee: {}"));

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
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Employee>>any())).thenReturn(employee);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        HashSet<SkillDTO> skills = new HashSet<>();
        assertThrows(RuntimeException.class, () -> employeeServiceImpl.addEmployee(employeeDTO, skills,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
        verify(employeeRepository).save(Mockito.<Employee>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Employee>>any());
    }

	@Test
	void testAddEmployeeWithSkillsSuccess() {
		// Arrange
		EmployeeDTO employeeDTO = new EmployeeDTO();
		Set<SkillDTO> skills = new HashSet<>();
		skills.add(new SkillDTO());

		Employee addedEmployee = new Employee();
		addedEmployee.setId(1L);

		ResponseEntity<Object> responseEntity = new ResponseEntity<>(HttpStatus.CREATED);
		when(restTemplate.exchange(eq(SKILL_SERVICE_URL + "/createSkills"), eq(HttpMethod.POST), any(HttpEntity.class),
				eq(Object.class))).thenReturn(responseEntity);

		when(modelMapper.map(any(EmployeeDTO.class), eq(Employee.class))).thenReturn(addedEmployee);
		when(employeeRepository.save(any(Employee.class))).thenReturn(addedEmployee);

		// Act
		assertDoesNotThrow(() -> employeeServiceImpl.addEmployee(employeeDTO, skills, null));

		// Assert
		verify(restTemplate, times(1)).exchange(eq(SKILL_SERVICE_URL + "/createSkills"), eq(HttpMethod.POST),
				any(HttpEntity.class), eq(Object.class));
	}

	// Add Employee End

	// downloadEmployeeDocument Start

	/**
	 * Method under test: {@link EmployeeServiceImpl#downloadEmployeeDocument(Long)}
	 */
	@Test
	void testDownloadEmployeeDocument() throws UnsupportedEncodingException {
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
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		byte[] actualDownloadEmployeeDocumentResult = employeeServiceImpl.downloadEmployeeDocument(1L);
		assertEquals(8, actualDownloadEmployeeDocumentResult.length);
		assertEquals('A', actualDownloadEmployeeDocumentResult[0]);
		assertEquals('X', actualDownloadEmployeeDocumentResult[1]);
		assertEquals('A', actualDownloadEmployeeDocumentResult[2]);
		assertEquals('X', actualDownloadEmployeeDocumentResult[3]);
		assertEquals('A', actualDownloadEmployeeDocumentResult[4]);
		assertEquals('X', actualDownloadEmployeeDocumentResult[5]);
		assertEquals('A', actualDownloadEmployeeDocumentResult[6]);
		assertEquals('X', actualDownloadEmployeeDocumentResult[7]);
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	/**
     * Method under test: {@link EmployeeServiceImpl#downloadEmployeeDocument(Long)}
     */
    @Test
    void testDownloadEmployeeDocument2() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertNull(employeeServiceImpl.downloadEmployeeDocument(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

	/**
     * Method under test: {@link EmployeeServiceImpl#downloadEmployeeDocument(Long)}
     */
    @Test
    void testDownloadEmployeeDocument3() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new RuntimeException("Fetching document content for employee with ID: {}"));
        assertThrows(RuntimeException.class, () -> employeeServiceImpl.downloadEmployeeDocument(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

	// downloadEmployeeDocument End

	// getAllEmployeesAndTasksWithPagination Start

	/**
     * Method under test: {@link EmployeeServiceImpl#getAllEmployeesAndTasksWithPagination(Integer, Integer)}
     */
    @Test
    void testGetAllEmployeesAndTasksWithPagination() {
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(employeeServiceImpl.getAllEmployeesAndTasksWithPagination(1, 3).toList().isEmpty());
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
    }

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllEmployeesAndTasksWithPagination(Integer, Integer)}
	 */
	@Test
	void testGetAllEmployeesAndTasksWithPagination2() throws UnsupportedEncodingException {
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

		ArrayList<Employee> content = new ArrayList<>();
		content.add(employee);
		PageImpl<Employee> pageImpl = new PageImpl<>(content);
		when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any())).thenReturn(new EmployeeDTO());
		assertEquals(1, employeeServiceImpl.getAllEmployeesAndTasksWithPagination(1, 3).toList().size());
		verify(employeeRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllEmployeesAndTasksWithPagination(Integer, Integer)}
	 */
	@Test
	void testGetAllEmployeesAndTasksWithPagination3() throws UnsupportedEncodingException {
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

		Employee employee2 = new Employee();
		employee2.setDepartment("com.primesoft.employee_service.model.Employee");
		employee2.setDesignation("com.primesoft.employee_service.model.Employee");
		employee2.setEmail("john.smith@example.org");
		employee2.setFileName("File Name");
		employee2.setFileType("com.primesoft.employee_service.model.Employee");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(2L);
		employee2.setName("com.primesoft.employee_service.model.Employee");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		ArrayList<Employee> content = new ArrayList<>();
		content.add(employee2);
		content.add(employee);
		PageImpl<Employee> pageImpl = new PageImpl<>(content);
		when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any())).thenReturn(new EmployeeDTO());
		assertEquals(2, employeeServiceImpl.getAllEmployeesAndTasksWithPagination(1, 3).toList().size());
		verify(employeeRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	// getAllEmployeesAndTasksWithPagination End

	// getAllEmployeesWithoutTasksWithPagination Start

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllEmployeesWithoutTasksWithPagination(Integer, Integer)}
	 */
	@Test
	void testGetAllEmployeesWithoutTasksWithPagination3() throws UnsupportedEncodingException {
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

		Employee employee2 = new Employee();
		employee2.setDepartment("com.primesoft.employee_service.model.Employee");
		employee2.setDesignation("com.primesoft.employee_service.model.Employee");
		employee2.setEmail("john.smith@example.org");
		employee2.setFileName("File Name");
		employee2.setFileType("com.primesoft.employee_service.model.Employee");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(2L);
		employee2.setName("com.primesoft.employee_service.model.Employee");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		ArrayList<Employee> content = new ArrayList<>();
		content.add(employee2);
		content.add(employee);
		PageImpl<Employee> pageImpl = new PageImpl<>(content);
		when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTOWithOutTasks>>any()))
				.thenReturn(new EmployeeDTOWithOutTasks());
		assertEquals(2, employeeServiceImpl.getAllEmployeesWithoutTasksWithPagination(1, 3).toList().size());
		verify(employeeRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTOWithOutTasks>>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllEmployeesWithoutTasksWithPagination(Integer, Integer)}
	 */
	@Test
	void testGetAllEmployeesWithoutTasksWithPagination2() throws UnsupportedEncodingException {
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

		ArrayList<Employee> content = new ArrayList<>();
		content.add(employee);
		PageImpl<Employee> pageImpl = new PageImpl<>(content);
		when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTOWithOutTasks>>any()))
				.thenReturn(new EmployeeDTOWithOutTasks());
		assertEquals(1, employeeServiceImpl.getAllEmployeesWithoutTasksWithPagination(1, 3).toList().size());
		verify(employeeRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTOWithOutTasks>>any());
	}

/**
     * Method under test: {@link EmployeeServiceImpl#getAllEmployeesWithoutTasksWithPagination(Integer, Integer)}
     */
    @Test
    void testGetAllEmployeesWithoutTasksWithPagination() {
        when(employeeRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(employeeServiceImpl.getAllEmployeesWithoutTasksWithPagination(1, 3).toList().isEmpty());
        verify(employeeRepository).findAll(Mockito.<Pageable>any());
    }

	// getAllEmployeesWithoutTasksWithPagination End

	// getEmployeeById Start

	/**
	 * Method under test: {@link EmployeeServiceImpl#getEmployeeById(Long)}
	 */
	@Test
	void testGetEmployeeById() throws UnsupportedEncodingException {
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
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		EmployeeDTO employeeDTO = new EmployeeDTO();
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any())).thenReturn(employeeDTO);
		assertSame(employeeDTO, employeeServiceImpl.getEmployeeById(1L));
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	/**
	 * Method under test: {@link EmployeeServiceImpl#getEmployeeById(Long)}
	 */
	@Test
	void testGetEmployeeById2() throws UnsupportedEncodingException {
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
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any()))
				.thenThrow(new RuntimeException("Fetching employee by ID: {}"));
		assertThrows(RuntimeException.class, () -> employeeServiceImpl.getEmployeeById(1L));
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	// getEmployeeById End

	// updateEmployee Start

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#updateEmployee(Long, EmployeeDTO, MultipartFile)}
	 */
	@Test
	void testUpdateEmployee2() throws IOException {
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

		// Create a set of SkillDTO objects
		Set<SkillDTO> skills = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Skill1");
		SkillDTO skill2 = new SkillDTO("Skill2");
		skills.add(skill1);
		skills.add(skill2);

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
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any()))
				.thenThrow(new RuntimeException("Updating employee with ID {}: {}"));
		EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
		assertThrows(RuntimeException.class, () -> employeeServiceImpl.updateEmployee(1L, updatedEmployeeDTO, skills,
				new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#updateEmployee(Long, EmployeeDTO, MultipartFile)}
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
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee);

		// Create a set of SkillDTO objects
		Set<SkillDTO> skills = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Skill1");
		SkillDTO skill2 = new SkillDTO("Skill2");
		skills.add(skill1);
		skills.add(skill2);

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
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		EmployeeDTO employeeDTO = new EmployeeDTO();
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any())).thenReturn(employeeDTO);
		EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
		assertSame(employeeDTO, employeeServiceImpl.updateEmployee(1L, updatedEmployeeDTO, skills,
				new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	@Test
	public void testUpdateEmployeeWithTasks() throws IOException {
		// Mock input data
		Long employeeId = 1L;
		EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
		List<TaskDTO> updatedTasks = new ArrayList<>();
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
		taskDTO.setStatus("Completed");
		updatedTasks.add(taskDTO);
		updatedEmployeeDTO.setTasks(updatedTasks);

		// Create a set of SkillDTO objects
		Set<SkillDTO> skills = new HashSet<>();
		SkillDTO skill1 = new SkillDTO("Skill1");
		SkillDTO skill2 = new SkillDTO("Skill2");
		skills.add(skill1);
		skills.add(skill2);

		// Mock existing employee
		Employee existingEmployee = new Employee();
		existingEmployee.setId(employeeId);
		existingEmployee.setName("John");
		// ... set other properties

		// Mock existing task
		Task existingTask = new Task();
		existingTask.setId(1L);

		// Mock repository responses
		Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
		Mockito.when(taskRepository.findById(taskDTO.getId())).thenReturn(Optional.of(existingTask));

		// Perform the update
		EmployeeDTO resultDTO = employeeServiceImpl.updateEmployee(employeeId, updatedEmployeeDTO, skills, null);

		// Verify that the status of the task is updated
		verify(taskRepository, times(1)).save(existingTask);
		assertEquals("Completed", existingTask.getStatus());

		// Verify that the updated employee is saved
		verify(employeeRepository, times(1)).save(existingEmployee);

	}

	// updateEmployee End

	// deleteEmployeeById Start

	/**
     * Method under test: {@link EmployeeServiceImpl#deleteEmployeeById(Long)}
     */
    @Test
    void testDeleteEmployeeById3() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.deleteEmployeeById(1L));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

	/**
	 * Method under test: {@link EmployeeServiceImpl#deleteEmployeeById(Long)}
	 */
	@Test
	void testDeleteEmployeeById2() throws UnsupportedEncodingException {
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
		doThrow(new RuntimeException("Deleting employee with ID: {}")).when(employeeRepository)
				.delete(Mockito.<Employee>any());
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertThrows(RuntimeException.class, () -> employeeServiceImpl.deleteEmployeeById(1L));
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(employeeRepository).delete(Mockito.<Employee>any());
	}

	/**
	 * Method under test: {@link EmployeeServiceImpl#deleteEmployeeById(Long)}
	 */
	@Test
	void testDeleteEmployeeById() throws UnsupportedEncodingException {
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
		doNothing().when(employeeRepository).delete(Mockito.<Employee>any());
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		employeeServiceImpl.deleteEmployeeById(1L);
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(employeeRepository).delete(Mockito.<Employee>any());
	}

	// deleteEmployeeById End

	// addTaskForEmployee Start
	// Tested All Covered
	@Test
	void testAddTaskForEmployeeValidInput() {
		// Arrange
		Long employeeId = 1L;
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(1L);
		Employee employee = new Employee();
		employee.setId(employeeId);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
		when(modelMapper.map(taskDTO, Task.class)).thenReturn(new Task());
		when(taskRepository.save(any(Task.class))).thenReturn(new Task());
		when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(new TaskDTO());

		// Act
		TaskDTO result = employeeServiceImpl.addTaskForEmployee(employeeId, taskDTO);

		// Assert
		assertNotNull(result);
		verify(employeeRepository, times(1)).findById(employeeId);
		verify(modelMapper, times(1)).map(taskDTO, Task.class);
		verify(taskRepository, times(1)).save(any(Task.class));
		verify(modelMapper, times(1)).map(any(Task.class), eq(TaskDTO.class));
	}

	@Test
	void testAddTaskForEmployeeInvalidEmployeeId() {
		// Arrange
		Long employeeId = null;
		TaskDTO taskDTO = new TaskDTO();

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> employeeServiceImpl.addTaskForEmployee(employeeId, taskDTO));
	}

	@Test
	void testAddTaskForEmployeeEmployeeNotFound() {
		// Arrange
		Long employeeId = 1L;
		TaskDTO taskDTO = new TaskDTO();

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class,
				() -> employeeServiceImpl.addTaskForEmployee(employeeId, taskDTO));
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#addTaskForEmployee(Long, TaskDTO)}
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
		Optional<Employee> ofResult = Optional.of(employee);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(taskRepository.save(Mockito.<Task>any()))
				.thenThrow(new RuntimeException("Adding a new task for Employee ID {}: {}"));

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
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Task>>any())).thenReturn(task);

		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setDescriptions("Descriptions");
		taskDTO.setDueDate("2020-03-01");
		taskDTO.setId(1L);
		taskDTO.setStatus("Status");
		assertThrows(RuntimeException.class, () -> employeeServiceImpl.addTaskForEmployee(1L, taskDTO));
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(taskRepository).save(Mockito.<Task>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Task>>any());
	}

	// addTaskForEmployee End

	// getAllTaskForEmployeeById Start
	@Test
	void testGetAllTaskForEmployeeByIdValidInput() {
		// Arrange
		Long employeeId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		Task task1 = new Task();
		task1.setId(1L);
		Task task2 = new Task();
		task2.setId(2L);
		List<Task> taskList = List.of(task1, task2);
		Page<Task> taskPage = new PageImpl<>(taskList, pageable, taskList.size());

		when(taskRepository.findAllByEmployeeId(employeeId, pageable)).thenReturn(taskPage);
		when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(new TaskDTO());

		// Act
		Page<TaskDTO> result = employeeServiceImpl.getAllTaskForEmployeeById(employeeId, pageable);

		// Assert
		assertNotNull(result);
		assertEquals(taskList.size(), result.getContent().size());
		verify(taskRepository, times(1)).findAllByEmployeeId(employeeId, pageable);
		verify(modelMapper, times(taskList.size())).map(any(Task.class), eq(TaskDTO.class));
	}

	@Test
	void testGetAllTaskForEmployeeByIdInvalidEmployeeId() {
		// Arrange
		Long employeeId = -1L; // Invalid employee ID
		Pageable pageable = PageRequest.of(0, 10);

		// Act
		Page<TaskDTO> result = employeeServiceImpl.getAllTaskForEmployeeById(employeeId, pageable);

		// Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(taskRepository, never()).findAllByEmployeeId(anyLong(), any(Pageable.class));
		verify(modelMapper, never()).map(any(Task.class), eq(TaskDTO.class));
	}

	// getAllTaskForEmployeeById End

	// getTasksByStatus Start

	/**
     * Method under test: {@link EmployeeServiceImpl#getTasksByStatus(Long, String, int, int)}
     */
    @Test
    void testGetTasksByStatus6() {
        when(employeeRepository.findById(Mockito.<Long>any()))
                .thenThrow(new RuntimeException("Fetching tasks with status {} for Employee ID: {}"));
        assertThrows(RuntimeException.class, () -> employeeServiceImpl.getTasksByStatus(1L, "Status", 1, 3));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

	/**
     * Method under test: {@link EmployeeServiceImpl#getTasksByStatus(Long, String, int, int)}
     */
    @Test
    void testGetTasksByStatus4() {
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeServiceImpl.getTasksByStatus(1L, "Status", 1, 3));
        verify(employeeRepository).findById(Mockito.<Long>any());
    }

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getTasksByStatus(Long, String, int, int)}
	 */
	@Test
	void testGetTasksByStatus3() throws UnsupportedEncodingException {
		Employee employee = new Employee();
		employee.setDepartment("Fetching tasks with status {} for Employee ID: {}");
		employee.setDesignation("Fetching tasks with status {} for Employee ID: {}");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("Fetching tasks with status {} for Employee ID: {}");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Fetching tasks with status {} for Employee ID: {}");
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument(new byte[] { 'A', 1, 'A', 1, 'A', 1, 'A', 1 });

		Employee employee2 = new Employee();
		employee2.setDepartment("Fetching tasks with status {} for Employee ID: {}");
		employee2.setDesignation("Fetching tasks with status {} for Employee ID: {}");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("Fetching tasks with status {} for Employee ID: {}");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Fetching tasks with status {} for Employee ID: {}");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument(new byte[] { 'A', 1, 'A', 1, 'A', 1, 'A', 1 });

		Timesheet timesheet = new Timesheet();
		timesheet.preUpdate();
		timesheet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setDescription("The characteristics of someone or something");
		timesheet.setEmployee(employee2);
		timesheet.setHours(10.0d);
		timesheet.setId(1L);
		timesheet.setTasks(new ArrayList<>());
		timesheet.setUpdatedBy("2020-03-01");
		timesheet.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setWorkStatus("Fetching tasks with status {} for Employee ID: {}");

		Task task = new Task();
		task.setDescriptions("Fetching tasks with status {} for Employee ID: {}");
		task.setDueDate(LocalDate.of(1970, 1, 1));
		task.setEmployee(employee);
		task.setId(1L);
		task.setStatus("Fetching tasks with status {} for Employee ID: {}");
		task.setTimesheet(timesheet);

		Employee employee3 = new Employee();
		employee3.setDepartment("Department");
		employee3.setDesignation("Designation");
		employee3.setEmail("john.smith@example.org");
		employee3.setFileName("Fetching tasks with status {} for Employee ID: {}");
		employee3.setFileType("File Type");
		employee3.setHireDate(LocalDate.of(1970, 1, 1));
		employee3.setId(2L);
		employee3.setName("Name");
		employee3.setTasks(new ArrayList<>());
		employee3.setUploadDocument(new byte[] { 'A', 5, 'A', 5, 'A', 5, 'A', 5 });

		Employee employee4 = new Employee();
		employee4.setDepartment("Department");
		employee4.setDesignation("Designation");
		employee4.setEmail("john.smith@example.org");
		employee4.setFileName("Fetching tasks with status {} for Employee ID: {}");
		employee4.setFileType("File Type");
		employee4.setHireDate(LocalDate.of(1970, 1, 1));
		employee4.setId(2L);
		employee4.setName("Name");
		employee4.setTasks(new ArrayList<>());
		employee4.setUploadDocument(new byte[] { 'A', 5, 'A', 5, 'A', 5, 'A', 5 });

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Fetching tasks with status {} for Employee ID: {}");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("Fetching tasks with status {} for Employee ID: {}");
		timesheet2.setEmployee(employee4);
		timesheet2.setHours(0.5d);
		timesheet2.setId(2L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020/03/01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("Work Status");

		Task task2 = new Task();
		task2.setDescriptions("Descriptions");
		task2.setDueDate(LocalDate.of(1970, 1, 1));
		task2.setEmployee(employee3);
		task2.setId(2L);
		task2.setStatus("Status");
		task2.setTimesheet(timesheet2);

		ArrayList<Task> tasks = new ArrayList<>();
		tasks.add(task2);
		tasks.add(task);

		Employee employee5 = new Employee();
		employee5.setDepartment("Department");
		employee5.setDesignation("Designation");
		employee5.setEmail("jane.doe@example.org");
		employee5.setFileName("foo.txt");
		employee5.setFileType("File Type");
		employee5.setHireDate(LocalDate.of(1970, 1, 1));
		employee5.setId(1L);
		employee5.setName("Name");
		employee5.setTasks(tasks);
		employee5.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee5);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertTrue(employeeServiceImpl.getTasksByStatus(1L, "Status", 1, 3).toList().isEmpty());
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getTasksByStatus(Long, String, int, int)}
	 */
	@Test
	void testGetTasksByStatus2() throws UnsupportedEncodingException {
		Employee employee = new Employee();
		employee.setDepartment("Fetching tasks with status {} for Employee ID: {}");
		employee.setDesignation("Fetching tasks with status {} for Employee ID: {}");
		employee.setEmail("jane.doe@example.org");
		employee.setFileName("foo.txt");
		employee.setFileType("Fetching tasks with status {} for Employee ID: {}");
		employee.setHireDate(LocalDate.of(1970, 1, 1));
		employee.setId(1L);
		employee.setName("Fetching tasks with status {} for Employee ID: {}");
		employee.setTasks(new ArrayList<>());
		employee.setUploadDocument(new byte[] { 'A', 1, 'A', 1, 'A', 1, 'A', 1 });

		Employee employee2 = new Employee();
		employee2.setDepartment("Fetching tasks with status {} for Employee ID: {}");
		employee2.setDesignation("Fetching tasks with status {} for Employee ID: {}");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("Fetching tasks with status {} for Employee ID: {}");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Fetching tasks with status {} for Employee ID: {}");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument(new byte[] { 'A', 1, 'A', 1, 'A', 1, 'A', 1 });

		Timesheet timesheet = new Timesheet();
		timesheet.preUpdate();
		timesheet.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setDescription("The characteristics of someone or something");
		timesheet.setEmployee(employee2);
		timesheet.setHours(10.0d);
		timesheet.setId(1L);
		timesheet.setTasks(new ArrayList<>());
		timesheet.setUpdatedBy("2020-03-01");
		timesheet.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet.setWorkStatus("Fetching tasks with status {} for Employee ID: {}");

		Task task = new Task();
		task.setDescriptions("Fetching tasks with status {} for Employee ID: {}");
		task.setDueDate(LocalDate.of(1970, 1, 1));
		task.setEmployee(employee);
		task.setId(1L);
		task.setStatus("Fetching tasks with status {} for Employee ID: {}");
		task.setTimesheet(timesheet);

		ArrayList<Task> tasks = new ArrayList<>();
		tasks.add(task);

		Employee employee3 = new Employee();
		employee3.setDepartment("Department");
		employee3.setDesignation("Designation");
		employee3.setEmail("jane.doe@example.org");
		employee3.setFileName("foo.txt");
		employee3.setFileType("File Type");
		employee3.setHireDate(LocalDate.of(1970, 1, 1));
		employee3.setId(1L);
		employee3.setName("Name");
		employee3.setTasks(tasks);
		employee3.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));
		Optional<Employee> ofResult = Optional.of(employee3);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertTrue(employeeServiceImpl.getTasksByStatus(1L, "Status", 1, 3).toList().isEmpty());
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getTasksByStatus(Long, String, int, int)}
	 */
	@Test
	void testGetTasksByStatus() throws UnsupportedEncodingException {
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
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertTrue(employeeServiceImpl.getTasksByStatus(1L, "Status", 1, 3).toList().isEmpty());
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	// getTasksByStatus End

	// updateEmployeeTasksByEmployeeId start
	// Tested Full Covered

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#updateEmployeeTasksByEmployeeId(Long, List)}
	 */
	@Test
	void testUpdateEmployeeTasksByEmployeeId() throws UnsupportedEncodingException {
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
		when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee2);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		EmployeeDTO employeeDTO = new EmployeeDTO();
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any())).thenReturn(employeeDTO);
		assertSame(employeeDTO, employeeServiceImpl.updateEmployeeTasksByEmployeeId(1L, new ArrayList<>()));
		verify(employeeRepository).save(Mockito.<Employee>any());
		verify(employeeRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<EmployeeDTO>>any());
	}

	@Test
	void testUpdateEmployeeTasksByEmployeeIdAddNewTask() {
		// Arrange
		Long employeeId = 1L;
		List<TaskDTO> taskDTOList = new ArrayList<>();
		TaskDTO taskDTO1 = new TaskDTO();
		taskDTO1.setId(2L); // New task ID
		taskDTOList.add(taskDTO1);

		Employee employee = new Employee();

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
		when(modelMapper.map(any(TaskDTO.class), eq(Task.class))).thenReturn(new Task());

		// Act
		EmployeeDTO result = employeeServiceImpl.updateEmployeeTasksByEmployeeId(employeeId, taskDTOList);

		// Assert
		assertNotNull(result);
		verify(employeeRepository, times(1)).findById(employeeId);
		verify(modelMapper, times(1)).map(any(TaskDTO.class), eq(Task.class));
		verify(employeeRepository, times(1)).save(employee);
	}

	@Test
	void testUpdateEmployeeTasksByEmployeeIdEmployeeNotFound() {
		// Arrange
		Long employeeId = 1L;
		List<TaskDTO> taskDTOList = new ArrayList<>();

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class,
				() -> employeeServiceImpl.updateEmployeeTasksByEmployeeId(employeeId, taskDTOList));

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(modelMapper, never()).map(any(TaskDTO.class), eq(Task.class));
		verify(employeeRepository, never()).save(any(Employee.class));
	}
	// updateEmployeeTasksByEmployeeId Ends

	// getAllTasksForEmployeeWithoutIds Start
	// Tested Full Covered

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllTasksForEmployeeWithoutIds(Long, Pageable)}
	 */
	@Test
	void testGetAllTasksForEmployeeWithoutIds4() {

		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
		TaskRepository taskRepository = mock(TaskRepository.class);
		assertThrows(ResourceNotFoundException.class, () -> (new EmployeeServiceImpl(employeeRepository, taskRepository,
				new ModelMapper(), mock(TimesheetRepository.class))).getAllTasksForEmployeeWithoutIds(1L, null));
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	/**
	 * Method under test:
	 * {@link EmployeeServiceImpl#getAllTasksForEmployeeWithoutIds(Long, Pageable)}
	 */
	@Test
	void testGetAllTasksForEmployeeWithoutIds5() {

		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		when(employeeRepository.findById(Mockito.<Long>any()))
				.thenThrow(new RuntimeException("Fetching all tasks for Employee ID {} without task IDs"));
		TaskRepository taskRepository = mock(TaskRepository.class);
		assertThrows(RuntimeException.class, () -> (new EmployeeServiceImpl(employeeRepository, taskRepository,
				new ModelMapper(), mock(TimesheetRepository.class))).getAllTasksForEmployeeWithoutIds(1L, null));
		verify(employeeRepository).findById(Mockito.<Long>any());
	}

	@Test
	void testGetAllTasksForEmployeeWithoutIdsSuccess() {
		// Arrange
		Long employeeId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		Employee employee = new Employee();
		Task task1 = new Task();
		Task task2 = new Task();
		employee.setTasks(List.of(task1, task2));

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
		when(modelMapper.map(any(Task.class), eq(TaskDTOWithoutTaskID.class))).thenReturn(new TaskDTOWithoutTaskID());

		// Act
		org.springframework.data.domain.Page<TaskDTOWithoutTaskID> result = employeeServiceImpl
				.getAllTasksForEmployeeWithoutIds(employeeId, pageable);

		// Assert
		assertEquals(2, result.getContent().size());
		verify(employeeRepository, times(1)).findById(employeeId);
		verify(modelMapper, times(2)).map(any(Task.class), eq(TaskDTOWithoutTaskID.class));
	}

	@Test
	void testGetAllTasksForEmployeeWithoutIdsEmployeeNotFound() {
		// Arrange
		Long employeeId = 1L;
		Pageable pageable = PageRequest.of(0, 10);

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(ResourceNotFoundException.class,
				() -> employeeServiceImpl.getAllTasksForEmployeeWithoutIds(employeeId, pageable));

		verify(employeeRepository, times(1)).findById(employeeId);
		verify(modelMapper, never()).map(any(Task.class), eq(TaskDTOWithoutTaskID.class));
	}

	// getAllTasksForEmployeeWithoutIds End

}
