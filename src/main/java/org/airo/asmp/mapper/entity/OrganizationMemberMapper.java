package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.OrganizationMemberCreateDto;
import org.airo.asmp.dto.entity.OrganizationMemberUpdateDto;
import org.airo.asmp.model.entity.OrganizationMember;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMemberMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "alumni", ignore = true)
    @Mapping(target = "joinTime", ignore = true)
    OrganizationMember toEntity(OrganizationMemberCreateDto dto);

    @Mapping(source = "alumni.id", target = "alumniId")
    OrganizationMemberCreateDto toDto(OrganizationMember entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(OrganizationMemberUpdateDto dto, @MappingTarget OrganizationMember entity);
}
