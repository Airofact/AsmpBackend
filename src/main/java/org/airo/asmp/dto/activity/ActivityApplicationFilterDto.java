package org.airo.asmp.dto.activity;

import org.airo.asmp.dto.entity.AlumniFilterDto;
import org.airo.asmp.model.activity.ActivityApplication;

import java.time.LocalDateTime;

/**
 * DTO for filtering {@link ActivityApplication}
 */
public record ActivityApplicationFilterDto(
        ActivityFilterDto activityFilter,
        AlumniFilterDto alumniFilter,
        LocalDateTime applyTimeBegin,
        LocalDateTime applyTimeEnd,
        Boolean signedIn
) {}
