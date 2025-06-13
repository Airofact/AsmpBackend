package org.airo.asmp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
@DiscriminatorValue("Alumni")
@NoArgsConstructor
@AllArgsConstructor
public class Alumni extends BusinessEntity {
	@Column(nullable = false, updatable = false, unique = true)
	private String studentId;
	@NotNull
	private String realName;
	@NotNull
	private Gender gender;
	@NotNull
	private LocalDate dateOfBirth;
	private String address;
	private String companyName;
	private String currentJob;

	public enum Gender {
		MALE,
		FEMALE
	}
}
