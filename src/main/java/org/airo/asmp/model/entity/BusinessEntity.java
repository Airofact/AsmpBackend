package org.airo.asmp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.airo.asmp.model.Admin;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class BusinessEntity {
	@Id
	@UuidGenerator
	@Immutable
	@JsonIgnore
	protected UUID id;

	@ManyToOne
	@NotNull
	protected Admin addedBy;

	@NotNull
	protected LocalDateTime addedAt;

	@PrePersist
	protected void onCreate() {
		addedAt = LocalDateTime.now();
	}
}
