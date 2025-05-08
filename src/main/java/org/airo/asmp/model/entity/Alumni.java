package org.airo.asmp.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import org.airo.asmp.model.*;
import org.hibernate.annotations.Immutable;

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
}
