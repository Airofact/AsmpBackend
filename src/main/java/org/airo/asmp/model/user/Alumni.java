package org.airo.asmp.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.airo.asmp.model.*;
import lombok.AllArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
public class Alumni extends User {
	private String realName;
	private Gender gender;
	private Date dateOfBirth;
	private String address;
	private String companyName;
	private String currentJob;

	public enum Status {
		PENDING,
		APPROVED,
	}

	private Status status;

	public int getAge() {
		LocalDate birthDate = dateOfBirth.toLocalDate();
		LocalDate currentDate = LocalDate.now();
		return Period.between(birthDate, currentDate).getYears();
	}
}
