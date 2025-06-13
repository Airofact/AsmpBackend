package org.airo.asmp.dto.activity;

import java.time.LocalDateTime;
import java.util.UUID;
/**
 * DTO for {@link org.airo.asmp.model.activity.Activity}
 */
public record ActivityCreateDto(
		String title,
        String description,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String location,
        int maxParticipants,
        UUID organizer
        // 移除status字段，因为它现在是计算列
){ }
