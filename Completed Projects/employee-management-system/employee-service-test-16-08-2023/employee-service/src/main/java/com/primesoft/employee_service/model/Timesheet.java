package com.primesoft.employee_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Entity
@Data
@Table(name = "timesheet")
@Schema(description = "Details about a timesheet")
public class Timesheet {

	private static final Logger logger = LoggerFactory.getLogger(Timesheet.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "The unique ID of the timesheet")
	private Long id;

	@Schema(description = "The description of the timesheet")
	private String description;

	@Schema(description = "The total hours worked in the timesheet")
	private double hours;

	@Schema(description = "The work status of the timesheet")
	private String workStatus;

	@Schema(description = "The user who created the timesheet")
	private String createdBy;

	@Schema(description = "The user who updated the timesheet")
	private String updatedBy;

	@Schema(description = "The date and time when the timesheet was created")
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Schema(description = "The date and time when the timesheet was last updated")
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	@ManyToOne
	@JoinColumn(name = "employee_ids")
	@Schema(description = "The employee associated with the timesheet")
	private Employee employee;

	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = false, mappedBy = "timesheet")
	@Schema(description = "List of tasks associated with the timesheet")
	private List<Task> tasks = new ArrayList<>();

	/**
	 * This method is automatically called before the entity is persisted (i.e.,
	 * before being inserted into the database). It sets the createdDate and
	 * updatedDate fields to the current date and time.
	 */
	@PrePersist
	public void prePersist() {
		createdDate = LocalDateTime.now();
		updatedDate = createdDate;
		logger.info("Timesheet created at: {}", createdDate);
	}

	/**
	 * This method is automatically called before the entity is updated (i.e.,
	 * before being updated in the database). It updates the updatedDate field to
	 * the current date and time.
	 */
	@PreUpdate
	public void preUpdate() {
		updatedDate = LocalDateTime.now();
		logger.info("Timesheet updated at: {}", updatedDate);
	}
}
