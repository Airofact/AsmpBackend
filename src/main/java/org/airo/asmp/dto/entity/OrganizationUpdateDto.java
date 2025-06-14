package org.airo.asmp.dto.entity;

/**
 * DTO for updating {@link org.airo.asmp.model.entity.Organization}
 */

public record OrganizationUpdateDto(
		String name,
        String type,
        String description,
		String state
) {}
