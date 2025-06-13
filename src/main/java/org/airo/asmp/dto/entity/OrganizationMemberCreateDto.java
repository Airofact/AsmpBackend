package org.airo.asmp.dto.entity;

import org.airo.asmp.model.entity.OrganizationAlumniId;
import org.airo.asmp.model.entity.OrganizationMember;

/**
 * DTO for {@link org.airo.asmp.model.entity.OrganizationMember}
 * Represents a request to create a new organization member with a specific role.
 */

public record OrganizationMemberCreateDto(
        OrganizationAlumniId id,
        OrganizationMember.Role role
) {
}
