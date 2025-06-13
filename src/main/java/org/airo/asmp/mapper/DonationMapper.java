package org.airo.asmp.mapper;

import org.airo.asmp.dto.donation.DonationCreateDto;
import org.airo.asmp.dto.donation.DonationUpdateDto;
import org.airo.asmp.model.donation.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for {@link Donation}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DonationMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "donor", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "donateTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Donation createDtoToEntity(DonationCreateDto dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "donor", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "donateTime", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(DonationUpdateDto dto, @MappingTarget Donation entity);
}
