package org.airo.asmp.mapper;

import org.airo.asmp.dto.NoticeCreateDto;
import org.airo.asmp.dto.NoticeUpdateDto;
import org.airo.asmp.model.notice.Notice;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoticeMapper {
    Notice toEntity(NoticeCreateDto dto);

    NoticeCreateDto toDto(Notice entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(NoticeUpdateDto dto, @MappingTarget Notice entity);
}
