package org.airo.asmp.dto.entity;

import jakarta.validation.constraints.NotNull;
import org.airo.asmp.model.Gender;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.entity.Alumni}
 */
public record AlumniCreateDto(UUID addedById, @NotNull String studentId, @NotNull String realName,
							  @NotNull Gender gender, @NotNull LocalDate dateOfBirth, String address,
							  String companyName, String currentJob) implements Serializable {
}