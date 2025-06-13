package org.airo.asmp.dto.entity;

import org.airo.asmp.model.entity.OrganizationMember;

/**
 * DTO for updating {@link OrganizationMember}
 */
public record OrganizationMemberUpdateDto(
        OrganizationMember.Role role
) {}
