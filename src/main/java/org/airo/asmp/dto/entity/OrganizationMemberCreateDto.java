package org.airo.asmp.dto.entity;

import org.airo.asmp.model.entity.OrganizationMember;

import java.util.UUID;

/**
 * DTO for {@link org.airo.asmp.model.entity.OrganizationMember}
 * Represents a request to create a new organization member with a specific role.
 * organizationId will be provided via path parameter
 */
public record OrganizationMemberCreateDto(
        UUID alumniId,
        OrganizationMember.Role role
) { }
