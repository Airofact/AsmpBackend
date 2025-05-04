package org.airo.asmp.dto.entity;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.entity.Enterprise}
 */
public record EnterpriseCreateDto(UUID addedById, String name, String field, String address, String contactPerson,
								  String contactEmail, String contactPhone) implements Serializable {
}