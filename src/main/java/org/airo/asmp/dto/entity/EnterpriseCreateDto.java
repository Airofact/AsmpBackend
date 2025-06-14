package org.airo.asmp.dto.entity;

import java.io.Serializable;

/**
 * DTO for {@link org.airo.asmp.model.entity.Enterprise}
 */
public record EnterpriseCreateDto(
		String name,
		String field,
		String address,
		String contactPerson,
		String contactEmail,
		String contactPhone
) implements Serializable { }