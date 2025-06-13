package org.airo.asmp.dto.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.airo.asmp.model.entity.Alumni;

import java.time.LocalDate;

/**
 * DTO for {@link org.airo.asmp.model.entity.Alumni}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlumniFilterDto extends BusinessEntityFilterDto {
	private String studentId;
	private String realName;
	private Alumni.Gender gender;
	private LocalDate dateOfBirthBegin;
	private LocalDate dateOfBirthEnd;
	private String address;
	private String companyName;
	private String currentJob;
}