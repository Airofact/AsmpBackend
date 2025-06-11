package org.airo.asmp.dto.entity;

import org.airo.asmp.model.OrganizationMember.OrganizationAlumniId;
import org.airo.asmp.model.OrganizationMember.Role;
import org.airo.asmp.model.entity.Organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationMemberCreateDto(
        OrganizationAlumniId id,
        Role role
) {
}
