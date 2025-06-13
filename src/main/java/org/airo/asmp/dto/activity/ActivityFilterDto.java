package org.airo.asmp.dto.activity;

import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.model.activity.Status;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for filtering {@link org.airo.asmp.model.activity.Activity}
 */
public record ActivityFilterDto(
        String title,
        String description,
        LocalDateTime startTimeBegin,
        LocalDateTime startTimeEnd,
        LocalDateTime endTimeBegin,
        LocalDateTime endTimeEnd,
        String location,
        Integer maxParticipants,
		AlumniFilterDto organizerFilter,
        String organizerName,
        Status status
) {}
