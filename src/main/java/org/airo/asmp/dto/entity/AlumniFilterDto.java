package org.airo.asmp.dto.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.airo.asmp.model.Gender;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.entity.Alumni}
 */
public record AlumniFilterDto (
	UUID id,
	LocalDateTime addedAtBegin,
	LocalDateTime addedAtEnd,
	String studentId,
	String realName,
	Gender gender,
	LocalDate dateOfBirthBegin,
	LocalDate dateOfBirthEnd,
	String address,
	String companyName,
	String currentJob
) {}