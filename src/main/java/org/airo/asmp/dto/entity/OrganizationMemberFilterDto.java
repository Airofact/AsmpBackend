package org.airo.asmp.dto.entity;

import org.airo.asmp.model.entity.OrganizationMember;

import java.time.LocalDateTime;

/**
 * DTO for filtering {@link OrganizationMember}
 */
public record OrganizationMemberFilterDto(
        OrganizationFilterDto organizationFilter,
        AlumniFilterDto alumniFilter,
        OrganizationMember.Role role,
        LocalDateTime joinTimeBegin,
        LocalDateTime joinTimeEnd
) {}
