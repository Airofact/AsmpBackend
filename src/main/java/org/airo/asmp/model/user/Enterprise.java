package org.airo.asmp.model.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Inheritance(strategy = jakarta.persistence.InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
public class Enterprise extends User {
	private String name;
	private String field;
	private String address;
	private String contactPerson;
	private String contactEmail;
	private String contactPhone;

	public enum Status {
		PENDING,
		APPROVED,
	}
	private Status status;
}
