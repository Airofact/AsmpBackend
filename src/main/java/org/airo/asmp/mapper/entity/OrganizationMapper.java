package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.OrganizationCreateDto;
import org.airo.asmp.dto.entity.OrganizationUpdateDto;
import org.airo.asmp.model.entity.Organization;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {
	Organization toEntity(OrganizationCreateDto organizationCreateDto);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Organization partialUpdate(OrganizationUpdateDto dto, @MappingTarget Organization organization);
}