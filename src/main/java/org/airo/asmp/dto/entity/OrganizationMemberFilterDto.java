package org.airo.asmp.dto.entity;

import org.airo.asmp.model.entity.OrganizationMember;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for filtering {@link OrganizationMember}
 */
public record OrganizationMemberFilterDto(
        UUID organizationId,
        UUID alumniId,
        OrganizationMember.Role role,
        LocalDateTime joinTimeBegin,
        LocalDateTime joinTimeEnd
) {}
