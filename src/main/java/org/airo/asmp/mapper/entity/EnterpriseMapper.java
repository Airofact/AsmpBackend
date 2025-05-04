package org.airo.asmp.mapper.entity;

import org.airo.asmp.dto.entity.EnterpriseCreateDto;
import org.airo.asmp.dto.entity.EnterpriseUpdateDto;
import org.airo.asmp.model.entity.Enterprise;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnterpriseMapper {
	@Mapping(source = "addedById", target = "addedBy.id")
	Enterprise toEntity(EnterpriseCreateDto enterpriseDto);

	@Mapping(source = "addedBy.id", target = "addedById")
	EnterpriseCreateDto toDto(Enterprise enterprise);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "addedById", target = "addedBy.id")
	Enterprise partialUpdate(EnterpriseCreateDto enterpriseDto, @MappingTarget Enterprise enterprise);


	Enterprise toEntity(EnterpriseUpdateDto enterpriseUpdateDto);

	EnterpriseUpdateDto toDto1(Enterprise enterprise);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	Enterprise partialUpdate(EnterpriseUpdateDto enterpriseUpdateDto, @MappingTarget Enterprise enterprise);
}