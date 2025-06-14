package org.airo.asmp.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class BusinessEntity {
	@Id
	@UuidGenerator
	@Immutable
	protected UUID id;

	@NotNull
	protected LocalDateTime addedAt;

	@PrePersist
	protected void onCreate() {
		addedAt = LocalDateTime.now();
	}
}
