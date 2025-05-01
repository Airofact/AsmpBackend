package org.airo.asmp.model.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
public class User {
	@Id
	@UuidGenerator
	@Immutable
	protected String id;

	protected String userName;

	protected String password;

	protected String email;

	protected String phone;

	protected Date createdAt;

	public User(
			String userName,
			String password,
			@Nullable String email,
			@Nullable String phone,
			Date createAt
	) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.createdAt = createAt;
	}

	public User(
			String userName,
			String password,
			@Nullable String email,
			@Nullable String phone
	) {
		this(userName, password, email, phone, new Date(System.currentTimeMillis()));
	}
}
