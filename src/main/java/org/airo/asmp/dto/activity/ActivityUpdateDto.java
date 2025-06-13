package org.airo.asmp.dto.activity;

import java.time.LocalDateTime;

public record ActivityUpdateDto(String title,
                                String description,
                                LocalDateTime startTime,
                                LocalDateTime endTime,
                                String location,
                                int maxParticipants) {
    // 移除status字段，因为它现在是计算列
}
