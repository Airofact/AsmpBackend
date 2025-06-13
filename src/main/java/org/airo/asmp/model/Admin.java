package org.airo.asmp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Admin {
	@Id
	@UuidGenerator
	@JsonIgnore
	private UUID id;

	private String username;

	private String password;
}
