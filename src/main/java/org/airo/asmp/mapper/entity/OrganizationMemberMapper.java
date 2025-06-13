package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.dto.entity.OrganizationMemberUpdateDto;
import org.airo.asmp.model.entity.OrganizationMember;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMemberMapper {
    OrganizationMember toEntity(OrganizationMemberCreateDto dto);

    OrganizationMemberCreateDto toDto(OrganizationMember entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(OrganizationMemberUpdateDto dto, @MappingTarget OrganizationMember entity);
}
