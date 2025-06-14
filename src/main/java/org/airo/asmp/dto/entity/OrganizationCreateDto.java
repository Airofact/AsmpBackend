package org.airo.asmp.dto.entity;

import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.entity.Organization}
 */

public record OrganizationCreateDto(String name,
        String type,
        String state,
        String description,
        UUID creatorId) {}
