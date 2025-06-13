package org.airo.asmp.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter
@DiscriminatorValue("enterprise")
@NoArgsConstructor
public class Enterprise extends BusinessEntity {
	private String name;
	private String field;
	private String address = "";
	private String contactPerson = "";
	private String contactEmail = "";
	private String contactPhone = "";
}
