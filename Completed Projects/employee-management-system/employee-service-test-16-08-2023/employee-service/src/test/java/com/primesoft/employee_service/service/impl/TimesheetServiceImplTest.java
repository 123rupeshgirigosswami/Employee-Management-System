package com.primesoft.employee_service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.primesoft.employee_service.exception.ResourceNotFoundException;
import com.primesoft.employee_service.model.Employee;
import com.primesoft.employee_service.model.Task;
import com.primesoft.employee_service.model.Timesheet;
import com.primesoft.employee_service.payloads.TimesheetDto;
import com.primesoft.employee_service.repository.EmployeeRepository;
import com.primesoft.employee_service.repository.TaskRepository;
import com.primesoft.employee_service.repository.TimesheetRepository;

@ContextConfiguration(classes = { TimesheetServiceImpl.class })
@ExtendWith(SpringExtension.class)
class TimesheetServiceImplTest {
	@MockBean
	private EmployeeRepository employeeRepository;

	@MockBean
	private ModelMapper modelMapper;

	@MockBean
	private TaskRepository taskRepository;

	@MockBean
	private TimesheetRepository timesheetRepository;

	@Autowired
	private TimesheetServiceImpl timesheetServiceImpl;

	/**
     * Method under test: {@link TimesheetServiceImpl#createTimesheet(Long, TimesheetDto)}
     */
    @Test
    void testCreateTimesheet() throws UnsupportedEncodingException {
        when(timesheetRepository.save(Mockito.<Timesheet>any()))
                .thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "Field Value"));

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
        timesheet.setWorkStatus("Work Status");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Timesheet>>any())).thenReturn(timesheet);

        TimesheetDto timesheetDto = new TimesheetDto();
        timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        timesheetDto.setDescription("The characteristics of someone or something");
        timesheetDto.setHours(10.0d);
        timesheetDto.setId(1L);
        timesheetDto.setTaskIds(new ArrayList<>());
        timesheetDto.setUpdatedBy("2020-03-01");
        timesheetDto.setWorkStatus("Work Status");
        assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.createTimesheet(1L, timesheetDto));
        verify(timesheetRepository).save(Mockito.<Timesheet>any());
        verify(employeeRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Timesheet>>any());
    }

	/**
	 * Method under test: {@link TimesheetServiceImpl#getTimesheetById(Long)}
	 */
	@Test
	void testGetTimesheetById() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		assertSame(timesheetDto, timesheetServiceImpl.getTimesheetById(1L));
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test: {@link TimesheetServiceImpl#getTimesheetById(Long)}
	 */
	@Test
	void testGetTimesheetById2() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any()))
				.thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "Field Value"));
		assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.getTimesheetById(1L));
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
     * Method under test: {@link TimesheetServiceImpl#getTimesheetById(Long)}
     */
    @Test
    void testGetTimesheetById3() {
        when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());

        TimesheetDto timesheetDto = new TimesheetDto();
        timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        timesheetDto.setDescription("The characteristics of someone or something");
        timesheetDto.setHours(10.0d);
        timesheetDto.setId(1L);
        timesheetDto.setTaskIds(new ArrayList<>());
        timesheetDto.setUpdatedBy("2020-03-01");
        timesheetDto.setWorkStatus("Work Status");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn("Map");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
        assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.getTimesheetById(1L));
        verify(timesheetRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Object>>any());
    }

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#updateTimesheet(Long, TimesheetDto)}
	 */
	@Test
	void testUpdateTimesheet() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);

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

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("The characteristics of someone or something");
		timesheet2.setEmployee(employee2);
		timesheet2.setHours(10.0d);
		timesheet2.setId(1L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020-03-01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("Work Status");
		when(timesheetRepository.save(Mockito.<Timesheet>any())).thenReturn(timesheet2);
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);

		TimesheetDto timesheetDto2 = new TimesheetDto();
		timesheetDto2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto2.setDescription("The characteristics of someone or something");
		timesheetDto2.setHours(10.0d);
		timesheetDto2.setId(1L);
		timesheetDto2.setTaskIds(new ArrayList<>());
		timesheetDto2.setUpdatedBy("2020-03-01");
		timesheetDto2.setWorkStatus("Work Status");
		assertSame(timesheetDto, timesheetServiceImpl.updateTimesheet(1L, timesheetDto2));
		verify(timesheetRepository).save(Mockito.<Timesheet>any());
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#updateTimesheet(Long, TimesheetDto)}
	 */
	@Test
	void testUpdateTimesheet2() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);

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

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("The characteristics of someone or something");
		timesheet2.setEmployee(employee2);
		timesheet2.setHours(10.0d);
		timesheet2.setId(1L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020-03-01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("Work Status");
		when(timesheetRepository.save(Mockito.<Timesheet>any())).thenReturn(timesheet2);
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any()))
				.thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "Field Value"));

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.updateTimesheet(1L, timesheetDto));
		verify(timesheetRepository).save(Mockito.<Timesheet>any());
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#updateTimesheet(Long, TimesheetDto)}
	 */
	@Test
	void testUpdateTimesheet3() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);

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

		Employee employee4 = new Employee();
		employee4.setDepartment("Department");
		employee4.setDesignation("Designation");
		employee4.setEmail("jane.doe@example.org");
		employee4.setFileName("foo.txt");
		employee4.setFileType("File Type");
		employee4.setHireDate(LocalDate.of(1970, 1, 1));
		employee4.setId(1L);
		employee4.setName("Name");
		employee4.setTasks(new ArrayList<>());
		employee4.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("The characteristics of someone or something");
		timesheet2.setEmployee(employee4);
		timesheet2.setHours(10.0d);
		timesheet2.setId(1L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020-03-01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("Work Status");

		Task task = new Task();
		task.setDescriptions("Descriptions");
		task.setDueDate(LocalDate.of(1970, 1, 1));
		task.setEmployee(employee3);
		task.setId(1L);
		task.setStatus("Status");
		task.setTimesheet(timesheet2);

		ArrayList<Task> tasks = new ArrayList<>();
		tasks.add(task);

		Timesheet timesheet3 = new Timesheet();
		timesheet3.preUpdate();
		timesheet3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet3.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet3.setDescription("The characteristics of someone or something");
		timesheet3.setEmployee(employee2);
		timesheet3.setHours(10.0d);
		timesheet3.setId(1L);
		timesheet3.setTasks(tasks);
		timesheet3.setUpdatedBy("2020-03-01");
		timesheet3.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet3.setWorkStatus("Work Status");
		when(timesheetRepository.save(Mockito.<Timesheet>any())).thenReturn(timesheet3);
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);

		TimesheetDto timesheetDto2 = new TimesheetDto();
		timesheetDto2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto2.setDescription("The characteristics of someone or something");
		timesheetDto2.setHours(10.0d);
		timesheetDto2.setId(1L);
		timesheetDto2.setTaskIds(new ArrayList<>());
		timesheetDto2.setUpdatedBy("2020-03-01");
		timesheetDto2.setWorkStatus("Work Status");
		assertSame(timesheetDto, timesheetServiceImpl.updateTimesheet(1L, timesheetDto2));
		verify(timesheetRepository).save(Mockito.<Timesheet>any());
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test: {@link TimesheetServiceImpl#deleteTimesheet(Long)}
	 */
	@Test
	void testDeleteTimesheet() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);
		doNothing().when(timesheetRepository).delete(Mockito.<Timesheet>any());
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertSame(timesheet, timesheetServiceImpl.deleteTimesheet(1L));
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(timesheetRepository).delete(Mockito.<Timesheet>any());
	}

	/**
	 * Method under test: {@link TimesheetServiceImpl#deleteTimesheet(Long)}
	 */
	@Test
	void testDeleteTimesheet2() throws UnsupportedEncodingException {
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
		Optional<Timesheet> ofResult = Optional.of(timesheet);
		doThrow(new ResourceNotFoundException("Deleting timesheet with ID: {}", "Deleting timesheet with ID: {}",
				"Field Value")).when(timesheetRepository).delete(Mockito.<Timesheet>any());
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.deleteTimesheet(1L));
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(timesheetRepository).delete(Mockito.<Timesheet>any());
	}

	/**
	 * Method under test: {@link TimesheetServiceImpl#deleteTimesheet(Long)}
	 */
	@Test
	void testDeleteTimesheet3() throws UnsupportedEncodingException {
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
		employee2.setDepartment("Deleting timesheet with ID: {}");
		employee2.setDesignation("Deleting timesheet with ID: {}");
		employee2.setEmail("jane.doe@example.org");
		employee2.setFileName("foo.txt");
		employee2.setFileType("Deleting timesheet with ID: {}");
		employee2.setHireDate(LocalDate.of(1970, 1, 1));
		employee2.setId(1L);
		employee2.setName("Deleting timesheet with ID: {}");
		employee2.setTasks(new ArrayList<>());
		employee2.setUploadDocument("AXAXAXAX".getBytes("UTF-8"));

		Employee employee3 = new Employee();
		employee3.setDepartment("Deleting timesheet with ID: {}");
		employee3.setDesignation("Deleting timesheet with ID: {}");
		employee3.setEmail("jane.doe@example.org");
		employee3.setFileName("foo.txt");
		employee3.setFileType("Deleting timesheet with ID: {}");
		employee3.setHireDate(LocalDate.of(1970, 1, 1));
		employee3.setId(1L);
		employee3.setName("Deleting timesheet with ID: {}");
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
		timesheet.setWorkStatus("Deleting timesheet with ID: {}");

		Task task = new Task();
		task.setDescriptions("Deleting timesheet with ID: {}");
		task.setDueDate(LocalDate.of(1970, 1, 1));
		task.setEmployee(employee2);
		task.setId(1L);
		task.setStatus("Deleting timesheet with ID: {}");
		task.setTimesheet(timesheet);

		ArrayList<Task> tasks = new ArrayList<>();
		tasks.add(task);

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("The characteristics of someone or something");
		timesheet2.setEmployee(employee);
		timesheet2.setHours(10.0d);
		timesheet2.setId(1L);
		timesheet2.setTasks(tasks);
		timesheet2.setUpdatedBy("2020-03-01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("Work Status");
		Optional<Timesheet> ofResult = Optional.of(timesheet2);
		doNothing().when(timesheetRepository).delete(Mockito.<Timesheet>any());
		when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
		assertSame(timesheet2, timesheetServiceImpl.deleteTimesheet(1L));
		verify(timesheetRepository).findById(Mockito.<Long>any());
		verify(timesheetRepository).delete(Mockito.<Timesheet>any());
	}

	/**
     * Method under test: {@link TimesheetServiceImpl#deleteTimesheet(Long)}
     */
    @Test
    void testDeleteTimesheet4() {
        when(timesheetRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.deleteTimesheet(1L));
        verify(timesheetRepository).findById(Mockito.<Long>any());
    }

	/**
     * Method under test: {@link TimesheetServiceImpl#getAllTimesheets(Pageable)}
     */
    @Test
    void testGetAllTimesheets() {
        when(timesheetRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(timesheetServiceImpl.getAllTimesheets(null).toList().isEmpty());
        verify(timesheetRepository).findAll(Mockito.<Pageable>any());
    }

	/**
	 * Method under test: {@link TimesheetServiceImpl#getAllTimesheets(Pageable)}
	 */
	@Test
	void testGetAllTimesheets2() throws UnsupportedEncodingException {
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

		ArrayList<Timesheet> content = new ArrayList<>();
		content.add(timesheet);
		PageImpl<Timesheet> pageImpl = new PageImpl<>(content);
		when(timesheetRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		assertEquals(1, timesheetServiceImpl.getAllTimesheets(null).toList().size());
		verify(timesheetRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test: {@link TimesheetServiceImpl#getAllTimesheets(Pageable)}
	 */
	@Test
	void testGetAllTimesheets3() throws UnsupportedEncodingException {
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

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Created By");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("Description");
		timesheet2.setEmployee(employee2);
		timesheet2.setHours(0.5d);
		timesheet2.setId(2L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020/03/01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("com.primesoft.employee_service.model.Timesheet");

		ArrayList<Timesheet> content = new ArrayList<>();
		content.add(timesheet2);
		content.add(timesheet);
		PageImpl<Timesheet> pageImpl = new PageImpl<>(content);
		when(timesheetRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		assertEquals(2, timesheetServiceImpl.getAllTimesheets(null).toList().size());
		verify(timesheetRepository).findAll(Mockito.<Pageable>any());
		verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
     * Method under test: {@link TimesheetServiceImpl#getAllByFromDateAndToDate(LocalDate, LocalDate, Pageable)}
     */
    @Test
    void testGetAllByFromDateAndToDate() {
        when(timesheetRepository.findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
                Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        LocalDate fromDate = LocalDate.of(1970, 1, 1);
        assertTrue(
                timesheetServiceImpl.getAllByFromDateAndToDate(fromDate, LocalDate.of(1970, 1, 1), null).toList().isEmpty());
        verify(timesheetRepository).findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
                Mockito.<Pageable>any());
    }

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#getAllByFromDateAndToDate(LocalDate, LocalDate, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate2() {
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
		employee.setUploadDocument(new byte[] { 'A', 23, 'A', 23, 'A', 23, 'A', 23 });

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

		ArrayList<Timesheet> content = new ArrayList<>();
		content.add(timesheet);
		PageImpl<Timesheet> pageImpl = new PageImpl<>(content);
		when(timesheetRepository.findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any())).thenReturn(pageImpl);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		LocalDate fromDate = LocalDate.of(1970, 1, 1);
		assertEquals(1, timesheetServiceImpl.getAllByFromDateAndToDate(fromDate, LocalDate.of(1970, 1, 1), null)
				.toList().size());
		verify(timesheetRepository).findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#getAllByFromDateAndToDate(LocalDate, LocalDate, Pageable)}
	 */
	@Test
	void testGetAllByFromDateAndToDate3() {
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
		employee.setUploadDocument(new byte[] { 'A', 23, 'A', 23, 'A', 23, 'A', 23 });

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
		employee2.setUploadDocument(new byte[] { 'A', 23, 'A', 23, 'A', 23, 'A', 23 });

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Created By");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("Description");
		timesheet2.setEmployee(employee2);
		timesheet2.setHours(0.5d);
		timesheet2.setId(2L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020/03/01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("com.primesoft.employee_service.model.Timesheet");

		ArrayList<Timesheet> content = new ArrayList<>();
		content.add(timesheet2);
		content.add(timesheet);
		PageImpl<Timesheet> pageImpl = new PageImpl<>(content);
		when(timesheetRepository.findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any())).thenReturn(pageImpl);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		LocalDate fromDate = LocalDate.of(1970, 1, 1);
		assertEquals(2, timesheetServiceImpl.getAllByFromDateAndToDate(fromDate, LocalDate.of(1970, 1, 1), null)
				.toList().size());
		verify(timesheetRepository).findByCreatedDateBetween(Mockito.<LocalDateTime>any(), Mockito.<LocalDateTime>any(),
				Mockito.<Pageable>any());
		verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
     * Method under test: {@link TimesheetServiceImpl#getAllTimesheetsByEmployeeId(Long)}
     */
    @Test
    void testGetAllTimesheetsByEmployeeId() {
        when(timesheetRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        assertTrue(timesheetServiceImpl.getAllTimesheetsByEmployeeId(1L).isEmpty());
        verify(timesheetRepository).findAllByEmployeeId(Mockito.<Long>any());
    }

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#getAllTimesheetsByEmployeeId(Long)}
	 */
	@Test
	void testGetAllTimesheetsByEmployeeId2() throws UnsupportedEncodingException {
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

		ArrayList<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		when(timesheetRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(timesheetList);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		assertEquals(1, timesheetServiceImpl.getAllTimesheetsByEmployeeId(1L).size());
		verify(timesheetRepository).findAllByEmployeeId(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#getAllTimesheetsByEmployeeId(Long)}
	 */
	@Test
	void testGetAllTimesheetsByEmployeeId3() throws UnsupportedEncodingException {
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

		Timesheet timesheet2 = new Timesheet();
		timesheet2.preUpdate();
		timesheet2.setCreatedBy("Created By");
		timesheet2.setCreatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setDescription("Description");
		timesheet2.setEmployee(employee2);
		timesheet2.setHours(0.5d);
		timesheet2.setId(2L);
		timesheet2.setTasks(new ArrayList<>());
		timesheet2.setUpdatedBy("2020/03/01");
		timesheet2.setUpdatedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
		timesheet2.setWorkStatus("com.primesoft.employee_service.model.Timesheet");

		ArrayList<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet2);
		timesheetList.add(timesheet);
		when(timesheetRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(timesheetList);

		TimesheetDto timesheetDto = new TimesheetDto();
		timesheetDto.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
		timesheetDto.setDescription("The characteristics of someone or something");
		timesheetDto.setHours(10.0d);
		timesheetDto.setId(1L);
		timesheetDto.setTaskIds(new ArrayList<>());
		timesheetDto.setUpdatedBy("2020-03-01");
		timesheetDto.setWorkStatus("Work Status");
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any())).thenReturn(timesheetDto);
		assertEquals(2, timesheetServiceImpl.getAllTimesheetsByEmployeeId(1L).size());
		verify(timesheetRepository).findAllByEmployeeId(Mockito.<Long>any());
		verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	/**
	 * Method under test:
	 * {@link TimesheetServiceImpl#getAllTimesheetsByEmployeeId(Long)}
	 */
	@Test
	void testGetAllTimesheetsByEmployeeId4() throws UnsupportedEncodingException {
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

		ArrayList<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		when(timesheetRepository.findAllByEmployeeId(Mockito.<Long>any())).thenReturn(timesheetList);
		when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any()))
				.thenThrow(new ResourceNotFoundException("Resource Name", "Field Name", "Field Value"));
		assertThrows(ResourceNotFoundException.class, () -> timesheetServiceImpl.getAllTimesheetsByEmployeeId(1L));
		verify(timesheetRepository).findAllByEmployeeId(Mockito.<Long>any());
		verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<TimesheetDto>>any());
	}

	@Test
	void testUpdateTimesheet11() {
		// Create a test Timesheet
		Timesheet timesheet = new Timesheet();
		// Set timesheet properties as needed
		timesheet.setId(1L);

		// Mock the TimesheetRepository's behavior
		when(timesheetRepository.findById(1L)).thenReturn(Optional.of(timesheet));
		when(timesheetRepository.save(any(Timesheet.class))).thenReturn(timesheet);

		// Mock the TaskRepository's behavior
		Long taskId1 = 1L;
		Long taskId2 = 2L;
		Task task1 = new Task();
		Task task2 = new Task();
		when(taskRepository.findById(taskId1)).thenReturn(Optional.of(task1));
		when(taskRepository.findById(taskId2)).thenReturn(Optional.of(task2));

		// Create a TimesheetDto to update the timesheet
		TimesheetDto timesheetDto = new TimesheetDto();
		// Set timesheetDto properties as needed
		timesheetDto.setTaskIds(List.of(taskId1, taskId2));

		// Call the service method to update the timesheet
		TimesheetDto updatedTimesheetDto = timesheetServiceImpl.updateTimesheet(timesheet.getId(), timesheetDto);

		// Assertions
		assertEquals(timesheetDto.getDescription(), updatedTimesheetDto.getDescription());
		assertEquals(timesheetDto.getHours(), updatedTimesheetDto.getHours());
		assertEquals(timesheetDto.getWorkStatus(), updatedTimesheetDto.getWorkStatus());
		// Add more assertions as needed

		// Verify that tasks were associated with the timesheet
		assertEquals(2, timesheet.getTasks().size());
		// Verify that the task IDs were used to fetch tasks from the repository
		verify(taskRepository, times(2)).findById(anyLong());
	}

	@Test
	void testCreateTimesheetWithoutTasks() {
		// Create a test TimesheetDto without task IDs
		TimesheetDto timesheetDto = new TimesheetDto();
		// Set timesheetDto properties as needed

		Long employeeId = 1L;
		Employee employee = new Employee();
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

		// Create a Timesheet entity to be saved
		Timesheet timesheetToSave = new Timesheet();
		// Set timesheetToSave properties as needed

		when(timesheetRepository.save(any(Timesheet.class))).thenReturn(timesheetToSave);

		TimesheetDto createdTimesheetDto = timesheetServiceImpl.createTimesheet(employeeId, timesheetDto);

		// Assertions
		assertEquals(timesheetDto.getDescription(), createdTimesheetDto.getDescription());
		// Add more assertions as needed

		verify(employeeRepository).findById(anyLong());
		verifyNoInteractions(taskRepository); // No interaction with task repository
	}

	@Test
	void testCreateTimesheetWithTasks() {
		// Create a test TimesheetDto with task IDs
		TimesheetDto timesheetDto = new TimesheetDto();
		// Set timesheetDto properties as needed
		timesheetDto.setTaskIds(List.of(1L, 2L)); // Assuming task IDs 1 and 2

		Long employeeId = 1L;
		Employee employee = new Employee();
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

		// Mock the TaskRepository's behavior
		Long taskId1 = 1L;
		Long taskId2 = 2L;
		Task task1 = new Task();
		Task task2 = new Task();
		when(taskRepository.findById(taskId1)).thenReturn(Optional.of(task1));
		when(taskRepository.findById(taskId2)).thenReturn(Optional.of(task2));

		// Create a Timesheet entity to be saved
		Timesheet timesheetToSave = new Timesheet();
		// Set timesheetToSave properties as needed

		when(timesheetRepository.save(any(Timesheet.class))).thenReturn(timesheetToSave);

		TimesheetDto createdTimesheetDto = timesheetServiceImpl.createTimesheet(employeeId, timesheetDto);

		// Assertions
		assertEquals(timesheetDto.getDescription(), createdTimesheetDto.getDescription());
		// Add more assertions as needed

		verify(employeeRepository).findById(anyLong());
		verify(taskRepository, times(2)).findById(anyLong());
		assertEquals(2, timesheetToSave.getTasks().size());
		verify(task1).setTimesheet(timesheetToSave);
		verify(task2).setTimesheet(timesheetToSave);
	}

	@Test
	void testCreateTimesheetEmployeeNotFound() {
		// Create a test TimesheetDto
		TimesheetDto timesheetDto = new TimesheetDto();
		// Set timesheetDto properties as needed

		Long employeeId = 1L;
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> timesheetServiceImpl.createTimesheet(employeeId, timesheetDto));

		verify(employeeRepository).findById(anyLong());
		verifyNoInteractions(taskRepository); // No interaction with task repository
	}

	@Test
	void testCreateTimesheetTaskNotFound() {
		// Create a test TimesheetDto with task IDs
		TimesheetDto timesheetDto = new TimesheetDto();
		// Set timesheetDto properties as needed
		timesheetDto.setTaskIds(List.of(1L)); // Assuming task ID 1

		Long employeeId = 1L;
		Employee employee = new Employee();
		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

		Long taskId1 = 1L;
		when(taskRepository.findById(taskId1)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class,
				() -> timesheetServiceImpl.createTimesheet(employeeId, timesheetDto));

		verify(employeeRepository).findById(anyLong());
		verify(taskRepository).findById(anyLong());
		verifyNoMoreInteractions(taskRepository); // No more interaction with task repository
	}

}
